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

import me.rikmentink.studybuddy.model.Objective;

@Path("/objectives")
public class ObjectiveController {

    /**
     * This function retrieves a list of objectives and returns them as a JSON
     * response.
     * 
     * @return A Response object containing all objectives, or status 201 (Created)
     *         if none
     *         are found.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectives() {
        List<Objective> objectives = Objective.getAllObjectives();

        if (objectives.size() == 0) {
            return Response.status(Response.Status.NO_CONTENT).entity(objectives).build();
        }

        return Response.ok(objectives).build();
    }

    /**
     * The function retrieves an Objective object based on the provided objectiveId
     * and returns it as a JSON response.
     * 
     * @param objectiveId The ID of the objective that we want to
     *                    retrieve. It is passed as a path parameter in the URL.
     * @return A Response object containing the objective found, or status 404 (Not
     *         Found) whether no objective was found with this ID.
     */
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

    /**
     * Updates an objective with the given ID and returns a response indicating the
     * success or failure of the update.
     * 
     * @param objectiveId The ID of the
     *                    objective that needs to be updated.
     * @param objective   The updated objective that will be used to update the
     *                    existing objective with the specified
     *                    objectiveId.
     * @return A Response object containing the updated objective object, or a
     *         status 404 whether no objective was found, or a status 417 if the
     *         objective couldn't be updated.
     */
    @PUT
    @Path("/{objectiveId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateObjective(@PathParam("objectiveId") int objectiveId, Objective objective) {
        if (getObjective(objectiveId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Objective with ID " + objectiveId + " not found."))
                    .build();
        }

        boolean objectiveUpdated = Objective.updateObjective(objectiveId, objective);

        if (!objectiveUpdated) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new SimpleEntry<>("message", "Unable to update objective."))
                    .build();
        }

        return Response.ok(objective).build();
    }

    /**
     * Deletes an objective with a given ID and returns an appropriate response
     * based on
     * the success or failure of the deletion.
     * 
     * @param objectiveId The unique identifier of the objective that needs to be
     *                    deleted.
     * @return A Response object containing a status 200 (OK) if the objective was
     *         deleted, or a 404 (Not Found) if no objective was found with this ID.
     */
    @DELETE
    @Path("/{objectiveId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteObjective(@PathParam("objectiveId") int objectiveId) {
        if (getObjective(objectiveId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Objective with ID " + objectiveId + " not found."))
                    .build();
        }

        boolean status = Objective.deleteObjective(objectiveId);

        if (!status) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new SimpleEntry<>("message", "Unable to delete objective."))
                    .build();
        }

        return Response.ok().build();
    }
}
