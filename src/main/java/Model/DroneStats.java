package Model;

public class DroneStats {

    private float timestamp;
    private Coordinates newCoordinates;
    private float kmCovered;
    private float avgPM10;
    private int newBatteryLevel;

    public DroneStats(){}

    public DroneStats(float timestamp, Coordinates newCoordinates, float kmCovered, float avgPM10, int newBatteryLevel) {
        this.timestamp = timestamp;
        this.newCoordinates = newCoordinates;
        this.kmCovered = kmCovered;
        this.avgPM10 = avgPM10;
        this.newBatteryLevel = newBatteryLevel;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public Coordinates getNewCoordinates() {
        return newCoordinates;
    }

    public int getNewBatteryLevel() {
        return newBatteryLevel;
    }

    public float getKmCovered() {
        return kmCovered;
    }

    public float getAvgPM10() {
        return avgPM10;
    }
}
