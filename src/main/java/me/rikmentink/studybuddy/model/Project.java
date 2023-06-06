package me.rikmentink.studybuddy.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import me.rikmentink.studybuddy.handler.FileHandler;

public class Project {
    private int id;
    private String name;
    private String description;
    
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;

    public Project(String name, String description, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @JsonCreator
    public Project(@JsonProperty("id") int id, 
                   @JsonProperty("name") String name, 
                   @JsonProperty("description") String description, 
                   @JsonProperty("startDate") LocalDate startDate, 
                   @JsonProperty("endDate") LocalDate endDate) {
        this(name, description, startDate, endDate);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public static int generateNewProjectId() {
        List<Project> projects = FileHandler.getAllProjects();
        
        if (projects.isEmpty()) return 1;
        int maxId = projects.stream()
                .mapToInt(Project::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }
}