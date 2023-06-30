package me.rikmentink.studybuddy;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            1,
            LocalDateTime.parse("2023-06-15 09:00"),
            false
        );
    }

    @Test
    public void newTaskGetsUniqueId() {
        // Add the new student, project and the task.
        Student.addStudent(student);
        Project.addProject(student.getId(), project);
        Task.addTask(project.getId(), task);

        // Test whether only one task has this ID
        boolean match = Task.getAllTasks().stream()
                .anyMatch(foundTask -> foundTask.getId() == task.getId());

        assertFalse(match);    
    }
}
