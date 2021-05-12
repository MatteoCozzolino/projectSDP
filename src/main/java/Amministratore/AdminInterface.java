package Amministratore;

import Model.Drone;
import Model.GlobalStat;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminInterface {

    private final String serverHost = "localhost";
    private final int serverPort = 1337;

    public AdminInterface () {

        try {
            AdminRESTClient.getInstance().initialize(serverHost , serverPort);
            launchMenu();                   //testare tutti i metodi con valori appropriati

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void launchMenu() {

        Scanner choiceListener = new Scanner(System.in);
        int choice;

        while (true){

            System.out.println("\nPlease pick the desired request:\n1- Get the list of drones in the network\n2- Get the last n global statistics of the smart city\n3- Get the" +
                    " average number of deliveries in the smart city between two timestamps\n4- Get the average number of km done by drones in the smart city between " +
                    "two timestamps\n5- Close\n\n");
            choice = choiceListener.nextInt();

            switch (choice){
                case 1 :
                    ArrayList<Drone> drones = AdminRESTClient.getInstance().droneListRequest();
                    System.out.println(drones.toString());          //da controllare
                    break;
                case 2 :
                    System.out.println("How many of the most recent global statistics do you wish to have?\n");
                    int n = choiceListener.nextInt();
                    ArrayList<GlobalStat> globalStats = AdminRESTClient.getInstance().lastStatsRequest(n);
                    System.out.println(globalStats.toString());
                    break;
                case 3 :
                    System.out.println("Between which timestamps do you want the average?\n");
                    int t1 = choiceListener.nextInt();              //controllare
                    int t2 = choiceListener.nextInt();
                    int avgDeliv = AdminRESTClient.getInstance().avgDeliveriesRequest(t1,t2);
                    System.out.println("\nThe average number of deliveries done by drones is: " + avgDeliv + "\n");
                    break;
                case 4 :
                    System.out.println("Between which timestamps do you want the average?\n");
                    int time1 = choiceListener.nextInt();              //controllare
                    int time2 = choiceListener.nextInt();
                    int avgKM = AdminRESTClient.getInstance().avgKMRequest(time1,time2);
                    System.out.println("\nThe average Km done by drones is: " + avgKM + "\n");
                    break;
                case 5 :
                    System.out.println("Thanks for using our services, the client will shut down!");
                    return;
                default:
                    break;
            }
        }
    }

    public static void main(String args[]) {
        new AdminInterface();
    }


}
