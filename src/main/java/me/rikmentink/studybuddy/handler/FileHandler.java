package me.rikmentink.studybuddy.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import me.rikmentink.studybuddy.model.Objective;
import me.rikmentink.studybuddy.model.Project;
import me.rikmentink.studybuddy.model.Student;

public class FileHandler {
    private static final String DATA_URL = "/home/data.json";
    private static final ObjectMapper objectMapper;
    
    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if (!Files.exists(Path.of(DATA_URL))) {
            try {
                Files.createFile(Path.of(DATA_URL));
            } catch (IOException e) {
                logError("Failed to create new data file: ", e);
            }
        }
    }

    /**
     * Reads all students from the data file.
     * 
     * @return List of Student objects representing all students.
     */
    public static List<Student> getAllStudents() {
        try {
            InputStream inputStream = new FileInputStream(new File(DATA_URL));
            return objectMapper.readValue(inputStream, new TypeReference<List<Student>>(){});
        } catch (IOException e) {
            logError("Failed to read students from data: ", e);
        }

        return new ArrayList<>();
    }
    
    /**
     * Retrieves a specific student based on the student ID.
     * 
     * @param studentId The ID of the student to retrieve.
     * @return The Student object matching the provided ID, or null if not found.
     */
    public static Student getStudent(int studentId) {
        List<Student> students = getAllStudents();
        return students.stream()
                .filter(student -> student.getId() == studentId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new student to the list of students.
     * 
     * @param student The Student object to add.
     * @return True if the student was successfully added, false otherwise.
     */
    public static boolean addStudent(Student student) {
        List<Student> students = getAllStudents();
        students.add(student);
        
        return writeStudentsToFile(students);
    }

    /**
     * Retrieves all projects from all students.
     * 
     * @return List of Project objects representing all the projects.
     */
    public static List<Project> getAllProjects() {
        List<Student> students = getAllStudents();
        return students.stream()
                .flatMap(student -> student.getProjects().stream())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all projects of a specific student based on the student ID.
     * 
     * @param studentId The ID of the student whose projects to retrieve.
     * @return List of Project objects representing the projects of the student.
     */
    public static List<Project> getProjects(int studentId) {
        Student student = getStudent(studentId);
        return student != null ? student.getProjects() : new ArrayList<>();
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
        List<Student> students = getAllStudents();
        Optional<Student> optionalStudent = students.stream()
                .filter(student -> student.getId() == studentId)
                .findFirst();

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.getProjects().add(project);

            return writeStudentsToFile(students);
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
        List<Student> students = getAllStudents();
        
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

        return writeStudentsToFile(students);
    } 

    /**
     * TODO: Document function.
     * 
     * @param projectId
     * @return
     */
    public static boolean deleteProject(int projectId) {
        List<Student> students = getAllStudents();
        
        students.forEach(student -> student.getProjects()
                .removeIf(project -> project.getId() == projectId));

        return writeStudentsToFile(students);
    } 

    /**
     * Retrieves all objectives from all projects from all students.
     * 
     * @return List of Objective objects representing all the objectives.
     */
    public static List<Objective> getAllObjectives() {
        List<Project> project = getAllProjects();
        return project.stream()
                .flatMap(foundProject -> foundProject.getObjectives().stream())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific objective based on its identifier.
     * 
     * @param objectiveId The ID of the objective to retrieve.
     * @return The Objective object matching the provided ID, or null if not 
     *         found.
     */
    public static Objective getObjective(int objectiveId) {
        List<Objective> objectives = getAllObjectives();
        return objectives.stream()
                .filter(objective -> objective.getId() == objectiveId)
                .findFirst()
                .orElse(null);
    }

    // TODO: Create addObjective() method.

    /**
     * Writes the given list of students to the data file.
     * 
     * @param students The list of students to write to the data file.
     * @return True if the data was successfully written, false otherwise.
     */
    private static boolean writeStudentsToFile(List<Student> students) {
        try (Writer writer = new FileWriter(DATA_URL)) { 
            objectMapper.writeValue(writer, students);
            return true;
        } catch (IOException e) {
            logError("Failed to write students to the data file: ", e);
        }

        return false;
    }

    /** 
     * Logs errors to the console, including the exception message.
     * 
     * @param message The error message to log.
     */
    private static void logError(String message, IOException e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
