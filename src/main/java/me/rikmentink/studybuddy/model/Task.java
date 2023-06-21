package me.rikmentink.studybuddy.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import me.rikmentink.studybuddy.handler.FileHandler;

public class Task extends Objective {
    private boolean completed;

    public Task(String name, String description, LocalDateTime deadline, String note, boolean completed) {
        super(name, description, deadline, note);
        this.completed = completed;
    }

    public Task(@JsonProperty("id") int id, 
                     @JsonProperty("name") String name, 
                     @JsonProperty("description") String description, 
                     @JsonProperty("deadline") LocalDateTime deadline,
                     @JsonProperty("note") String note,
                     @JsonProperty("completed") boolean completed) {
        super(id, name, description, deadline, note);
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
     * @param task   The Task object to add.
     * @return True if the task was successfully added, false otherwise.
     */
    public static boolean addTask(int projectId, Task task) {
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
     * TODO: Document function.
     * 
     * @param taskId
     * @param updatedTask
     * @return
     */
    public static boolean updateTask(int taskId, Objective updatedTask) {
        List<Student> students = Student.getAllStudents();
        
        students.stream()
            .flatMap(student -> student.getProjects().stream())
            .filter(project -> project.getObjectives().stream()
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
                        task.setDeadline(updatedTask.getDeadline());
                        task.setNote(updatedTask.getNote());
                    });
            });

        return FileHandler.writeData(students);
    } 

    /**
     * TODO: Document function.
     * 
     * @param taskId
     * @return
     */
    public static boolean deleteTask(int taskId) {
        List<Student> students = Student.getAllStudents();
        
        students.forEach(student -> student.getProjects().forEach(project ->
                project.getTasks().removeIf(task -> task.getId() == taskId)));

        return FileHandler.writeData(students);
    } 

    public static int generateNewTaskId() {
        List<Task> taskList = getAllTasks();
        
        if (taskList.isEmpty()) return 1;
        int maxId = taskList.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }
}
