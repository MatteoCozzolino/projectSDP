package Model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Drone {

    private int id;
    private int port;
    private String host;
    private int batteryLevel = 100;
    private int position_x;
    private int position_y;
    private boolean master = false;
    public boolean deliveryInProgress = false;

    public Drone(){}

    public Drone(int id, int port, String host){
        this.id=id;
        this.port=port;
        this.host = host;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public void changeBatteryLevel () {

        batteryLevel = batteryLevel - 10;

    }

    public void setPosition(int x, int y){
        position_x = x;
        position_y = y;
    }

    public Coordinates getCoordinates(){
        return new Coordinates(getPosition_x(),getPosition_y());
    }

    public int getPosition_x(){
        return position_x;
    }

    public int getPosition_y() {
        return position_y;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }
}
