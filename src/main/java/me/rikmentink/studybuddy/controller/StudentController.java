package me.rikmentink.studybuddy.controller;

import java.net.URI;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import me.rikmentink.studybuddy.handler.FileHandler;
import me.rikmentink.studybuddy.model.Project;
import me.rikmentink.studybuddy.model.Student;

@Path("/students")
public class StudentController {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudents() {
        List<Student> students = FileHandler.getAllStudents();

        if (students.size() == 0) {
            return Response.noContent().build();
        }

        return Response.ok(students).build();
    }

    @GET
    @Path("/{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("studentId") int studentId) {
        Student student = FileHandler.getStudent(studentId);

        if (student == null) {
            JsonObject errorMessage = Json.createObjectBuilder()
                    .add("error", "Student with ID " + studentId + " not found.")
                    .build();
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        return Response.ok(student).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudent(@Context UriInfo uri, Student student) {
        if (!FileHandler.addStudent(student)) {
            JsonObject errorMessage = Json.createObjectBuilder()
                .add("error", "Student with ID " + student.getId() + " couldn't be created.")
                .build();

            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        URI location = UriBuilder.fromUri(uri.getBaseUri())
            .path("students")
            .path(String.valueOf(student.getId()))
            .build();
        return Response.created(location).entity(student).build();
    }

    @GET
    @Path("/{studentId}/projects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjects(@PathParam("studentId") int studentId) {
        List<Project> projects = FileHandler.getProjects(studentId);

        if (projects.size() == 0) {
            return Response.status(Response.Status.NO_CONTENT).entity(projects).build();
        }

        return Response.ok(projects).build();
    }

    @GET
    @Path("/{studentId}/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjects(@PathParam("studentId") int studentId, @PathParam("projectId") int projectId) {
        Project project = FileHandler.getProject(studentId, projectId);

        if (project == null) {
            JsonObject errorMessage = Json.createObjectBuilder()
                    .add("error", "Project with ID " + projectId + " not found.")
                    .build();
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        return Response.ok(project).build();
    }

    @POST
    @Path("/{studentId}/projects")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProject(@Context UriInfo uri, @PathParam("studentId") int studentId, Project project) {
        project.setId(Project.generateNewProjectId());

        if (!FileHandler.addProject(studentId, project)) {
            JsonObject errorMessage = Json.createObjectBuilder()
                .add("error", "Student with ID " + studentId + " not found.")
                .build();

            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        URI location = UriBuilder.fromUri(uri.getBaseUri())
            .path("students")
            .path(String.valueOf(studentId))
            .path("projects")
            .path(String.valueOf(project.getId()))
            .build();
        return Response.created(location).entity(project).build();
    }
}
