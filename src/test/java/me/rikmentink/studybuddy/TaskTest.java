package me.rikmentink.studybuddy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

import me.rikmentink.studybuddy.model.Project;
import me.rikmentink.studybuddy.model.Student;
import me.rikmentink.studybuddy.model.Task;

public class TaskTest {
    private static Student student;
    private static Project project;
    private static Task task;

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
        task = new Task(
            "Test", 
            "This is a task meant for testing.",
            LocalDateTime.parse("2023-06-15 09:00"),
            "Test notes",
            false
        );
    }

    // TODO: add newProjectGetsUniqueId() test.
}
