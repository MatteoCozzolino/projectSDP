package Drones;

import Model.Drone;

import java.util.Scanner;

public class DroneStarter {

    private int id;
    private String serverHost;
    private int serverPort;
    private int port;
    private Drone drone;


    public DroneStarter( String serverHost, int serverPort ) {

        Scanner scanner = new Scanner(System.in);

        this.serverHost = serverHost;
        this.serverPort = serverPort;

        System.out.println("Type drone ID...");
        this.id = scanner.nextInt();
        System.out.println("Type drone port...");
        this.port = scanner.nextInt();
        drone = new Drone(this.id, this.port, serverHost);

        try{
            DronesRESTClient.getInstance().initialize(serverHost , serverPort , drone);

            Scanner test = new Scanner(System.in);
            int choice = test.nextInt();

            if (choice == 1)
                DronesRESTClient.getInstance().addRequest();
            if (choice == 2)
                DronesRESTClient.getInstance().deleteRequest(drone);

            //prints the recieved message from server?
            System.out.println();
        }
        catch (Exception e){                //da gestire se il drone non è nella rete
            e.printStackTrace();}

    }

    public static void main(String args[]) {
        new DroneStarter("localhost",1337);
    }

}
