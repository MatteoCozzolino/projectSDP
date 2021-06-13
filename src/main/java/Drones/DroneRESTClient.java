package Drones;

import Model.Drone;
import Model.GlobalStat;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;


public class DroneRESTClient {

    private String serverHost;
    private int serverPort;
    private Drone drone;
    private Client client;

    private static DroneRESTClient droneRESTClient;

    public static DroneRESTClient getInstance(){
        if(droneRESTClient == null)
            droneRESTClient = new DroneRESTClient();
        return droneRESTClient;
    }

    public void initialize (String serverHost, int serverPort, Drone drone){
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.drone = drone;
    }

    public ArrayList<Drone> addRequest(){

        ArrayList<Drone> droneList = null;

        try {
            Gson gson = new Gson();
            String droneToJson = gson.toJson(drone);

            client = Client.create();
            WebResource webResource = client.resource("http://" + serverHost + ":" + serverPort + "/drones/add/drone");
            ClientResponse response = webResource.type("application/json").accept("application/json").post(ClientResponse.class, droneToJson);

            if (response.getStatus() == 400) {
                System.out.println("A drone with the same ID already exists, please start again!");
                System.exit(-1);
            }
            else if (response.getStatus() == 200) {

                droneList = gson.fromJson(response.getEntity(String.class), new TypeToken<ArrayList<Drone>>(){}.getType());
                System.out.println("Connection succesful! Waiting for peers...");

            }

        }catch (Exception e){
            System.out.println("Unable to reach the server");
            System.exit(-1);
        }

        return droneList;
    }

    //review quando si fa la logica dei droni
    public void leaveRequest (Drone drone) {

        try{
            Gson gson = new Gson();
            String droneToJson = gson.toJson(drone);

            client = Client.create();
            WebResource webResource = client.resource("http://" + serverHost + ":" + serverPort + "/drones/delete");
            ClientResponse response = webResource.type("application/json").accept("application/json").post(ClientResponse.class, droneToJson);

        } catch (Exception e) {
            System.out.println("Unable to reach the server");
            System.exit(-1);
        }
    }


    public void sendGlobalStats(GlobalStat globalStat) {

        try {
            Gson gson = new Gson();
            String globalStatToJson = gson.toJson(globalStat);

            client = Client.create();
            WebResource webResource = client.resource("http://" + serverHost + ":" + serverPort + "/drones/add/globalStats");
            ClientResponse response = webResource.type("application/json").accept("application/json").post(ClientResponse.class, globalStatToJson);

        }catch (Exception e){
            System.out.println("Unable to reach the server");
            System.exit(-1);
        }

    }
}
