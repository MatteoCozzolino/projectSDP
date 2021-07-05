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
    private final ArrayList <GlobalStat> globalStatsList;

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
        return true;
    }

    public ArrayList<GlobalStat> getGlobalStatsList(int n){

        //creates a temporary list to avoid modification from other parties
        ArrayList<GlobalStat> tempStatList = new ArrayList<>(globalStatsList);
        tempStatList.remove(tempStatList.get(0));

        ArrayList<GlobalStat> orderedList = new ArrayList<>();

        for (int i = tempStatList.size(); i != 0 ; i--) {
            if (tempStatList.get(i - 1).getTimestamp() == 0)
                tempStatList.remove(i - 1);
            else
                orderedList.add(tempStatList.get(i - 1));
        }
        ArrayList<GlobalStat> requiredList = new ArrayList<>();
        for (int i = 0; i < n && i < orderedList.size() ; i++){
            requiredList.add(orderedList.get(i));
        }

        return requiredList;
    }

    public float getDelivMed(float t1, float t2) {

        float sum = 0;
        int counter = 0;
        HashMap<Float,GlobalStat> tempStatList = new HashMap<>();

        for (GlobalStat globalStat : globalStatsList)
            if (globalStat.getTimestamp() != 0 && globalStat.getAvgPM10() != 0)
                tempStatList.put(globalStat.getTimestamp(), globalStat);

        for (Map.Entry<Float,GlobalStat> element : tempStatList.entrySet()){

            if (element.getKey() >= t1 && element.getKey() <= t2){
                sum += element.getValue().getAvgNumberDeliveries();
                counter++;
            }
        }
        return sum/counter;
    }

    public float getKMMed(float t1, float t2) {

        float sum = 0;
        int counter = 0;
        HashMap<Float,GlobalStat> tempStatList = new HashMap<>();

        for (GlobalStat globalStat : globalStatsList)
            if (globalStat.getTimestamp() != 0  && globalStat.getAvgPM10() != 0.0)
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
