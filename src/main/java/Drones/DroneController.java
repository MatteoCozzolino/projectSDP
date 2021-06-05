package Drones;

import Dronazon.DeliveriesGenerator;
import Drones.Threads.PeerListenerThread;
import Model.Drone;
import Model.DroneStats;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.DronesMessagesGrpc;
import proto.DronesMessagesOuterClass;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DroneController {

    private ArrayList<Drone> dronesList;
    private static DroneController droneController;
    private PeerListenerThread listenerThread;
    private Drone drone;
    private Drone succDrone;
    private DroneStats droneStats;
    public int masterID;            //posso  metterle  private?
    public int masterPort;
    public String masterHost = "";
    public boolean electionInProgress = false;
    private DeliveriesGenerator delivery = null;

    public void initialize (ArrayList<Drone> dronesList, Drone drone){

        this.dronesList = dronesList;
        this.drone = drone;
        droneStats = new DroneStats();

        succDrone = setSuccDrone();

        if (dronesList.size() == 1) {    //aggiungere l'initialize delle global e local measure + sensore pm10
            this.drone.setMaster(true);
            System.out.println(this.drone.isMaster());  //temp
            setMasterInfos(drone.getId(), drone.getPort(), drone.getHost());
            MasterDroneController.getInstance().initialize();
        }

        grpcInitialize();


    }

    public synchronized void test(){

        while(true) {
            try {
                wait(5000); //incrementare per i test
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendStats(0,0);        //temp

        }}

    public void droneLifecycle () {

        while (getCurrDrone().getBatteryLevel() > 15){
            if (delivery != null){
                System.out.println(" INIZIO   battery :"+drone.getBatteryLevel()+" posiz partenza "+drone.getPosition_x() + " "+drone.getPosition_y());
                //calcolo km percorsi
                float d1 = (float) Math.sqrt(Math.pow((delivery.getPickUpPoint().getX() - drone.getPosition_x()),2) + Math.pow((delivery.getPickUpPoint().getY() - drone.getPosition_y()),2));
                float d2 = (float) Math.sqrt(Math.pow((delivery.getPickUpPoint().getX() - delivery.getDeliveryPoint().getX()),2) + Math.pow((delivery.getPickUpPoint().getY() - delivery.getDeliveryPoint().getY()),2));
                float km = d1+d2;

                //setto la nuova posizione del drone
                drone.setPosition(delivery.getDeliveryPoint().getX(), delivery.getDeliveryPoint().getY());

                //diminuisco la batteria
                drone.changeBatteryLevel();

                //medie PM10
                //wip

                //il drone effettua la consegna
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //timestamp di arrivo al luogo di consegna
                //String ts = new Timestamp(System.currentTimeMillis()).toString();

                System.out.println(" FINE   battery :"+drone.getBatteryLevel()+" nuova posiz "+drone.getPosition_x() + " "+drone.getPosition_y());

                //invio le statistiche al drone master
                sendStats(Float.parseFloat("22"),km); //fix ts
            }

            delivery = null;
        }

        if (drone.getBatteryLevel() < 15){

            System.exit(0);
            //da fare, forse in un thread?

        }



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
    public Drone setSuccDrone() {

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

        try{        //riguardare
            synchronized (listenerThread){
                listenerThread.wait();}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (drone.getId() != succDrone.getId())
            greeting();

    }

    private void greeting() {

        ManagedChannel channel;
        DronesMessagesGrpc.DronesMessagesBlockingStub stub;

        for(Drone targetDrone : dronesList) {
            try{
                channel = ManagedChannelBuilder.forTarget(targetDrone.getHost()+":"+targetDrone.getPort()).usePlaintext().build();
                stub = DronesMessagesGrpc.newBlockingStub(channel);
                DronesMessagesOuterClass.DroneData request = DronesMessagesOuterClass.DroneData.newBuilder().setId(drone.getId()).setPort(drone.getPort()).setHost(drone.getHost()).build();

                DronesMessagesOuterClass.DroneData reply = stub.greet(request);

                //solo il drone master invierà le sue informazioni
                if (reply.getId() != 0)
                    setMasterInfos(reply.getId(), reply.getPort(), reply.getHost());

                System.out.println(masterID + "----" + masterPort + "----" + masterHost);   //temp

                channel.shutdownNow();

            } catch (Throwable t) {

                System.out.println("Drone with ID: "+targetDrone.getId() +" disconnected, updating peers...");
                updateList(targetDrone);
                removeDrone(targetDrone);

            }
        }

        //il drone che è entrato nella rete invierà le sue informazioni su batteria e posizione al master
        if (masterID != 0 && masterID != getCurrDrone().getId()){
            channel = ManagedChannelBuilder.forTarget(masterHost+":"+masterPort).usePlaintext().build();
            stub = DronesMessagesGrpc.newBlockingStub(channel);
            DronesMessagesOuterClass.DroneInfo info = DronesMessagesOuterClass.DroneInfo.newBuilder().setId(getCurrDrone().getId()).setPort(getCurrDrone().getPort()).setHost(getCurrDrone().getHost()).setCoordinateX(getCurrDrone().getPosition_x()).setCoordinateY(getCurrDrone().getPosition_y()).setBattery(getCurrDrone().getBatteryLevel()).build();

            DronesMessagesOuterClass.Empty reply = stub.sendDroneInfoToMaster(info);

            channel.shutdownNow();

        }
    }

    public void removeDrone(Drone targetDrone) {





    }

    private void sendStats(float timestamp, float km){

        try {
            ManagedChannel channel = ManagedChannelBuilder.forTarget(masterHost + ":" + masterPort).usePlaintext().build();
            DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
            DronesMessagesOuterClass.DroneStats stats = DronesMessagesOuterClass.DroneStats.newBuilder().setTimestamp(timestamp).setCoordinateX(drone.getPosition_x()).setCoordinateY(drone.getPosition_y()).setKm(km).setAvgPM10(12).setBattery(drone.getBatteryLevel()).build();
            //sistemare set pm10

            DronesMessagesOuterClass.Empty reply = stub.sendStats(stats);

            channel.shutdownNow();
        }catch (Throwable t){

            System.out.println("The drone master is offline!");
            updateList(getByID(masterID));
            if (!electionInProgress){
                electionInProgress = true;
                election(drone, succDrone);   }
        }

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
        for (Drone d : tempList)
            if (d.getId() == id)
                return d;
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
        //synchronized (this) {
            dronesList.remove(targetDrone);
            succDrone = setSuccDrone();
        //}
    }

    public void setDelivery (DeliveriesGenerator delivery){this.delivery = delivery;}
}
