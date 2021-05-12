package Dronazon;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;
import java.util.Map;

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

            Map<Integer, DeliveriesGenerator> payload;

            System.out.println(clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            System.out.println(clientId + " Connected");

            while(true){

                delivery.generateDelivery();
                payload = new HashMap<>();
                payload.put(deliveryID , delivery);
                deliveryID ++;

                gson = new Gson();
                String deliveryToJson = gson.toJson(payload);

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
