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
import java.util.Iterator;

public class MasterDroneController extends DroneController{

    private ArrayList<DroneStats> netDroneStats = new ArrayList<>();
    private ArrayList<GlobalStat> globalStats = new ArrayList<>();
    private ArrayList<DeliveriesGenerator> deliveryQueue = new ArrayList<>();
    private static MasterDroneController masterDroneController;
    private ArrayList<Drone> drones = new ArrayList<>();

    public static MasterDroneController getInstance(){
        if(masterDroneController == null)
            masterDroneController = new MasterDroneController();
        return masterDroneController;
    }

    @Override
    public ArrayList<Drone> getDronesList() {
        return drones;
    }

    //alla nomina del drone a master questo richiede a tutti i droni le loro informazioni e gli comunica che lui è il master, inoltre avvia il thread per connettersi a Dronazon
    public void initialize() {

        ArrayList<Drone> templist = DroneController.getInstance().getDronesList();

        ManagedChannel channel;
        DronesMessagesGrpc.DronesMessagesBlockingStub stub;

        if (templist.size() == 1)
            drones.add(DroneController.getInstance().getCurrDrone());

        if (templist.size() > 1) {
            for (Drone targetDrone : templist) {
                if (targetDrone != DroneController.getInstance().getCurrDrone()) {
                    channel = ManagedChannelBuilder.forTarget(targetDrone.getHost() + ":" + targetDrone.getPort()).usePlaintext().build();
                    stub = DronesMessagesGrpc.newBlockingStub(channel);
                    DronesMessagesOuterClass.Empty request = DronesMessagesOuterClass.Empty.newBuilder().build();

                    DronesMessagesOuterClass.DroneInfo reply = stub.getDroneInformations(request);

                    Drone tempDrone = new Drone(reply.getId(), reply.getPort(), reply.getHost());
                    tempDrone.setPosition(reply.getCoordinateX(), reply.getCoordinateY());
                    tempDrone.setBatteryLevel(reply.getBattery());
                    drones.add(tempDrone);

                    channel.shutdownNow();
                }
            }
        }
        System.out.println("\nsize della lista del master: "+drones.size());    //temp
        
        //deliveriesBrokerConnection();

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

    private void deliveriesBrokerConnection() {

        Thread deliveryGetter = new Thread(this::launchMQTTSub);
        deliveryGetter.start();

    }

    private void launchMQTTSub(){

        final String broker = "tcp://localhost:1883";
        final String clientId = String.valueOf(DroneController.getInstance().getCurrDrone().getId());
        Gson gson = new Gson();
        final String topic = "dronazon/smartcity/orders/";
        final int qos = 2;

            try{
                MqttClient client = new MqttClient(broker,clientId);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);

                // Connect the client
                System.out.println("Master Drone id: " + clientId + ". Connecting to Broker " + broker);
                client.connect(connOpts);

                client.setCallback(new MqttCallback() {

                    public void messageArrived(String topic, MqttMessage message) {
                        String receivedMessage = new String(message.getPayload());
                        DeliveriesGenerator delivery = gson.fromJson(receivedMessage, DeliveriesGenerator.class);
                        deliveryQueue.add(delivery);
                    }

                    public void connectionLost(Throwable cause) {
                        System.out.println(clientId + " Connectionlost! cause:" + cause.getMessage());
                    }

                    public void deliveryComplete(IMqttDeliveryToken token) {
                        // Not used here
                    }
                });
                client.subscribe(topic,qos);
                System.out.println(clientId + " Subscribed to topics : " + topic);

                //client.disconnect();  da fare quando si sconnette

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void addStat(DroneStats stats){

        netDroneStats.add(stats);
        System.out.println("stat list size: "+netDroneStats.size());
        System.out.println("stat "+ netDroneStats.get(0).getKmCovered());//temp

    }

    @Override
    public void addDroneToList (Drone newDrone){
        synchronized (this) {
            drones.add(newDrone);
        }
    }

    public void assignDelivery(){

        Iterator<DeliveriesGenerator> iterator = deliveryQueue.iterator();
        Drone bestDrone = null;
        ManagedChannel channel;
        DronesMessagesGrpc.DronesMessagesBlockingStub stub;

        while (iterator.hasNext())
            bestDrone = getClosestAvailableDrone(iterator.next().getPickUpPoint());

        channel = ManagedChannelBuilder.forTarget(bestDrone.getHost()+":"+bestDrone.getPort()).usePlaintext().build();
        stub = DronesMessagesGrpc.newBlockingStub(channel);
        DronesMessagesOuterClass.DeliveryInfo request = DronesMessagesOuterClass.DeliveryInfo.newBuilder().build(); //sistemare

        DronesMessagesOuterClass.Empty reply = stub.assignDelivery(request);



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


}
//master vuole uscire ma non puo dare le consegne a nessuno, aspetta o posso fare un timeout dopo il quale esce
//syncro buffer del sensore di rilevazione
//global stat  solo su droni che hanno  fatto consegne? anche  su tutti i droni
//il clean è della prima parte e poi fa scorrere (?)
