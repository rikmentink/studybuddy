package me.rikmentink.studybuddy;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.rikmentink.studybuddy.model.Objective;
import me.rikmentink.studybuddy.model.Project;
import me.rikmentink.studybuddy.model.Student;

public class ObjectiveTest {
    private static Student student;
    private static Project project;
    private static Objective objective;

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
        objective = new Objective(
            "Test", 
            "This is an objective meant for testing.",
            LocalDateTime.parse("2023-06-15 09:00"),
            "Test notes"
        );
    }

    @Test
    public void newObjectiveGetsUniqueId() {
        // Save the new student, project and the objective.
        Student.addStudent(student);
        Project.addProject(student.getId(), project);
        Objective.addObjective(project.getId(), objective);

        // Test whether only one objective has this ID
        boolean match = Objective.getAllObjectives().stream()
                .anyMatch(foundObjective -> foundObjective.getId() == objective.getId());

        assertFalse(match);    
    }
}
