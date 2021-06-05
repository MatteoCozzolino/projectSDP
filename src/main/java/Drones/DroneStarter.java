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
        ArrayList<Drone> list;


    public DroneStarter(String host, int serverPort ) {

        scanner = new Scanner(System.in);

        this.host = host;
        this.serverPort = serverPort;

        System.out.println("Type drone ID...");
        this.id = scanner.nextInt();
        System.out.println("Type drone port...");
        this.port = scanner.nextInt();
        drone = new Drone(this.id, this.port, host);

        try{
            DroneRESTClient.getInstance().initialize(host, serverPort , drone);

            list = DroneRESTClient.getInstance().addRequest();

            DroneController.getInstance().initialize(list , drone);

            startThreads();

            DroneController.getInstance().test();
            //DroneController.getInstance().droneLifecycle();

            //mi accorgo che drone è uscito quando:
            // - master gli assegna una consegna e non risponde (devo aggiornare succ e lista di tutti e assegnare la  consegna a un altro)
            // - durante elezione il mio succ non esiste (aggiorno il mio succ e la lista di tutti)
            // - quando un drone entra e fa greeting uno dei droni non risponde (aggiorno succ e lista di tutti)

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
