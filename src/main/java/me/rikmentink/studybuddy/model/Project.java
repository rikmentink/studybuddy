package me.rikmentink.studybuddy.model;

import java.time.LocalDateTime;

import me.rikmentink.studybuddy.handlers.FileHandler;

public class Project {
    private static int nextId = 1;

    private int id;
    private int studentId;
    private String name;
    private String description;
    private String startDate;
    private String endDate;

    public Project(int id, int studentId, String name, String description, String startDate, String endDate) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return this.id;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public static Project addProject(int studentId, String name, String description, String startDate, String endDate) {
        Project project = new Project(
            nextId++,
            studentId,
            name,
            description,
            startDate,
            endDate
        );

        FileHandler.createProject(project);

        return project;
    }
}