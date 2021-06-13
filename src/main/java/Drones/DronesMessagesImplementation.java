package Drones;

import Dronazon.DeliveriesGenerator;
import Model.Coordinates;
import Model.Drone;
import Model.DroneStats;
import io.grpc.stub.StreamObserver;
import proto.DronesMessagesGrpc;
import proto.DronesMessagesOuterClass;

public class DronesMessagesImplementation extends DronesMessagesGrpc.DronesMessagesImplBase {

    @Override
    public void greet(DronesMessagesOuterClass.DroneData request, StreamObserver<DronesMessagesOuterClass.DroneData> response) {

        DronesMessagesOuterClass.DroneData master = null;
        Drone newDrone = new Drone(request.getId(), request.getPort(),request.getHost());

        if (request.getId() != DroneController.getInstance().getCurrDrone().getId()){

            //aggiunge il nuovo drone alla lista locale di ogni drone e identifica il nuovo successivo se è cambiato
            DroneController.getInstance().addDroneToList(newDrone);

            if (DroneController.getInstance().getCurrDrone().isMaster())
                master = DronesMessagesOuterClass.DroneData.newBuilder().setId(DroneController.getInstance().getCurrDrone().getId()).setPort(DroneController.getInstance().getCurrDrone().getPort()).setHost(DroneController.getInstance().getCurrDrone().getHost()).build();

        }

        response.onNext(master);
        response.onCompleted();
    }

    @Override
    public void sendStats (DronesMessagesOuterClass.DroneStats request, StreamObserver<DronesMessagesOuterClass.Empty> response) {

        DroneStats newStats = new DroneStats(request.getTimestamp(), new Coordinates(request.getCoordinateX(), request.getCoordinateY()), request.getKm(), request.getAvgPM10(), request.getBattery());
        MasterDroneController.getInstance().addStat(newStats);

        response.onNext(DronesMessagesOuterClass.Empty.newBuilder().build());
        response.onCompleted();
    }

    @Override
    public void election(DronesMessagesOuterClass.DroneData request, StreamObserver<DronesMessagesOuterClass.Empty> response) {

        //rimuove il vecchio master
        DroneController.getInstance().updateList(DroneController.getInstance().getByID(DroneController.getInstance().masterID));

        if (request.getId() > DroneController.getInstance().getCurrDrone().getId()){
            Drone to = new Drone(DroneController.getInstance().getSuccDrone().getId(), DroneController.getInstance().getSuccDrone().getPort(), DroneController.getInstance().getSuccDrone().getHost());
            Drone tokenDrone = new Drone(request.getId(), request.getPort(), request.getHost());
            DroneController.getInstance().electionInProgress = true;
            DroneController.getInstance().election(tokenDrone,to);
            System.out.println("mando elezione a succ");    //temp
        }

        if (request.getId() < DroneController.getInstance().getCurrDrone().getId())

            if (!DroneController.getInstance().electionInProgress){

                Drone to = new Drone(DroneController.getInstance().getSuccDrone().getId(), DroneController.getInstance().getSuccDrone().getPort(), DroneController.getInstance().getSuccDrone().getHost());
                Drone tokenDrone = new Drone(DroneController.getInstance().getCurrDrone().getId(), DroneController.getInstance().getCurrDrone().getPort(), DroneController.getInstance().getCurrDrone().getHost());
                DroneController.getInstance().electionInProgress = true;
                DroneController.getInstance().election(tokenDrone,to);
            }
            else{
                response.onNext(DronesMessagesOuterClass.Empty.newBuilder().build());
                response.onCompleted();
            }


        if (request.getId() == DroneController.getInstance().getCurrDrone().getId()){

            //currentDrone è il master
            DroneController.getInstance().getCurrDrone().setMaster(true);
            DroneController.getInstance().setMasterInfos(DroneController.getInstance().getCurrDrone().getId(), DroneController.getInstance().getCurrDrone().getPort(), DroneController.getInstance().getCurrDrone().getHost());
            DroneController.getInstance().electionInProgress = false;

            //notifica gli altri droni
            Drone to = new Drone(DroneController.getInstance().getSuccDrone().getId(), DroneController.getInstance().getSuccDrone().getPort(), DroneController.getInstance().getSuccDrone().getHost());
            Drone tokenDrone = new Drone(DroneController.getInstance().getCurrDrone().getId(), DroneController.getInstance().getCurrDrone().getPort(), DroneController.getInstance().getCurrDrone().getHost());
            DroneController.getInstance().elected(tokenDrone,to);


            System.out.println("sono il master eletto"); //temp
        }

        response.onNext(DronesMessagesOuterClass.Empty.newBuilder().build());
        response.onCompleted();
    }

