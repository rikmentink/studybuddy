package me.rikmentink.studybuddy.controller;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import me.rikmentink.studybuddy.model.Project;
import me.rikmentink.studybuddy.model.Student;

@Path("/projects")
public class ProjectController {

    @GET
    @Path("{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjects(@PathParam("studentId") int studentId) {
        Student student = Student.getStudentById(studentId);

        if (student == null) {
            return Response.status(409).entity(new SimpleEntry<>("error", "Student not found!")).build();
        }
        
        return Response.ok(student.getProjects()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProject(String payload) {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject jsonData = reader.readObject();
        reader.close();

        Project project = Project.addProject(
            jsonData.getInt("studentId"), 
            jsonData.getString("name"), 
            jsonData.getString("description"), 
            jsonData.getString("startDate"),
            jsonData.getString("endDate")
        );

        return Response.ok(project).build();
    }
}
