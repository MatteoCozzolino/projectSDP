package ServerAmministratore;

import Model.GlobalStat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GlobalStats {

    private static GlobalStats globalStatsInstance;

    @XmlElement(name="globalStats")
    private ArrayList <GlobalStat> globalStatsList;

    private GlobalStats(){
        globalStatsList = new ArrayList<GlobalStat>() {};
    }

    public static GlobalStats getGlobalStatsInstance(){
        if(globalStatsInstance == null)
            globalStatsInstance = new GlobalStats();
        return globalStatsInstance;
    }

    public boolean addGlobalStatValue (GlobalStat stats){

        globalStatsList.add(stats);

        for (GlobalStat a :globalStatsList)         //temp
            System.out.println(a.getAvgKm()+" <-global stats");

        return true;
    }

    public ArrayList<GlobalStat> getGlobalStatsList(int n){

        //creates a temporary list to avoid modification from other parties
        ArrayList<GlobalStat> tempStatList = new ArrayList<>();
        tempStatList.addAll(globalStatsList);

        Collections.sort(tempStatList,Collections.reverseOrder());

        ArrayList<GlobalStat> requiredList = new ArrayList<>();
        for (int i = 0; i < n && i < tempStatList.size() ; i++){
            requiredList.add(tempStatList.get(i));
        }

        return requiredList;
    }

    public int getDelivMed(int t1, int t2) {

        int sum = 0;
        int counter = 0;
        HashMap<Float,GlobalStat> tempStatList = new HashMap<>();

        for (GlobalStat globalStat : globalStatsList)
            tempStatList.put(globalStat.getTimestamp(), globalStat);

        for (Map.Entry<Float,GlobalStat> element : tempStatList.entrySet()){

            if (element.getKey() >= t1 && element.getKey() <= t2){
                sum += element.getValue().getAvgNumberDeliveries();
                counter++;
            }
        }

        return sum/counter;
    }

    public int getKMMed(int t1, int t2) {

        int sum = 0;
        int counter = 0;
        HashMap<Float,GlobalStat> tempStatList = new HashMap<>();

        for (GlobalStat globalStat : globalStatsList)
            tempStatList.put(globalStat.getTimestamp(), globalStat);

        for (Map.Entry<Float,GlobalStat> element : tempStatList.entrySet()){

            if (element.getKey() >= t1 && element.getKey() <= t2){
                sum += element.getValue().getAvgKm();
                counter++;
            }
        }

        return sum/counter;
    }


}
