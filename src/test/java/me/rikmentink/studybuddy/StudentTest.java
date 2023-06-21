package me.rikmentink.studybuddy;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.rikmentink.studybuddy.model.Student;

public class StudentTest {
    private static Student student;

    @BeforeEach
    public void init() {
        student = new Student(
            "Test", 
            "Student",  
            "test@test.nl",
            "12345",
            new ArrayList<>()
        );
    }

    @Test
    public void newStudentGetsUniqueId() {
        // Save the new student with an empty project list
        Student.addStudent(student);

        // Test whether only one student has this ID
        boolean match = Student.getAllStudents().stream()
                .anyMatch(foundStudent -> foundStudent.getId() == student.getId());

        assertFalse(match);    
    }
}
