package Drones;

import Drones.Threads.InputListenerThread;
import Model.Drone;

import java.util.ArrayList;
import java.util.Scanner;

public class DroneStarter {

    private int id;
    private String host;
    private int serverPort;
    private int port;
    private Drone drone;
    private Scanner scanner;
    private InputListenerThread inputListenerThread;
    private ArrayList<Drone> list;


    public DroneStarter(String host, int serverPort ) {

        scanner = new Scanner(System.in);

        this.host = host;
        this.serverPort = serverPort;

        System.out.println("Type drone ID...");
        this.id = scanner.nextInt();

        port = (int) Math.floor(Math.random()*40263)+8888;

        drone = new Drone(this.id, this.port, host);

        try{
            DroneRESTClient.getInstance().initialize(this.host, this.serverPort , drone);

            list = DroneRESTClient.getInstance().addRequest();

            for (Drone d : list)
                if (d.getId() == id)
                    drone.setPosition(d.getPosition_x(),d.getPosition_y());

            DroneController.getInstance().initialize(list , drone);

            startThreads();

        }
        catch (Exception e){
            e.printStackTrace();}
    }

    private void startThreads() {

        inputListenerThread = new InputListenerThread();
        inputListenerThread.start();

    }

    public static void main(String args[]) {
        new DroneStarter("localhost",1337);
    }

}
