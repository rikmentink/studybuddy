package me.rikmentink.studybuddy.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public List<Project> getProjects() {
        return this.projects;
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