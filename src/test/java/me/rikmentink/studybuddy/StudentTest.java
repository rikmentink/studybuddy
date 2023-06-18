package me.rikmentink.studybuddy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
        // Create a new student and assign a unique ID
        Student.addStudent(student);

        // Test whether only one student has this ID
        int studentsWithIdFound = Student.getAllStudents().stream()
                .filter(foundStudent -> foundStudent.getId() == student.getId())
                .collect(Collectors.toList())
                .size();    

        assertEquals(1, studentsWithIdFound);
    }
}
