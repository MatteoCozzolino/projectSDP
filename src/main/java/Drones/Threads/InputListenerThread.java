package Drones.Threads;

import Drones.DroneController;

import java.util.Scanner;

public class InputListenerThread extends Thread{

    Scanner scanner = new Scanner(System.in);

    @Override
    public void run(){

        while(true) {
            if (scanner.nextLine().equals("quit")){
                 if (DroneController.getInstance().notDelivering && !DroneController.getInstance().electionInProgress){
                    DroneController.getInstance().droneLogout();
                    break;
                }
                else
                    System.out.println("The drone is currently busy and its not possible to exit from the system, try again in a few seconds.");
            }
        }
    }
}
