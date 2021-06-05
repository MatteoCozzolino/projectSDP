package Dronazon;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Dronazon {

    public static void main(String[] args) {

        MqttClient client;
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        DeliveriesGenerator delivery = new DeliveriesGenerator();
        Gson gson;
        int deliveryID = 0;
        String topic = "dronazon/smartcity/orders/";
        int qos = 2;

        try {
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Connecting Broker " + broker);
            client.connect(connOpts);
            System.out.println(" Connected!");

            while(true){

                delivery.generateDelivery(deliveryID);
                deliveryID ++;

                gson = new Gson();
                String deliveryToJson = gson.toJson(delivery);

                MqttMessage message = new MqttMessage(deliveryToJson.getBytes());
                message.setQos(qos);
                System.out.println("Sending delivery information with id: " + deliveryID + " ...");
                client.publish(topic, message);

                Thread.sleep(5000); //5 seconds waiting time before generating another delivery

            }

        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

}