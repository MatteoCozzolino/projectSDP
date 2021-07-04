package ServerAmministratore;

import Model.GlobalStat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("amministratore")
public class GlobalStatsServices {

    @Path("dronesList")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response dronesList (){

        return Response.ok(Drones.getDronesInstance().getDronesList()).build();

    }

    @Path("lastStats/{numberOfValues}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getLastStats (@PathParam("numberOfValues") int n) {

        ArrayList<GlobalStat> stats = GlobalStats.getGlobalStatsInstance().getGlobalStatsList(n);

        if (stats != null )
            return Response.ok(stats).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @Path("mediaDeliveries/{t1}/{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getDelivMed (@PathParam("t1") float t1, @PathParam("t2") float t2) {

        float med = GlobalStats.getGlobalStatsInstance().getDelivMed(t1,t2);
        return Response.ok(med).build();

    }

    @Path("mediaKM/{t1}/{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getKMMed (@PathParam("t1") float t1, @PathParam("t2") float t2) {

        float med = GlobalStats.getGlobalStatsInstance().getKMMed(t1,t2);
        return Response.ok(med).build();

    }

}
