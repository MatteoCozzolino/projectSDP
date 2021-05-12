package Amministratore;

import Model.Drone;
import Model.GlobalStat;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;

public class AdminRESTClient {

    private String serverHost;
    private int serverPort;

    private Client client;

    private static AdminRESTClient adminRESTClient;

    public static AdminRESTClient getInstance(){
        if(adminRESTClient == null)
            adminRESTClient = new AdminRESTClient();
        return adminRESTClient;
    }

    public void initialize (String serverHost, int serverPort){
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        client = Client.create();
    }

    public ArrayList<Drone> droneListRequest() {

        Gson gson = new Gson();
        ArrayList<Drone> droneList = new ArrayList<>();

        try{

            WebResource webResource = client.resource("http://" + serverHost + ":" + serverPort + "/amministratore/dronesList");
            ClientResponse response = webResource.type("application/json").accept("application/json").get(ClientResponse.class);

            //TypeToken rappresenta un generico tipo T, Drone in questo caso, per facilitare la conversione da json al tipo di oggetto desiderato (una lista di oggetti Drone)
            droneList = gson.fromJson(response.getEntity(String.class), new TypeToken<ArrayList<Drone>>(){}.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return droneList;
    }

    public ArrayList<GlobalStat> lastStatsRequest (int n){

        Gson gson = new Gson();
        ArrayList<GlobalStat> statsList = new ArrayList<>();

        try{

            WebResource webResource = client.resource("http://" + serverHost + ":" + serverPort + "/amministratore/lastStats/" + n);
            ClientResponse response = webResource.type("application/json").accept("application/json").get(ClientResponse.class);

            if (response.getStatus() == 200)
                statsList = gson.fromJson(response.getEntity(String.class), new TypeToken<ArrayList<GlobalStat>>(){}.getType());
            else
                new RuntimeException("Server unavailable");         //da testare

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return statsList;
    }

    public int avgDeliveriesRequest (int t1, int t2){

        int avgDelivs = 0;

        try{

            WebResource webResource = client.resource("http://" + serverHost + ":" + serverPort + "/amministratore/mediaDeliveries/" + t1 + "/" + t2);
            ClientResponse response = webResource.type("application/json").accept("application/json").get(ClientResponse.class);

            if (response.getStatus() == 200)
                avgDelivs = Integer.parseInt(response.getEntity(String.class));
            else
                new RuntimeException("Server unavailable");         //da testare

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return avgDelivs;

    }

    public int avgKMRequest (int t1, int t2){

        int avgKM = 0;

        try{

            WebResource webResource = client.resource("http://" + serverHost + ":" + serverPort + "/amministratore/mediaKM/" + t1 + "/" + t2);
            ClientResponse response = webResource.type("application/json").accept("application/json").get(ClientResponse.class);

            if (response.getStatus() == 200)
                avgKM = Integer.parseInt(response.getEntity(String.class));
            else
                new RuntimeException("Server unavailable");         //da testare

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return avgKM;

    }


}
