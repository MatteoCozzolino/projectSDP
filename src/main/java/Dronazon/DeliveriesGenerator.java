package Dronazon;

import Model.Coordinates;

public class DeliveriesGenerator {

    Coordinates pickUpPoint;
    Coordinates deliveryPoint;

    public Coordinates generateCoordinates(){

        int x = (int) Math.floor(Math.random()*11);
        int y = (int) Math.floor(Math.random()*11);

        return new Coordinates(x,y);
    }

    public void generateDelivery (){

        this.pickUpPoint = generateCoordinates();
        this.deliveryPoint = generateCoordinates();

    }


}
