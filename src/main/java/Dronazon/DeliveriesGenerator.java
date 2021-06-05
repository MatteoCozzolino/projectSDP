package Dronazon;

import Model.Coordinates;

public class DeliveriesGenerator {

    private Coordinates pickUpPoint;
    private Coordinates deliveryPoint;
    private int deliveryID;

    public DeliveriesGenerator(int id, Coordinates pickUpPoint, Coordinates deliveryPoint){

        deliveryID = id;
        this.pickUpPoint = pickUpPoint;
        this.deliveryPoint = deliveryPoint;
    }

    public DeliveriesGenerator(){}

    public Coordinates generateCoordinates(){

        int x = (int) Math.floor(Math.random()*11);
        int y = (int) Math.floor(Math.random()*11);

        return new Coordinates(x,y);
    }

    public void generateDelivery (int id){

        this.pickUpPoint = generateCoordinates();
        this.deliveryPoint = generateCoordinates();
        deliveryID = id;

    }


    public Coordinates getPickUpPoint() {
        return pickUpPoint;
    }

    public Coordinates getDeliveryPoint() {
        return deliveryPoint;
    }
}
