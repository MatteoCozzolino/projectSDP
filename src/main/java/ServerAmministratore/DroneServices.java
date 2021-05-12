package ServerAmministratore;

import Model.Drone;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("drones")
public class DroneServices {

    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addDrone(Drone d){

        if (Drones.getDronesInstance().addDrone(d)){
            System.out.println("Drone " + d.getId() + " joined the network!");
            return Response.ok(Drones.getDronesInstance().getDronesList()).build();
        }
        else{
            System.out.println("Drone " + d.getId() + " can't join, another Drone with the same ID already exists in the network!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


    }

    @Path("delete")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response deleteDrone(Drone d){

        if (Drones.getDronesInstance().deleteDrone(d)){
            System.out.println("Drone " + d.getId() + " has been removed from the network!");
            return Response.ok().build();
        }
        else{
            System.out.println("There is no Drone with id " + d.getId() + " in the network.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


    }


}
