package Model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Drone {

    private int id;
    private int port;
    private String serverHost;
    private int batteryLevel = 100;
    private int position_x;
    private int position_y;

    public Drone(){}

    public Drone(int id, int port, String serverHost){
        this.id=id;
        this.port=port;
        this.serverHost = serverHost;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public String getServerHost() {
        return serverHost;
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

    public void setPosition(Coordinates position){
        position_x = position.getX();
        position_y = position.getY();
    }

    public int getPosition_x(){
        return position_x;
    }

    public int getPosition_y() {
        return position_y;
    }
}
