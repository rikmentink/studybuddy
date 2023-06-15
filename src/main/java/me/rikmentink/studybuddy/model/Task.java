package me.rikmentink.studybuddy.model;

import java.time.LocalDateTime;

public class Task extends Objective {
    private boolean completed;

    public Task(int id, String name, String description, LocalDateTime deadline, boolean completed) {
        super(id, name, description, deadline);
        this.completed = completed;
    }
}
