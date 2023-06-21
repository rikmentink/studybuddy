package me.rikmentink.studybuddy;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.ArrayList;

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
    public void newObjectiveGetsUniqueId() {
        // Save the new student and the project.
        Student.addStudent(student);
        Project.addProject(student.getId(), project);

        // Test whether only one project has this ID
        boolean match = Project.getAllProjects().stream()
                .anyMatch(foundProject -> foundProject.getId() == project.getId());

        assertFalse(match);    
    }
}
