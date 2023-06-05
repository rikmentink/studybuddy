package me.rikmentink.studybuddy.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import me.rikmentink.studybuddy.handler.FileHandler;

public class Student implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private List<Project> projects;

    @JsonCreator
    public Student(@JsonProperty("id")int id, 
                   @JsonProperty("firstName") String firstName, 
                   @JsonProperty("lastName") String lastName, 
                   @JsonProperty("projects") List<Project> projects) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.projects = projects;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public List<Project> getProjects() {
        return this.projects;
    }

    public static int generateNewStudentId() {
        List<Student> students = FileHandler.getAllStudents();
        
        if (students.isEmpty()) return 1;
        int maxId = students.stream()
                .mapToInt(Student::getId)
                .max()
                .orElse(0);
        return maxId++;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Student student = (Student) obj;
        return this.id == student.id &&
               this.firstName.equals(student.getFirstName()) &&
               this.lastName.equals(student.getLastName());
    }
}  