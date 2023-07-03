package me.rikmentink.studybuddy.controller;

import java.net.URI;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

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

import me.rikmentink.studybuddy.model.Project;
import me.rikmentink.studybuddy.model.Student;

@Path("/students")
public class StudentController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudents() {
        List<Student> students = Student.getAllStudents();

        if (students.size() == 0) {
            return Response.noContent().build();
        }

        return Response.ok(students).build();
    }

    @GET
    @Path("/{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("studentId") int studentId) {
        Student student = Student.getStudent(studentId);

        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Student with ID " + studentId + " not found."))
                    .build();
        }

        return Response.ok(student).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudent(@Context UriInfo uri, Student student) {
        if (Student.addStudent(student) == -1) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new SimpleEntry<>("message",
                            "Student with ID " + student.getId() + " couldn't be created."))
                    .build();
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
        List<Project> projects = Student.getStudent(studentId).getProjects();

        if (projects.size() == 0) {
            return Response.status(Response.Status.NO_CONTENT).entity(projects).build();
        }

        return Response.ok(projects).build();
    }

    @POST
    @Path("/{studentId}/projects")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProject(@Context UriInfo uri, @PathParam("studentId") int studentId, Project project) {
        if (!Project.addProject(studentId, project)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Student with ID " + studentId + " not found."))
                    .build();
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
