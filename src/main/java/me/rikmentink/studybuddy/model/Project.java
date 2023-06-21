package me.rikmentink.studybuddy.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import me.rikmentink.studybuddy.handler.FileHandler;

public class Project {
    private int id;
    private String name;
    private String description;
    private List<Objective> objectives; 
    private List<Task> tasks; 
    
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;

    public Project(String name, String description, LocalDate startDate, LocalDate endDate, List<Objective> objectives, List<Task> tasks) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.objectives = objectives;
        this.tasks = tasks;
    }

    @JsonCreator
    public Project(@JsonProperty("id") int id, 
                   @JsonProperty("name") String name, 
                   @JsonProperty("description") String description, 
                   @JsonProperty("startDate") LocalDate startDate, 
                   @JsonProperty("endDate") LocalDate endDate,
                   @JsonProperty("objectives") List<Objective> objectives,
                   @JsonProperty("tasks") List<Task> tasks) {
        this(name, description, startDate, endDate, objectives, tasks);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Objective> getObjectives() {
        return this.objectives != null ? this.objectives : new ArrayList<>();
    }

    public List<Task> getTasks() {
        return this.tasks != null ? this.tasks : new ArrayList<>();
    }

    /**
     * Retrieves all projects from all students.
     * 
     * @return List of Project objects representing all the projects.
     */
    public static List<Project> getAllProjects() {
        List<Student> students = Student.getAllStudents();
        return students.stream()
                .flatMap(student -> student.getProjects().stream())
                .collect(Collectors.toList());
    }

     /**
     * Retrieves a specific project of a specific student.
     * 
     * @param studentId The ID of the student whose project to retrieve.
     * @param projectId The ID of the project to retrieve.
     * @return The Project object matching the provided IDs, or null if not found.
     */
    public static Project getProject(int projectId) {
        List<Project> projects = getAllProjects();
        return projects.stream()
                .filter(project -> project.getId() == projectId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new project to the projects of a specific student.
     * 
     * @param studentId The ID of the student to whom the project belongs.
     * @param project   The Project object to add.
     * @return True if the project was successfully added, false otherwise.
     */
    public static boolean addProject(int studentId, Project project) {
        project.setId(generateNewProjectId());
        
        List<Student> students = Student.getAllStudents();
        Optional<Student> optionalStudent = students.stream()
                .filter(student -> student.getId() == studentId)
                .findFirst();

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.getProjects().add(project);

            return FileHandler.writeData(students);
        }
        
        return false;
    }

    /**
     * TODO: Document function.
     * 
     * @param projectId
     * @param updatedProject
     * @return
     */
    public static boolean updateProject(int projectId, Project updatedProject) {
        List<Student> students = Student.getAllStudents();
        
        students.stream()
            .flatMap(student -> student.getProjects().stream())
            .filter(project -> project.getId() == projectId)
            .findFirst()
            .ifPresent(project -> {
                project.setName(updatedProject.getName());
                project.setDescription(updatedProject.getDescription());
                project.setStartDate(updatedProject.getStartDate());
                project.setEndDate(updatedProject.getEndDate());
            });

        return FileHandler.writeData(students);
    } 

    /**
     * TODO: Document function.
     * 
     * @param projectId
     * @return
     */
    public static boolean deleteProject(int projectId) {
        List<Student> students = Student.getAllStudents();
        
        students.forEach(student -> student.getProjects()
                .removeIf(project -> project.getId() == projectId));

        return FileHandler.writeData(students);
    } 

    public static int generateNewProjectId() {
        List<Project> projects = getAllProjects();
        
        if (projects.isEmpty()) return 1;
        int maxId = projects.stream()
                .mapToInt(Project::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }
}