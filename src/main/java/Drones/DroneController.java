package Drones;

import Dronazon.DeliveriesGenerator;
import Drones.SensorSimulator.BufferImpl;
import Drones.SensorSimulator.PM10Simulator;
import Drones.Threads.PeerListenerThread;
import Drones.Threads.PingThread;
import Drones.Threads.StatsThread;
import Model.Drone;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.DronesMessagesGrpc;
import proto.DronesMessagesOuterClass;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/*TODO
- concorrenza
- casi limite
- uscita dei droni quando:   // - durante elezione il mio succ non esiste (aggiorno il mio succ e la lista di tutti)
- testare admininterface <- ci sono problemi
- timestamp consegne e pm10
*/
public class DroneController {

    private ArrayList<Drone> dronesList = new ArrayList<>();
    private static DroneController droneController;
    PeerListenerThread listenerThread;
    private Drone drone;
    private Drone succDrone;
    public int masterID;
    public int masterPort;
    public String masterHost = "";
    public boolean electionInProgress = false;
    public int totalNumberDeliveries;
    public float totalKm = 0;
    public boolean notDeliverying = true;
    private ArrayList<Double> PM10values;
    PM10Simulator simulator;
    StatsThread statsThread;

    public void initialize (ArrayList<Drone> dronesList, Drone drone){

        this.dronesList = dronesList;
        this.drone = drone;
        PM10values = new ArrayList<>();

        succDrone = setSuccDrone();

        if (dronesList.size() == 1) {
            this.drone.setMaster(true);
            setMasterInfos(drone.getId(), drone.getPort(), drone.getHost());
            MasterDroneController.getInstance().startDeliveryThreads();
        }

        grpcInitialize();

        if (drone.getId() != succDrone.getId())
            for (Drone targetDrone : dronesList) {
                Thread greetThread = new Thread(() -> greeting(targetDrone));
                greetThread.start();
            }

        PingThread pingThread = new PingThread();
        pingThread.start();

        simulator = new PM10Simulator(new BufferImpl());
        simulator.start();

        statsThread = new StatsThread();
        statsThread.start();
    }

    public void deliver (DeliveriesGenerator currentDelivery) {

        if (notDeliverying){
            notDeliverying = false;
            System.out.println(" INIZIO   battery :"+drone.getBatteryLevel()+" posiz partenza "+drone.getPosition_x() + " "+drone.getPosition_y()+"   pickup"+currentDelivery.getPickUpPoint().getX()+currentDelivery.getPickUpPoint().getY());

            //calcolo km percorsi
            float d1 = (float) Math.sqrt(Math.pow((currentDelivery.getPickUpPoint().getX() - drone.getPosition_x()),2) + Math.pow((currentDelivery.getPickUpPoint().getY() - drone.getPosition_y()),2));
            float d2 = (float) Math.sqrt(Math.pow((currentDelivery.getPickUpPoint().getX() - currentDelivery.getDeliveryPoint().getX()),2) + Math.pow((currentDelivery.getPickUpPoint().getY() - currentDelivery.getDeliveryPoint().getY()),2));
            float km = d1+d2;
            totalKm += km;

            //setto la nuova posizione del drone
            drone.setPosition(currentDelivery.getDeliveryPoint().getX(), currentDelivery.getDeliveryPoint().getY());

            //diminuisco la batteria
            drone.changeBatteryLevel();

            totalNumberDeliveries ++;

            //il drone effettua la consegna
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //timestamp di arrivo al luogo di consegna
            //String ts = new Timestamp(System.currentTimeMillis()).toString();

            double pm10 = 0;
            synchronized (PM10values) { //serve syncro?

                for (int i = 0 ; i < PM10values.size(); i++){
                    pm10 += PM10values.get(i);
                }
                pm10 /= PM10values.size();
                PM10values.clear();
            }

            System.out.println(" FINE   battery :"+drone.getBatteryLevel()+" nuova posiz "+drone.getPosition_x() + " "+drone.getPosition_y() + "     "+totalNumberDeliveries);  //temp

            //invio le statistiche al drone master
            sendStats(Float.parseFloat("22"),km, pm10); //fix ts

            notDeliverying = true;
        }

        if (drone.getBatteryLevel() < 15)
            droneLogout();
    }

    public void droneLogout (){

        if (drone.isMaster())
            MasterDroneController.getInstance().disconnectOperations();

        synchronized (listenerThread) {
            listenerThread.shutdown();
        }
        simulator.stopMeGently();

        DroneRESTClient.getInstance().leaveRequest(drone);

        System.out.println("Il drone esce dalla rete.");
        System.exit(0);
    }

    public static DroneController getInstance(){
        if(droneController == null)
            droneController = new DroneController();
        return droneController;
    }

    public ArrayList<Drone> getDronesList() {
        return dronesList;
    }       //tolto syncro ???????

    //questo metodo gestisce l'anello, assegnando al drone il suo successivo in ordine crescente per id, il metodo utilizza un id
    //inizialmente settato ad un valore molto grande per gestire la richiusura dell'anello su se stesso e collegare il drone
    //con id più piccolo a quello con id più grande
    public synchronized Drone setSuccDrone() {

        ArrayList<Drone> list = this.dronesList;
        boolean exists = false;
        int id = 9999999;

        //il caso di un solo drone nell'anello che avrà come successivo se stesso
        if (list.size() == 1){
            id = drone.getId();
            exists = true;
        }

        //il caso generale
        if (!exists){
            for (Drone d : list){
                if (drone.getId() < d.getId() && id > d.getId()){
                    id = d.getId();
                    exists = true;
                }
            }
        }

        // gestisce il caso di un drone con id maggiore di tutti gli altri droni presenti nell'anello
        if (!exists){
            id = 9999999;
            for (Drone d : list){
                if (drone.getId() > d.getId() && id > d.getId()){
                    id = d.getId();
                }
            }
        }

        for (Drone d : list){
            if (d.getId() == id)
                return d;
        }

        return null;
    }

