package Amministratore;

import Model.Drone;
import Model.GlobalStat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminInterface {

    private final String serverHost = "localhost";
    private final int serverPort = 1337;

    public AdminInterface () {

        try {
            AdminRESTClient.getInstance().initialize(serverHost , serverPort);
            launchMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void launchMenu() {

        Scanner choiceListener = new Scanner(System.in);
        int choice;

        while (true){

            System.out.println("\nScegli l'opzione desiderata:\n1- Mostra la lista dei droni nella smart city\n2- Mostra le ultime n statistiche globali\n3- Mostra" +
                    " la media delle consegne effettuate tra due timestamp\n4- Mostra la media dei km percorsi dai droni tra due timestamp\n5- Chiudi l'admin interface\n\n");
            choice = choiceListener.nextInt();

            switch (choice){
                case 1 :
                    ArrayList<Drone> drones = AdminRESTClient.getInstance().droneListRequest();
                    System.out.println("Mostro gli ID dei droni nella smart city.");
                    for (Drone drone : drones)
                        System.out.println(drone.getId());
                    break;
                case 2 :
                    System.out.println("Quante tra le ultime statistiche globali devono essere mostrate?\n");
                    int n = choiceListener.nextInt();
                    ArrayList<GlobalStat> globalStats = AdminRESTClient.getInstance().lastStatsRequest(n);
                    System.out.println("Mostro la ultime " + n + " statistiche globali.");
                    for (GlobalStat stat : globalStats)
                        System.out.println("\nMedie al timestamp " + stat.getTimestamp() + ":\nconsegne - " + stat.getAvgNumberDeliveries() + "\nkm - " + stat.getAvgKm() + "\nPM10 - " + stat.getAvgPM10() + "\nbatteria - " + stat.getAvgResidualBatteries());
                    break;
                case 3 :
                    String ts = new Timestamp(System.currentTimeMillis()).toString().substring(14);
                    float tsInSeconds = (Float.parseFloat(ts.substring(0,2))*60) + Float.parseFloat(ts.substring(3));
                    System.out.println("Tra quali timestamp vuoi la media? Il timestamp corrente è: " + tsInSeconds);
                    float t1 = choiceListener.nextFloat();
                    float t2 = choiceListener.nextFloat();
                    float avgDeliv = AdminRESTClient.getInstance().avgDeliveriesRequest(t1,t2);
                    System.out.println("\nLa media delle consegne effettuate dai droni è: " + avgDeliv);
                    break;
                case 4 :
                    String tsKM = new Timestamp(System.currentTimeMillis()).toString().substring(14);
                    float tsInSecondsKM = (Float.parseFloat(tsKM.substring(0,2))*60) + Float.parseFloat(tsKM.substring(3));
                    System.out.println("Tra quali timestamp vuoi la media? Il timestamp corrente è: " + tsInSecondsKM);
                    float time1 = choiceListener.nextInt();
                    float time2 = choiceListener.nextInt();
                    float avgKM = AdminRESTClient.getInstance().avgKMRequest(time1,time2);
                    System.out.println("\nLa media dei km percorsi dai droni è: " + avgKM);
                    break;
                case 5 :
                    System.out.println("Grazie per aver usato i nostri servizi, il client si spegnerà!");
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
