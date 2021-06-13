package Drones;

import Dronazon.DeliveriesGenerator;
import Model.Coordinates;
import Model.Drone;
import Model.DroneStats;
import Model.GlobalStat;
import com.google.gson.Gson;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.eclipse.paho.client.mqttv3.*;
import proto.DronesMessagesGrpc;
import proto.DronesMessagesOuterClass;

import java.util.ArrayList;

public class MasterDroneController extends DroneController{

    private ArrayList<DroneStats> netDroneStats = new ArrayList<>();
    private ArrayList<GlobalStat> globalStats = new ArrayList<>();
    private ArrayList<DeliveriesGenerator> deliveryQueue = new ArrayList<>();
    private static MasterDroneController masterDroneController;
    private MqttClient mqttClient;
    private Thread deliveryGetter;

    public static MasterDroneController getInstance(){
        if(masterDroneController == null)
            masterDroneController = new MasterDroneController();
        return masterDroneController;
    }

    //alla nomina del drone a master questo richiede a tutti i droni le loro informazioni e gli comunica che lui è il master, inoltre avvia il thread per connettersi a Dronazon
    public void initialize() {

        ArrayList<Drone> templist = DroneController.getInstance().getDronesList();

        if (templist.size() > 1) {
            for (Drone targetDrone : templist) {
                Thread getInfoThread; //controllare la concorrenza
                if (targetDrone != DroneController.getInstance().getCurrDrone()) {
                    getInfoThread = new Thread(() -> getDronesInfo(targetDrone));
                    getInfoThread.start();
                }
            }
        }
        
        deliveriesBrokerConnection();

        /*//test
        while(true){

            for (DeliveriesGenerator a :deliveryQueue){
                System.out.println("size: "+deliveryQueue.size());
            System.out.println(a.getPickUpPoint().getX() + " asd " +a.getPickUpPoint().getY());
            System.out.println("deliv point "+a.getDeliveryPoint().getX() + " asd " +a.getDeliveryPoint().getY());
            }
        }

        try {
            Thread.sleep(5000); //test
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("deliv point "+deliveryQueue.get(0).getDeliveryPoint().getX() + " asd " +deliveryQueue.get(0).getDeliveryPoint().getY());
        DroneController.getInstance().setDelivery(deliveryQueue.get(0));*/
    }

    private void getDronesInfo(Drone targetDrone){

        ManagedChannel channel = ManagedChannelBuilder.forTarget(targetDrone.getHost() + ":" + targetDrone.getPort()).usePlaintext().build();
        DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
        DronesMessagesOuterClass.Empty request = DronesMessagesOuterClass.Empty.newBuilder().build();

        DronesMessagesOuterClass.DroneInfo reply = stub.getDroneInformations(request);

        DroneController.getInstance().getByID(reply.getId()).setBatteryLevel(reply.getBattery());
        DroneController.getInstance().getByID(reply.getId()).setPosition(reply.getCoordinateX(), reply.getCoordinateY());

        channel.shutdownNow();

    }