    private void grpcInitialize() {

        listenerThread = new PeerListenerThread();
        listenerThread.start();

        try {
            synchronized (listenerThread) {
                listenerThread.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void greeting(Drone targetDrone) {

        ManagedChannel channel = ManagedChannelBuilder.forTarget(targetDrone.getHost()+":"+targetDrone.getPort()).usePlaintext().build();
        DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
        DronesMessagesOuterClass.DroneData request = DronesMessagesOuterClass.DroneData.newBuilder().setId(drone.getId()).setPort(drone.getPort()).setHost(drone.getHost()).build();

        try {
            DronesMessagesOuterClass.DroneData reply = stub.greet(request);

            if (reply.getId() != 0)
                setMasterInfos(reply.getId(), reply.getPort(), reply.getHost());

            if (succDrone.getId() == masterID)
                succDrone.setMaster(true);

            if (masterID != 0 && masterID != getCurrDrone().getId()) {
                DronesMessagesOuterClass.DroneInfo info = DronesMessagesOuterClass.DroneInfo.newBuilder().setId(getCurrDrone().getId()).setPort(getCurrDrone().getPort()).setHost(getCurrDrone().getHost()).setCoordinateX(getCurrDrone().getPosition_x()).setCoordinateY(getCurrDrone().getPosition_y()).setBattery(getCurrDrone().getBatteryLevel()).build();

                DronesMessagesOuterClass.Empty emptyReply = stub.sendDroneInfoToMaster(info);
            }
        }catch (Throwable t){
            updateList(getByID(targetDrone.getId()));
        }
        finally {
            channel.shutdownNow();
        }
    }

    private void sendStats(float timestamp, float km, double pm10){

        ManagedChannel channel = ManagedChannelBuilder.forTarget(masterHost + ":" + masterPort).usePlaintext().build();
        DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
        DronesMessagesOuterClass.DroneStats stats = DronesMessagesOuterClass.DroneStats.newBuilder().setTimestamp(timestamp).setCoordinateX(drone.getPosition_x()).setCoordinateY(drone.getPosition_y()).setKm(km).setAvgPM10(pm10).setBattery(drone.getBatteryLevel()).setDroneID(drone.getId()).build();

        DronesMessagesOuterClass.Empty reply = stub.sendStats(stats);

        channel.shutdownNow();
    }

    public void election( Drone tokenDrone, Drone to){

        System.out.println("Election of the new master started!");

        ManagedChannel channel = ManagedChannelBuilder.forTarget(to.getHost() + ":" + to.getPort()).usePlaintext().build();
        DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
        DronesMessagesOuterClass.DroneData request = DronesMessagesOuterClass.DroneData.newBuilder().setId(tokenDrone.getId()).setPort(tokenDrone.getPort()).setHost(tokenDrone.getHost()).build();

        DronesMessagesOuterClass.Empty reply = stub.election(request);

        channel.shutdownNow();
    }

    public void elected( Drone tokenDrone, Drone to){

        ManagedChannel channel = ManagedChannelBuilder.forTarget(to.getHost() + ":" + to.getPort()).usePlaintext().build();
        DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
        DronesMessagesOuterClass.DroneData request = DronesMessagesOuterClass.DroneData.newBuilder().setId(tokenDrone.getId()).setPort(tokenDrone.getPort()).setHost(tokenDrone.getHost()).build();

        DronesMessagesOuterClass.Empty reply = stub.elected(request);

        channel.shutdownNow();
    }

    public void addDroneToList (Drone newDrone){
        synchronized (this) {
            dronesList.add(newDrone);
            succDrone = setSuccDrone();
        }
    }

    public Drone getByID(int id){

        ArrayList<Drone> tempList = getDronesList();
        for (Drone d : tempList){
            if (d.getId() == id)
                return d;}
        return null;
    }

    public void setMasterInfos(int id, int port, String host){

        masterID = id;
        masterPort = port;
        masterHost = host;
    }

    public Drone getCurrDrone() {
        return drone;
    }
    public Drone getSuccDrone() {
        return succDrone;
    }

    public void updateList(Drone targetDrone) {
        synchronized (this) {
            dronesList.remove(targetDrone);
            succDrone = setSuccDrone();
        }
    }

    public void setDelivery (DeliveriesGenerator delivery){
        Thread deliveryThread = new Thread(() -> deliver(delivery));
        deliveryThread.start();
    }

    public void addPM10value(Double PM10value) {
        PM10values.add(PM10value);
    }

    public void getDronesInfo(Drone targetDrone){

        ManagedChannel channel = ManagedChannelBuilder.forTarget(targetDrone.getHost() + ":" + targetDrone.getPort()).usePlaintext().build();
        DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);

        DronesMessagesOuterClass.DroneInfo request = DronesMessagesOuterClass.DroneInfo.newBuilder().setId(drone.getId()).setPort(drone.getPort()).setHost(drone.getHost()).setBattery(drone.getBatteryLevel()).setCoordinateX(drone.getPosition_x()).setCoordinateY(drone.getPosition_y()).build();

        DronesMessagesOuterClass.Empty reply = stub.sendDroneInfoToMaster(request);
        channel.shutdownNow();

    }
}
