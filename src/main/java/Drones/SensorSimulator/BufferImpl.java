package Drones.SensorSimulator;

import Drones.DroneController;

import java.util.ArrayList;
import java.util.List;

public class BufferImpl implements Buffer{

    private List<Measurement> buffer;
    private double average;
    private int counter;

    public BufferImpl (){

        buffer = new ArrayList<>(8);
        counter = 0;
        average = 0;
    }

    @Override
    public void addMeasurement(Measurement m) {

        if (counter < 8)
            buffer.add(m);

        counter++;

        if (counter >= 8)
            buffer = readAllAndClean();
    }

    @Override
    public List<Measurement> readAllAndClean() {

        List<Measurement> measures = buffer;

        double sum = 0;
        for (Measurement m : measures)
            sum += m.getValue();

        average = sum/8;

        DroneController.getInstance().addPM10value(average);

        measures.subList(0, 4).clear();
        counter = 4;

        return measures;
    }
}
