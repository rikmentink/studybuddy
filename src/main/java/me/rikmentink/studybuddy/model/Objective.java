package me.rikmentink.studybuddy.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import me.rikmentink.studybuddy.handler.FileHandler;

public class Objective {
    private int id;
    private String name;
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;

    public Objective(String name, String description, LocalDateTime deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

    public Objective(@JsonProperty("id") int id, 
                     @JsonProperty("name") String name, 
                     @JsonProperty("description") String description, 
                     @JsonProperty("deadline") LocalDateTime deadline) {
        this(name, description, deadline);
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

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    public static List<Objective> getAllObjectives() {
        return FileHandler.getAllObjectives();
    }

    public static int generateNewObjectiveId() {
        List<Objective> objectiveList = FileHandler.getAllObjectives();
        
        if (objectiveList.isEmpty()) return 1;
        int maxId = objectiveList.stream()
                .mapToInt(Objective::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }
}
