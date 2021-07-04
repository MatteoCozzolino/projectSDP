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

import java.sql.Timestamp;
import java.util.ArrayList;

public class MasterDroneController extends DroneController{

    private final ArrayList<DroneStats> netDroneStats = new ArrayList<>();
    private final ArrayList<DeliveriesGenerator> deliveryQueue = new ArrayList<>();
    private static MasterDroneController masterDroneController;
    private MqttClient mqttClient;
    public int disableQuitCounter;

    public static MasterDroneController getInstance(){
        if(masterDroneController == null)
            masterDroneController = new MasterDroneController();
        return masterDroneController;
    }

    //I tre thread gestiscono la coda delle consegne e l'assegnamento delle stesse. Rispettivamente riempiono la coda con le consegne ricevute dall'MQTT broker, assegnano
    //la consegna al miglior drone e controllano la presenza di consegne nella coda, sincronizzandosi in base alla presenza di consegne o meno.
    public void startDeliveryThreads() {

        Thread elementPutter = new Thread(this::launchMQTTSub);
        elementPutter.start();
        try {
            elementPutter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread elementGetter = new Thread(this::deliveryQueueHandler);
        elementGetter.start();

        Thread elementChecker = new Thread(this::checker);
        elementChecker.start();
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
                    deliveryQueue.add(delivery);
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

    public synchronized void addStat(DroneStats stats){

        netDroneStats.add(stats);
    }

    private void deliveryQueueHandler () {

        while (true) {
            synchronized (deliveryQueue) {
                try {
                    deliveryQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            DeliveriesGenerator currDelivery = deliveryQueue.get(0);
            assignDelivery(currDelivery);
        }
    }

    private void checker () {

        while(true){
            synchronized (deliveryQueue){
            if (deliveryQueue.size() > 0){
                    deliveryQueue.notifyAll();
                }
            }
        }
    }

    public void assignDelivery(DeliveriesGenerator currDelivery){

        Drone bestDrone = getClosestAvailableDrone(currDelivery.getPickUpPoint());
        ManagedChannel channel = null;

        try{
            if (bestDrone.getId() == 0)
                return;

            disableQuitCounter++;

            channel = ManagedChannelBuilder.forTarget(bestDrone.getHost() + ":" + bestDrone.getPort()).usePlaintext().build();
            DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
            DronesMessagesOuterClass.DeliveryInfo request = DronesMessagesOuterClass.DeliveryInfo.newBuilder().setDeliveryID(currDelivery.getDeliveryID()).setPickUpX(currDelivery.getPickUpPoint().getX()).setPickUpY(currDelivery.getPickUpPoint().getY()).setDeliveryX(currDelivery.getDeliveryPoint().getX()).setDeliveryY(currDelivery.getDeliveryPoint().getY()).build();

            DronesMessagesOuterClass.Empty reply = stub.assignDelivery(request);
            DroneController.getInstance().getDronesList().get(DroneController.getInstance().getDronesList().indexOf(bestDrone)).setDeliveryInProgress(false);

            deliveryQueue.remove(0);
        }catch (Throwable t){
            disableQuitCounter--;
        }
        finally{
            if (channel != null) {
                channel.shutdownNow();
            }
        }
    }

    private Drone getClosestAvailableDrone(Coordinates pickUpPoint){

        Drone closestDrone = new Drone();
        closestDrone.setBatteryLevel(0);
        float minDistance = 20;

        for (Drone d : DroneController.getInstance().getDronesList()){
            if (!d.isDeliveryInProgress()) {
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
    if (closestDrone.getId() != 0)
        DroneController.getInstance().getDronesList().get(DroneController.getInstance().getDronesList().indexOf(closestDrone)).setDeliveryInProgress(true);

    return closestDrone;
    }

    public void disconnectOperations() {

        try {
            if (mqttClient.isConnected())
                mqttClient.disconnect();
        }
        catch (MqttException exp){
            exp.printStackTrace();
        }

        DroneController.getInstance().statsThread.stopThread();

        //Salvo lo stato corrente della coda delle consegne per valutare se il drone master ha ricevuto tutte le risposte dai droni prima di uscire della rete
        int deliveryQueueSnapshot = deliveryQueue.size();

        if (deliveryQueueSnapshot == 0 && disableQuitCounter == 0){
            DroneRESTClient.getInstance().sendGlobalStats(calculateGlobalStats());
        }
        else{
            Thread statsChecker = new Thread(this::checkRecievedStats);
            statsChecker.start();

            synchronized (this){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            DroneRESTClient.getInstance().sendGlobalStats(calculateGlobalStats());
        }
    }

    public void checkRecievedStats() {
        while (true)
            synchronized (this){
                if (deliveryQueue.size() == 0 && disableQuitCounter == 0){
                this.notify();
                break;
                }
            }
    }

    public GlobalStat calculateGlobalStats (){

        ArrayList<Drone> tempDroneList = DroneController.getInstance().getDronesList();
        ArrayList<DroneStats> tempDroneStatsList = new ArrayList<>(netDroneStats);

        float numberOfDrones = tempDroneList.size();
        GlobalStat stats = new GlobalStat();

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

        //calcolo il timestamp in secondi
        String ts = new Timestamp(System.currentTimeMillis()).toString().substring(14);
        float tsInSeconds = (Float.parseFloat(ts.substring(0,2))*60) + Float.parseFloat(ts.substring(3));
        stats.setTimestamp(tsInSeconds);

        netDroneStats.removeAll(tempDroneStatsList);

        return stats;
    }

}
