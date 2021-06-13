package Drones;

import Dronazon.DeliveriesGenerator;
import Drones.Threads.PeerListenerThread;
import Drones.Threads.PingThread;
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
    PeerListenerThread listenerThread;
    private Drone drone;
    private Drone succDrone;
    private DroneStats droneStats;
    public int masterID;
    public int masterPort;
    public String masterHost = "";
    public boolean electionInProgress = false;
    public DeliveriesGenerator currentDelivery = new DeliveriesGenerator();
    private int totalNumberDeliveries;
    public boolean notDeliverying = true;

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

    public synchronized void test(){        //temp

        while(true) {
            try {
                wait(5000); //incrementare per i test
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendStats(0,0);

        }}

    public void droneLifecycle () {

        PingThread pingThread = new PingThread();
        pingThread.start(); //da rivedere quando far partire

        while (drone.getBatteryLevel() > 15){

            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
//System.out.println( "  id    aaaa");        //il !=0 da problemi
            if (currentDelivery.getDeliveryID() != 0 && notDeliverying){
                notDeliverying = false;
                System.out.println(" INIZIO   battery :"+drone.getBatteryLevel()+" posiz partenza "+drone.getPosition_x() + " "+drone.getPosition_y());

                //calcolo km percorsi
                float d1 = (float) Math.sqrt(Math.pow((currentDelivery.getPickUpPoint().getX() - drone.getPosition_x()),2) + Math.pow((currentDelivery.getPickUpPoint().getY() - drone.getPosition_y()),2));
                float d2 = (float) Math.sqrt(Math.pow((currentDelivery.getPickUpPoint().getX() - currentDelivery.getDeliveryPoint().getX()),2) + Math.pow((currentDelivery.getPickUpPoint().getY() - currentDelivery.getDeliveryPoint().getY()),2));
                float km = d1+d2;

                //setto la nuova posizione del drone
                drone.setPosition(currentDelivery.getDeliveryPoint().getX(), currentDelivery.getDeliveryPoint().getY());

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

                totalNumberDeliveries ++;
                System.out.println(" FINE   battery :"+drone.getBatteryLevel()+" nuova posiz "+drone.getPosition_x() + " "+drone.getPosition_y() + "     "+totalNumberDeliveries);

                //invio le statistiche al drone master
                sendStats(Float.parseFloat("22"),km); //fix ts
                currentDelivery.setDeliveryID(0);
                notDeliverying = true;

            }


        }
/*
        if (drone.getBatteryLevel() < 15){

            //il drone esce dalla rete (da aspettare  se sta consegnando, ancora da fare)

            if (drone.isMaster())
                MasterDroneController.getInstance().disconnectOperations();

            listenerThread.shutdown();

            DroneRESTClient.getInstance().leaveRequest(drone);

            System.out.println("Il drone esce dalla rete.");
            System.exit(0);

        }*/



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
            for (Drone targetDrone : dronesList) {          //controllare la concorrenza
                Thread greetThread = new Thread(() -> greeting(targetDrone));
                greetThread.start();
                /*try {
                    greetThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }

    }

    private void greeting(Drone targetDrone) {

        ManagedChannel channel = ManagedChannelBuilder.forTarget(targetDrone.getHost()+":"+targetDrone.getPort()).usePlaintext().build();
        DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
        DronesMessagesOuterClass.DroneData request = DronesMessagesOuterClass.DroneData.newBuilder().setId(drone.getId()).setPort(drone.getPort()).setHost(drone.getHost()).build();

        DronesMessagesOuterClass.DroneData reply = stub.greet(request);

        if (reply.getId() != 0)
            setMasterInfos(reply.getId(), reply.getPort(), reply.getHost());

        if (succDrone.getId() == masterID)
            succDrone.setMaster(true);

        System.out.println(masterID + "----" + masterPort + "----" + masterHost);   //temp

        if (masterID != 0 && masterID != getCurrDrone().getId()){
            DronesMessagesOuterClass.DroneInfo info = DronesMessagesOuterClass.DroneInfo.newBuilder().setId(getCurrDrone().getId()).setPort(getCurrDrone().getPort()).setHost(getCurrDrone().getHost()).setCoordinateX(getCurrDrone().getPosition_x()).setCoordinateY(getCurrDrone().getPosition_y()).setBattery(getCurrDrone().getBatteryLevel()).build();

            DronesMessagesOuterClass.Empty emptyReply = stub.sendDroneInfoToMaster(info);
        }

        channel.shutdownNow();

    }

    private void sendStats(float timestamp, float km){

        ManagedChannel channel = ManagedChannelBuilder.forTarget(masterHost + ":" + masterPort).usePlaintext().build();
        DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
        DronesMessagesOuterClass.DroneStats stats = DronesMessagesOuterClass.DroneStats.newBuilder().setTimestamp(timestamp).setCoordinateX(drone.getPosition_x()).setCoordinateY(drone.getPosition_y()).setKm(km).setAvgPM10(12).setBattery(drone.getBatteryLevel()).build();
        //sistemare set pm10

        DronesMessagesOuterClass.Empty reply = stub.sendStats(stats);

        channel.shutdownNow();

    }

    //controllare bene quando consegne sono finiti
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
        //synchronized (this) {
            dronesList.remove(targetDrone);
            succDrone = setSuccDrone();
        //}
    }

    public void setDelivery (DeliveriesGenerator delivery){this.currentDelivery = delivery;}
}
