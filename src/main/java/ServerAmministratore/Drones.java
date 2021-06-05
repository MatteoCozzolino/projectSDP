package ServerAmministratore;

import Model.Coordinates;
import Model.Drone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Drones {

    //Making the drone list a singleton
    private static Drones dronesInstance;

    @XmlElement(name="drone")
    private List<Drone> dronesList;

    private Drones(){
        dronesList = new ArrayList<>();
    }

    public static Drones getDronesInstance(){
        if(dronesInstance == null)
            dronesInstance = new Drones();
        return dronesInstance;
    }

    public List<Drone> getDronesList(){
        ArrayList<Drone> droneList = new ArrayList<>();

        droneList.addAll(this.dronesList);

        return new ArrayList<>(droneList);
    }

    public synchronized boolean addDrone(Drone drone){

        for(Drone d : dronesList){
            if(d.getId() == drone.getId()) {
                return false;
            }
        }
        setDroneStartingPosition(drone);
        dronesList.add(drone);

        return true;
    }

    public synchronized boolean deleteDrone(Drone drone){

        for (Drone d : dronesList){
            if (d.getId() == drone.getId()){
                dronesList.remove(d);
                return true;
            }
        }
        return false;
    }

    public void setDroneStartingPosition (Drone drone){

        int x = (int) Math.floor(Math.random()*11);
        int y = (int) Math.floor(Math.random()*11);

        drone.setPosition(x,y);

    }
}