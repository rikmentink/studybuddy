package me.rikmentink.studybuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.rikmentink.studybuddy.model.Project;
import me.rikmentink.studybuddy.model.Student;

public class ProjectTest {
    private static Student student;
    private static Project project;

    @BeforeEach
    public void init() {
        student = new Student(
            "Test", 
            "Student",  
            "test@test.nl",
            "12345",
            new ArrayList<>()
        );
        project = new Project(
            "Test", 
            "This is a project meant for testing.",
            LocalDate.parse("2023-06-08"),
            LocalDate.parse("2023-06-15"),
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    @Test
    public void newProjectGetsUniqueId() {
        // Save the new student with an empty project list
        Student.addStudent(student);

        // Add a new project and assign a unique ID
        int uniqueId = Project.generateNewProjectId();
        project.setId(uniqueId);
        student.addProject(project);

        // Test whether only one project has this ID
        int projectsWithIdFound = Project.getAllProjects().stream()
                .filter(project -> project.getId() == uniqueId)
                .collect(Collectors.toList())
                .size();    

        assertEquals(1, projectsWithIdFound);
    }
}
