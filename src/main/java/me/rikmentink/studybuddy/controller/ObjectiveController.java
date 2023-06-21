package me.rikmentink.studybuddy.controller;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import me.rikmentink.studybuddy.handler.FileHandler;
import me.rikmentink.studybuddy.model.Objective;
import me.rikmentink.studybuddy.model.Project;

@Path("/objectives")
public class ObjectiveController {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectives() {
        List< Objective> objectives = Objective.getAllObjectives();

        if (objectives.size() == 0) {
            return Response.status(Response.Status.NO_CONTENT).entity(objectives).build();
        }

        return Response.ok(objectives).build();
    }

    @GET
    @Path("/{objectiveId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjective(@PathParam("objectiveId") int objectiveId) {
        Objective objective = Objective.getObjective(objectiveId);

        if (objective == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Objective with ID " + objectiveId + " not found."))
                    .build();
        }

        return Response.ok(objective).build();
    }

    @PUT
    @Path("/{objectiveId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateObjective(@PathParam("objectiveId") int objectiveId, Objective objective) {
        boolean objectiveUpdated = Objective.updateObjective(objectiveId, objective);

        if (getObjective(objectiveId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Objective with ID " + objectiveId + " not found."))
                    .build();
        }

        if (!objectiveUpdated) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new SimpleEntry<>("message", "Unable to update objective."))
                    .build();
        }

        return Response.ok(objective).build();
    }

    @DELETE
    @Path("/{objectiveId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteObjective(@PathParam("objectiveId") int objectiveId) {
        boolean status = Objective.deleteObjective(objectiveId);

        if (getObjective(objectiveId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Objective with ID " + objectiveId + " not found."))
                    .build();
        }

        if (!status) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new SimpleEntry<>("message", "Unable to delete objective."))
                    .build();
        }

        return Response.ok().build();
    }
}
