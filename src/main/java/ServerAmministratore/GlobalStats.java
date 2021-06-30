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
            System.out.println(a.getAvgKm()+" <-global stats" + a.getTimestamp());

        return true;
    }

    public ArrayList<GlobalStat> getGlobalStatsList(int n){

        //creates a temporary list to avoid modification from other parties
        ArrayList<GlobalStat> tempStatList = new ArrayList<>(globalStatsList);
        tempStatList.remove(tempStatList.get(0));

        ArrayList<GlobalStat> orderedList = new ArrayList<>();

        for (int i = tempStatList.size(); i != 0 ; i--)
            orderedList.add(tempStatList.get(i - 1));

        ArrayList<GlobalStat> requiredList = new ArrayList<>();
        for (int i = 0; i < n && i < orderedList.size() ; i++){
            requiredList.add(orderedList.get(i));
        }

        return requiredList;
    }

    public float getDelivMed(int t1, int t2) {

        float sum = 0;
        int counter = 0;
        HashMap<Float,GlobalStat> tempStatList = new HashMap<>();

        for (GlobalStat globalStat : globalStatsList)
            tempStatList.put(globalStat.getTimestamp(), globalStat);

        tempStatList.remove(globalStatsList.get(0).getTimestamp());

        for (Map.Entry<Float,GlobalStat> element : tempStatList.entrySet()){

            if (element.getKey() >= (float) t1 && element.getKey() <= (float) t2){
                sum += element.getValue().getAvgNumberDeliveries();
                counter++;
            }
        }
        return sum/counter;
    }

    public float getKMMed(int t1, int t2) {

        float sum = 0;
        int counter = 0;
        HashMap<Float,GlobalStat> tempStatList = new HashMap<>();

        for (GlobalStat globalStat : globalStatsList)
            tempStatList.put(globalStat.getTimestamp(), globalStat);

        tempStatList.remove(globalStatsList.get(0).getTimestamp());

        for (Map.Entry<Float,GlobalStat> element : tempStatList.entrySet()){

            if (element.getKey() >= t1 && element.getKey() <= t2){
                sum += element.getValue().getAvgKm();
                counter++;
            }
        }

        return sum/counter;
    }


}
