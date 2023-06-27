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

import me.rikmentink.studybuddy.model.Task;

@Path("/tasks")
public class TaskController {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasks() {
        List<Task> tasks = Task.getAllTasks();

        if (tasks.size() == 0) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(tasks).build();
    }

    @GET
    @Path("/{taskId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask(@PathParam("taskId") int taskId) {
        Task task = Task.getTask(taskId);

        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Task with ID " + taskId + " not found."))
                    .build();
        }

        return Response.ok(task).build();
    }

    @PUT
    @Path("/{taskId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("taskId") int taskId, Task task) {
        if (getTask(taskId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Task with ID " + taskId + " not found."))
                    .build();
        }

        boolean taskUpdated = Task.updateTask(taskId, task);

        if (!taskUpdated) {
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(new SimpleEntry<>("message", "Unable to update task."))
                    .build();
        }

        return Response.ok(task).build();
    }

    @DELETE
    @Path("/{taskId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTask(@PathParam("taskId") int taskId) {
        if (getTask(taskId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleEntry<>("message", "Task with ID " + taskId + " not found."))
                    .build();
        }

        boolean status = Task.deleteTask(taskId);

        if (!status) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new SimpleEntry<>("message", "Unable to delete task."))
                    .build();
        }

        return Response.ok().build();
    }
}
