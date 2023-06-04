package me.rikmentink.studybuddy.handler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import me.rikmentink.studybuddy.model.Project;
import me.rikmentink.studybuddy.model.Student;

public class FileHandler {
    private static final ObjectMapper objectMapper;
    
    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Reads all students from data
    public static List<Student> getAllStudents() {
        try {
            InputStream inputStream = FileHandler.class.getClassLoader().getResourceAsStream("data.json");
            return objectMapper.readValue(inputStream, new TypeReference<List<Student>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<Student>();
    }
    
    // Reads a specific student from data
    public static Student getStudent(int studentId) {
        List<Student> students = getAllStudents();
        return students.stream()
                .filter(student -> student.getId() == studentId)
                .findFirst()
                .orElse(null);
    }

    // Reads a student's projects
    public static List<Project> getAllProjects() {
        List<Student> students = getAllStudents();
        return students.stream()
                .flatMap(student -> student.getProjects().stream())
                .collect(Collectors.toList());
    }

    // Reads a student's projects
    public static List<Project> getProjects(int studentId) {
        Student student = getStudent(studentId);
        return student.getProjects();
    }

    // Reads a student's specific project
    public static Project getProject(int studentId, int projectId) {
        List<Project> projects = getStudent(studentId).getProjects();
        return projects.stream()
                .filter(project -> project.getId() == projectId)
                .findFirst()
                .orElse(null);
    }

    public static boolean addProject(int studentId, Project project) {
        try {
            List<Student> students = getAllStudents();
            Student student = getStudent(studentId);
            
            if (student == null) return false;

            student.getProjects().add(project);
            try (Writer writer = new FileWriter("src/main/resources/data.json")) {
                objectMapper.writeValue(writer, students);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
