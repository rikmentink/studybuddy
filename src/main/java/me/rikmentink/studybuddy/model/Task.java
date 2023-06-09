package me.rikmentink.studybuddy.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import me.rikmentink.studybuddy.handler.FileHandler;

public class Task extends Objective {
    private boolean completed;

    public Task(String name, String description, int expectedTime, LocalDateTime deadline, boolean completed) {
        super(name, description, expectedTime, deadline);
        this.completed = completed;
    }

    public Task(@JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("expectedTime") int expectedTime,
            @JsonProperty("deadline") LocalDateTime deadline,
            @JsonProperty("completed") boolean completed) {
        super(id, name, description, expectedTime, deadline);
        this.completed = completed;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Retrieves all tasks from all projects from all students.
     * 
     * @return List of Task objects representing all the objectives.
     */
    public static List<Task> getAllTasks() {
        List<Project> project = Project.getAllProjects();
        return project.stream()
                .flatMap(foundProject -> foundProject.getTasks().stream())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific task based on its identifier.
     * 
     * @param taskId The ID of the task to retrieve.
     * @return The Task object matching the provided ID, or null if not found.
     */
    public static Task getTask(int taskId) {
        List<Task> tasks = getAllTasks();
        return tasks.stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new task to the tasks of a specific project.
     * 
     * @param projectId The ID of the project to whom the task belongs.
     * @param task      The Task object to add.
     * @return True if the task was successfully added, false otherwise.
     */
    public static boolean addTask(int projectId, Task task) {
        task.setId(generateNewTaskId());
        List<Student> students = Student.getAllStudents();
        Optional<Student> optionalStudent = students.stream()
                .filter(student -> student.getProjects().stream()
                        .anyMatch(project -> project.getId() == projectId))
                .findFirst();

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            Optional<Project> optionalProject = student.getProjects().stream()
                    .filter(project -> project.getId() == projectId)
                    .findFirst();

            if (optionalProject.isPresent()) {
                Project project = optionalProject.get();
                List<Task> tasks = project.getTasks();
                tasks.add(task);

                return FileHandler.writeData(students);
            }
        }

        return false;
    }

    /**
     * Updates a task with the given taskId in a list of students' projects.
     * 
     * @param taskId      The ID of the task that needs to be updated.
     * @param updatedTask Object of type `Task` that represents the updated task
     *                    information.
     * @return Whether the task has successfully been updated or not.
     */
    public static boolean updateTask(int taskId, Task updatedTask) {
        List<Student> students = Student.getAllStudents();

        students.stream()
                .flatMap(student -> student.getProjects().stream())
                .filter(project -> project.getTasks().stream()
                        .anyMatch(task -> task.getId() == taskId))
                .findFirst()
                .ifPresent(project -> {
                    List<Task> tasks = project.getTasks();
                    tasks.stream()
                            .filter(task -> task.getId() == taskId)
                            .findFirst()
                            .ifPresent(task -> {
                                task.setName(updatedTask.getName());
                                task.setDescription(updatedTask.getDescription());
                                task.setExpectedTime(updatedTask.getExpectedTime());
                                task.setDeadline(updatedTask.getDeadline());
                                task.setCompleted(updatedTask.getCompleted());
                            });
                });

        return FileHandler.writeData(students);
    }

    /**
     * Deletes a task with a given taskId from all projects of all students and
     * returns
     * whether the data was successfully written.
     * 
     * @param taskId The unique identifier of the task that needs to be deleted.
     * @return Whether the task was successfully deleted or not.
     */
    public static boolean deleteTask(int taskId) {
        List<Student> students = Student.getAllStudents();

        students.forEach(student -> student.getProjects()
                .forEach(project -> project.getTasks().removeIf(task -> task.getId() == taskId)));

        return FileHandler.writeData(students);
    }

    /**
     * Generates a new task ID by finding the maximum ID from a list of tasks and
     * incrementing it by 1.
     * 
     * @return The new generated new task ID.
     */
    public static int generateNewTaskId() {
        List<Task> taskList = getAllTasks();

        if (taskList.isEmpty())
            return 1;
        int maxId = taskList.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }
}
