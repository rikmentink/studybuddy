package me.rikmentink.studybuddy.model;

import java.time.LocalDateTime;

public class Objective {
    private int id;
    private String name;
    private String description;
    private LocalDateTime deadline;

    public Objective(int id, String name, String description, LocalDateTime deadline) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }
}
