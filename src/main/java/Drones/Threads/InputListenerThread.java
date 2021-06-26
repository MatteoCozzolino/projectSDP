package Drones.Threads;

import Drones.DroneController;
import Drones.DroneRESTClient;
import Drones.MasterDroneController;
import Model.Drone;

import java.util.Scanner;

public class InputListenerThread extends Thread{

    Scanner scanner = new Scanner(System.in);

    @Override
    public void run(){

        while(true) {               //temp
            System.out.println("succ is: "+ DroneController.getInstance().getSuccDrone().getId() + " sono il master: "+DroneController.getInstance().getCurrDrone().isMaster());
            System.out.println("\nmasterid: "+DroneController.getInstance().masterID + " size lista droni  "+DroneController.getInstance().getDronesList().size());

            for (Drone d : DroneController.getInstance().getDronesList()){
                System.out.println("\n list drone con id: "+d.getId() + "---- posiz " + d.getPosition_x() + "---" + d.getPosition_y() +" batt " + d.getBatteryLevel());
            }

            if (scanner.nextLine().equals("quit")){
                if (DroneController.getInstance().notDeliverying && DroneController.getInstance().getCurrDrone().isMaster() && !MasterDroneController.getInstance().disableQuit){
                    DroneController.getInstance().droneLogout();
                    break;
                }
                else if (DroneController.getInstance().notDeliverying && !DroneController.getInstance().getCurrDrone().isMaster()){
                    DroneController.getInstance().droneLogout();
                    break;
                }
                else
                    System.out.println("The drone is currently busy and its not possible to exit from the system, try again in a few seconds.");
            }
        }
    }
}