    @Override
    public void elected(DronesMessagesOuterClass.DroneData request, StreamObserver<DronesMessagesOuterClass.Empty> response) {

        DroneController.getInstance().electionInProgress = false;

        if (!(DroneController.getInstance().getCurrDrone().getId() == request.getId())){

            DroneController.getInstance().setMasterInfos(request.getId(), request.getPort(), request.getHost());

            Drone to = new Drone(DroneController.getInstance().getSuccDrone().getId(), DroneController.getInstance().getSuccDrone().getPort(), DroneController.getInstance().getSuccDrone().getHost());
            Drone tokenDrone = new Drone(request.getId(), request.getPort(), request.getHost());

            if (to.getId() == tokenDrone.getId())
                DroneController.getInstance().getSuccDrone().setMaster(true);

            DroneController.getInstance().elected(tokenDrone,to);
        }

        if (DroneController.getInstance().getCurrDrone().getId() == request.getId())
            MasterDroneController.getInstance().initialize();

        response.onNext(DronesMessagesOuterClass.Empty.newBuilder().build());
        response.onCompleted();
    }

    @Override
    public void assignDelivery(DronesMessagesOuterClass.DeliveryInfo request, StreamObserver<DronesMessagesOuterClass.Empty> response){

        DroneController.getInstance().setDelivery(new DeliveriesGenerator(request.getDeliveryID(), new Coordinates(request.getPickUpX(), request.getPickUpY()), new Coordinates(request.getDeliveryX(), request.getDeliveryY())));
//System.out.println("done "+DroneController.getInstance().currentDelivery.getDeliveryID());
        response.onNext(DronesMessagesOuterClass.Empty.newBuilder().build());
        response.onCompleted();
    }

    @Override
    public void getDroneInformations (DronesMessagesOuterClass.Empty request, StreamObserver<DronesMessagesOuterClass.DroneInfo> response){

        Drone tempDrone = DroneController.getInstance().getCurrDrone();
        DronesMessagesOuterClass.DroneInfo reply = DronesMessagesOuterClass.DroneInfo.newBuilder().setId(tempDrone.getId()).setPort(tempDrone.getPort()).setHost(tempDrone.getHost()).setCoordinateX(tempDrone.getPosition_x()).setCoordinateY(tempDrone.getPosition_y()).setBattery(tempDrone.getBatteryLevel()).build();

        response.onNext(reply);
        response.onCompleted();
    }

    @Override
    public void sendDroneInfoToMaster(DronesMessagesOuterClass.DroneInfo request,StreamObserver<DronesMessagesOuterClass.Empty> response)  {

        if (DroneController.getInstance().getCurrDrone().isMaster()){

            Drone tempDrone = new Drone(request.getId(), request.getPort(), request.getHost());
            tempDrone.setPosition(request.getCoordinateX(), request.getCoordinateY());
            tempDrone.setBatteryLevel(request.getBattery());

            DroneController.getInstance().getDronesList().set(DroneController.getInstance().getDronesList().indexOf(DroneController.getInstance().getByID(request.getId())),tempDrone);
        }

        response.onNext(DronesMessagesOuterClass.Empty.newBuilder().build());
        response.onCompleted();
    }

    @Override
    public void alive (DronesMessagesOuterClass.Empty request, StreamObserver<DronesMessagesOuterClass.Empty> response){

        response.onNext(DronesMessagesOuterClass.Empty.newBuilder().build());
        response.onCompleted();
    }

    @Override
    public void remove (DronesMessagesOuterClass.DroneData request, StreamObserver<DronesMessagesOuterClass.Empty> response){

        Drone toRemove = new Drone();

        for (Drone d : DroneController.getInstance().getDronesList())
            if (d.getId() == request.getId())
                toRemove = d;

        DroneController.getInstance().updateList(toRemove);

        response.onNext(DronesMessagesOuterClass.Empty.newBuilder().build());
        response.onCompleted();
    }

}
