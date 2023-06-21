package me.rikmentink.studybuddy.model;

import java.time.LocalDateTime;

public class Task extends Objective {
    private boolean completed;

    public Task(int id, String name, String description, LocalDateTime deadline, String note, boolean completed) {
        super(id, name, description, deadline, note);
        this.completed = completed;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
