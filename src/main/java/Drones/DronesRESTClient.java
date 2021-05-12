package Drones;

import Model.Drone;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class DronesRESTClient {

    private String serverHost;
    private int serverPort;
    private Drone drone;
    private Client client;

    private static DronesRESTClient dronesRESTClient;

    public static DronesRESTClient getInstance(){
        if(dronesRESTClient == null)
            dronesRESTClient = new DronesRESTClient();
        return dronesRESTClient;
    }

    public void initialize (String serverHost, int serverPort, Drone drone){
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.drone = drone;
    }

    public void addRequest(){

        try {
            Gson gson = new Gson();
            String droneToJson = gson.toJson(drone);

            client = Client.create();
            WebResource webResource = client.resource("http://" + serverHost + ":" + serverPort + "/drones/add");
            ClientResponse response = webResource.type("application/json").accept("application/json").post(ClientResponse.class, droneToJson);

            if (response.getStatus() == 400) {
                throw new RuntimeException("Operation failed with error code: " + response.getStatus());
            }
            else if (response.getStatus() == 200) {

                System.out.println(response.getEntity(String.class));
            }

        }catch (Exception e){
            System.out.println("Unable to reach the server");       //notificare che l'id esiste già
            System.exit(-1);
        }
    }

    //review quando si fa la logica dei droni
    public void deleteRequest (Drone drone) {

        try{
            Gson gson = new Gson();
            String droneToJson = gson.toJson(drone);

            client = Client.create();
            WebResource webResource = client.resource("http://" + serverHost + ":" + serverPort + "/drones/delete");
            ClientResponse response = webResource.type("application/json").accept("application/json").post(ClientResponse.class, droneToJson);

            System.out.println(response.getEntity(String.class));

        } catch (Exception e) {
            System.out.println("Unable to reach the server");       //se il drone non esiste già?
            System.exit(-1);
        }
    }


}
