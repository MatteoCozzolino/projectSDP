package Model;

//statistiche che il drone master manda al server

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GlobalStat {

    private float timestamp;
    private float avgNumberDeliveries;
    private float avgKm;
    private float avgPM10;
    private float avgResidualBatteries;

    public GlobalStat(){

    }

    public GlobalStat(float timestamp, float avgNumberDeliveries, float avgKm, float avgPM10, float avgResidualBatteries) {
        this.timestamp = timestamp;
        this.avgNumberDeliveries = avgNumberDeliveries;
        this.avgKm = avgKm;
        this.avgPM10 = avgPM10;
        this.avgResidualBatteries = avgResidualBatteries;
    }


    public float getTimestamp() {
        return timestamp;
    }

    public float getAvgNumberDeliveries() {
        return avgNumberDeliveries;
    }

    public float getAvgKm() {
        return avgKm;
    }

    public float getAvgPM10() {
        return avgPM10;
    }

    public float getAvgResidualBatteries() {
        return avgResidualBatteries;
    }
}
