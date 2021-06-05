package Drones.Threads;

import Drones.DroneController;
import Drones.DronesMessagesImplementation;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.concurrent.TimeUnit;

public class PeerListenerThread extends Thread{

    private Server grpcServer;

    @Override
    public void run(){

        try {
            grpcServer = ServerBuilder.forPort(DroneController.getInstance().getCurrDrone().getPort()).addService(new DronesMessagesImplementation()).build();
            grpcServer.start();
//sincronization? perchè?
        synchronized (this) {
            notify();
        }
        grpcServer.awaitTermination(1000, TimeUnit.MILLISECONDS);       //serve?
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){     //chiamare quando si spegne un drone
        grpcServer.shutdown();
        synchronized (this){
            notify();
        }
        System.out.println("\nGrpc server shutted down.");
    }
}