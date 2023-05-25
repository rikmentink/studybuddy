package me.rikmentink.studybuddy.model;

import java.io.Serializable;
import java.util.ArrayList;

import me.rikmentink.studybuddy.handlers.FileHandler;

public class Student implements Serializable {
    private int id;
    private String firstName;
    private String lastName;

    public Student(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public ArrayList<Project> getProjects() {
        ArrayList<Project> allProjects = FileHandler.readProjects();
        ArrayList<Project> projects = new ArrayList<Project>();

        for (Project project : allProjects) {
            if (project.getStudentId() == this.id) {
                projects.add(project);
            }
        }

        return projects;
    }

    public static Student getStudentById(int studentId) {
        ArrayList<Student> students = FileHandler.readStudents();
        Student student = null;

        for (Student s : students) {
            if (s.getId() == studentId) {
                student = s;
                break;
            }
        }

        return student;
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