    private void deliveriesBrokerConnection() {

        deliveryGetter = new Thread(this::launchMQTTSub);
        deliveryGetter.start();
        try {   //da testare
            deliveryGetter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void launchMQTTSub(){

        final String broker = "tcp://localhost:1883";
        final String clientId = String.valueOf(DroneController.getInstance().getCurrDrone().getId());
        Gson gson = new Gson();
        final String topic = "dronazon/smartcity/orders/";
        final int qos = 2;

        try{
            mqttClient = new MqttClient(broker,clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println("Master Drone id: " + clientId + ". Connecting to Broker " + broker);
            mqttClient.connect(connOpts);

            mqttClient.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) {
                    String receivedMessage = new String(message.getPayload());
                    DeliveriesGenerator delivery = gson.fromJson(receivedMessage, DeliveriesGenerator.class);
                    //deliveryQueue.add(delivery);
                    assignDelivery(delivery);
                }

                public void connectionLost(Throwable cause) {
                    System.out.println(clientId + " Connectionlost! cause:" + cause.getMessage());
                    cause.printStackTrace();
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used here
                }
            });
            mqttClient.subscribe(topic,qos);
            System.out.println(clientId + " Subscribed to topics : " + topic);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void addStat(DroneStats stats){ //cotrollare syncro

        netDroneStats.add(stats);
        System.out.println("stat list size: "+ netDroneStats.size());
        System.out.println("stat "+ netDroneStats.get(0).getKmCovered());//temp

    }

    public void assignDelivery(DeliveriesGenerator delivery){


            //ArrayList<DeliveriesGenerator> a = new ArrayList<>(deliveryQueue);
            //Iterator<DeliveriesGenerator> iterator = a.iterator();
            Drone bestDrone = null;
            ManagedChannel channel;
            DronesMessagesGrpc.DronesMessagesBlockingStub stub;

            //DeliveriesGenerator delivery = a.get(a.size() - 1);

            //System.out.println(a.size() + "     " + deliveryQueue.size());
            //while (iterator.hasNext()) {
            bestDrone = getClosestAvailableDrone(delivery.getPickUpPoint());
            System.out.println("assegno delivery " + delivery.getPickUpPoint().getX() + " " + delivery.getPickUpPoint().getY() + " a drone " + bestDrone.getId() + " del ID " + delivery.getDeliveryID());
            channel = ManagedChannelBuilder.forTarget(bestDrone.getHost() + ":" + bestDrone.getPort()).usePlaintext().build();
            stub = DronesMessagesGrpc.newBlockingStub(channel);
            DronesMessagesOuterClass.DeliveryInfo request = DronesMessagesOuterClass.DeliveryInfo.newBuilder().setDeliveryID(delivery.getDeliveryID()).setPickUpX(delivery.getPickUpPoint().getX()).setPickUpY(delivery.getPickUpPoint().getY()).setDeliveryX(delivery.getDeliveryPoint().getX()).setDeliveryY(delivery.getDeliveryPoint().getY()).build();
            System.out.println("asdasdasd");
            DronesMessagesOuterClass.Empty reply = stub.assignDelivery(request);
            System.out.println("hiuwhgt");
            //}
            //deliveryQueue.remove(delivery);



    }

    private Drone getClosestAvailableDrone(Coordinates pickUpPoint){

        ArrayList<Drone> tempList = this.getDronesList();
        Drone closestDrone = new Drone();
        closestDrone.setBatteryLevel(0);
        float minDistance = 20;

        for (Drone d : tempList){
            if (!d.deliveryInProgress) {
                float distance = (float) Math.sqrt(Math.pow((pickUpPoint.getX() - d.getPosition_x()), 2) + Math.pow((pickUpPoint.getY() - d.getPosition_y()), 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestDrone = d;
                }
                if (distance == minDistance){
                    if (d.getBatteryLevel() > closestDrone.getBatteryLevel())
                        closestDrone = d;
                    if (d.getBatteryLevel() == closestDrone.getBatteryLevel()){
                        if (d.getId() > closestDrone.getId())
                            closestDrone = d;
                    }
                }
            }
        }

    return closestDrone;

    }

    public void disconnectOperations() {

        try {
            mqttClient.disconnect();
        }
        catch (MqttException exp){
            exp.printStackTrace();
        }

        while (deliveryQueue.size() > 0 && getDronesList().size() > 1)   //controllare
            //assignDelivery();

        if (deliveryQueue.size() == 0){

            //metto sleep?
            listenerThread.shutdown();

            globalStats.add(calculateGlobalStats());

            //invio al server master le ultime statistiche globali calcolate
            DroneRESTClient.getInstance().sendGlobalStats(globalStats.get(globalStats.size()-1));

            DroneRESTClient.getInstance().leaveRequest(this.getCurrDrone());

            System.out.println("Il drone esce dalla rete.");
            System.exit(0);
        }
    }

    private GlobalStat calculateGlobalStats (){

        ArrayList<Drone> tempDroneList = getDronesList();
        ArrayList<DroneStats> tempDroneStatsList = new ArrayList<>(netDroneStats);

        float numberOfDrones = tempDroneList.size();
        GlobalStat stats = new GlobalStat();

        //stats.setTimestamp();         da sistemare
        stats.setAvgNumberDeliveries(tempDroneStatsList.size()/numberOfDrones);

        int sumKm = 0;
        int sumPM10 = 0;
        int sumBattery = 0;

        for (DroneStats ds : tempDroneStatsList){
            sumKm += ds.getKmCovered();
            sumPM10 += ds.getAvgPM10();
            sumBattery += ds.getNewBatteryLevel();
        }

        stats.setAvgKm(sumKm/numberOfDrones);
        stats.setAvgPM10(sumPM10/numberOfDrones);
        stats.setAvgResidualBatteries(sumBattery/numberOfDrones);

        netDroneStats.removeAll(tempDroneStatsList);

        return stats;
    }

}
//master vuole uscire ma non puo dare le consegne a nessuno, aspetta o posso fare un timeout dopo il quale esce
//syncro buffer del sensore di rilevazione
//il clean è della prima parte e poi fa scorrere (?)
