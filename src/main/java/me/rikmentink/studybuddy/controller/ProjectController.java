package me.rikmentink.studybuddy.controller;

import java.net.URI;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import me.rikmentink.studybuddy.model.Objective;
import me.rikmentink.studybuddy.model.Project;

@Path("/projects")
public class ProjectController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjects() {
        List<Project> projects = Project.getAllProjects();

        if (projects.size() == 0) {
            return Response.status(Response.Status.NO_CONTENT).entity(projects).build();
        }

        return Response.ok(projects).build();
    }

    @GET
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProject(@PathParam("projectId") int projectId) {
        Project project = Project.getProject(projectId);

        if (project == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Project with ID " + projectId + " not found."))
                    .build();
        }

        return Response.ok(project).build();
    }

    @PUT
    @Path("/{projectId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProject(@PathParam("projectId") int projectId, Project project) {
        boolean projectUpdated = Project.updateProject(projectId, project);

        if (getProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Project with ID " + projectId + " not found."))
                    .build();
        }

        if (!projectUpdated) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new SimpleEntry<>("message", "Unable to update project."))
                    .build();
        }

        return Response.ok(project).build();
    }

    @DELETE
    @Path("/{projectId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteProject(@PathParam("projectId") int projectId) {
        boolean status = Project.deleteProject(projectId);

        if (getProject(projectId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Project with ID " + projectId + " not found."))
                    .build();
        }

        if (!status) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new SimpleEntry<>("message", "Unable to delete project."))
                    .build();
        }

        return Response.ok().build();
    }

    @GET
    @Path("/{projectId}/projects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObjectives(@PathParam("projectId") int projectId) {
        List<Objective> objectives = Project.getProject(projectId).getObjectives();

        if (objectives.size() == 0) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(objectives).build();
    }

    @POST
    @Path("/{projectId}/objectives")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addObjective(@Context UriInfo uri, @PathParam("projectId") int projectId, Objective objective) {
        objective.setId(Objective.generateNewObjectiveId());

        if (!Objective.addObjective(projectId, objective)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Project with ID " + projectId + " not found."))
                    .build();
        }

        URI location = UriBuilder.fromUri(uri.getBaseUri())
            .path("projects")
            .path(String.valueOf(projectId))
            .path("objectives")
            .path(String.valueOf(objective.getId()))
            .build();
        return Response.created(location).entity(objective).build();
    }
}
