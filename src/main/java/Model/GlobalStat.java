package Model;

//statistiche che il drone master manda al server

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GlobalStat {

    private float timestamp;
    private float avgNumberDeliveries;
    private float avgKm;
    private float avgPM10;

    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }

    public void setAvgNumberDeliveries(float avgNumberDeliveries) {
        this.avgNumberDeliveries = avgNumberDeliveries;
    }

    public void setAvgKm(float avgKm) {
        this.avgKm = avgKm;
    }

    public void setAvgPM10(float avgPM10) {
        this.avgPM10 = avgPM10;
    }

    public void setAvgResidualBatteries(float avgResidualBatteries) {
        this.avgResidualBatteries = avgResidualBatteries;
    }

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
