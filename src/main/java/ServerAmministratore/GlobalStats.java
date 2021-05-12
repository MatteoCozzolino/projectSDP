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
    private Map<Float , GlobalStat> globalStatsList;

    private GlobalStats(){
        globalStatsList = new HashMap<Float,GlobalStat>() {};
    }

    public static GlobalStats getGlobalStatsInstance(){
        if(globalStatsInstance == null)
            globalStatsInstance = new GlobalStats();
        return globalStatsInstance;
    }

    public HashMap<Float,GlobalStat> getGlobalStatsList(int n){

        //creates a temporary list to avoid modification from other parties
        HashMap<Float,GlobalStat> tempStatList = new HashMap<>();
        tempStatList.putAll(globalStatsList);

        ArrayList <Float> timeStamps = new ArrayList<>();
        ArrayList <GlobalStat> stats = new ArrayList<>();

        for (Map.Entry<Float,GlobalStat> element : tempStatList.entrySet()){

            timeStamps.add(element.getKey());
            stats.add(element.getValue());

        }

        Collections.sort(timeStamps,Collections.reverseOrder());
        Collections.sort(stats,Collections.reverseOrder());

        tempStatList.clear();

        for (int i = 0; i < n && i < timeStamps.size() ; i++){
            tempStatList.put(timeStamps.get(i), stats.get(i));
        }

        return tempStatList;
    }

    public int getDelivMed(int t1, int t2) {

        int sum = 0;
        int counter = 0;
        HashMap<Float,GlobalStat> tempStatList = new HashMap<>();
        tempStatList.putAll(globalStatsList);

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
        tempStatList.putAll(globalStatsList);

        for (Map.Entry<Float,GlobalStat> element : tempStatList.entrySet()){

            if (element.getKey() >= t1 && element.getKey() <= t2){
                sum += element.getValue().getAvgKm();
                counter++;
            }
        }

        return sum/counter;
    }


}
