package Drones.Threads;

import Drones.DroneController;
import Drones.DroneRESTClient;
import Drones.MasterDroneController;

public class StatsThread extends Thread{

    private boolean flag = true;

    @Override
    public void run() {
        while (flag) {

            System.out.println("\nIl drone ha effettuato " + DroneController.getInstance().totalNumberDeliveries + " consegne, percorso " + DroneController.getInstance().totalKm + " km ed ha il " + DroneController.getInstance().getCurrDrone().getBatteryLevel() + "% di batteria residua.");

            if (DroneController.getInstance().getCurrDrone().isMaster())
                DroneRESTClient.getInstance().sendGlobalStats(MasterDroneController.getInstance().calculateGlobalStats());

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread (){
        flag = false;
    }


}
