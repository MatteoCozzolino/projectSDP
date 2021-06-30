package Drones.Threads;

import Drones.DroneController;
import Model.Drone;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.DronesMessagesGrpc;
import proto.DronesMessagesOuterClass;

public class PingThread extends Thread{

    @Override
    public void run(){

        Drone succDrone = DroneController.getInstance().getSuccDrone();

        if (DroneController.getInstance().masterID == 0)
            DroneController.getInstance().election(DroneController.getInstance().getCurrDrone(), succDrone);

        while (true) {

            if (!pingSucc(succDrone)) {

                if (succDrone.isMaster()) {

                    DroneController.getInstance().updateList(succDrone);
                    DroneController.getInstance().election(DroneController.getInstance().getCurrDrone(), DroneController.getInstance().getSuccDrone());
                }
                else{

                    DroneController.getInstance().updateList(succDrone);

                    for (Drone d : DroneController.getInstance().getDronesList()) {
                        if (d != DroneController.getInstance().getCurrDrone()) {
                            Drone offDrone = succDrone;
                            Thread removeFromPeerLists = new Thread(() -> removeDrone(d, offDrone));
                            removeFromPeerLists.start();
                        }
                    }
                }
            }

            //Il Thread aspetta 10 secondi prima di inviare un altro ping
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            succDrone= DroneController.getInstance().getSuccDrone();

        }

    }

    private boolean pingSucc(Drone succDrone) {

        ManagedChannel channel = null;
        try {
            channel = ManagedChannelBuilder.forTarget(succDrone.getHost() + ":" + succDrone.getPort()).usePlaintext().build();
            DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
            DronesMessagesOuterClass.Empty pingMessage = DronesMessagesOuterClass.Empty.newBuilder().build();

            DronesMessagesOuterClass.Empty reply = stub.alive(pingMessage);

            channel.shutdownNow();
            return true;

        }catch (Throwable t){
            channel.shutdownNow();
            return false;
        }
    }

    private void removeDrone(Drone targetDrone, Drone offDrone) {

        if (targetDrone.getPort() != offDrone.getPort()) {
            ManagedChannel channel = ManagedChannelBuilder.forTarget(targetDrone.getHost() + ":" + targetDrone.getPort()).usePlaintext().build();
            DronesMessagesGrpc.DronesMessagesBlockingStub stub = DronesMessagesGrpc.newBlockingStub(channel);
            DronesMessagesOuterClass.DroneData data = DronesMessagesOuterClass.DroneData.newBuilder().setId(offDrone.getId()).setHost(offDrone.getHost()).setPort(offDrone.getPort()).build();

            DronesMessagesOuterClass.Empty reply = stub.remove(data);

            channel.shutdownNow();
        }
    }

}