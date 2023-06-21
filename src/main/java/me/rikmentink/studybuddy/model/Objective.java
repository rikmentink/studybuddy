package me.rikmentink.studybuddy.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    /**
     * Retrieves all objectives from all projects from all students.
     * 
     * @return List of Objective objects representing all the objectives.
     */
    public static List<Objective> getAllObjectives() {
        List<Project> project = Project.getAllProjects();
        return project.stream()
                .flatMap(foundProject -> foundProject.getObjectives().stream())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific objective based on its identifier.
     * 
     * @param objectiveId The ID of the objective to retrieve.
     * @return The Objective object matching the provided ID, or null if not 
     *         found.
     */
    public static Objective getObjective(int objectiveId) {
        List<Objective> objectives = getAllObjectives();
        return objectives.stream()
                .filter(objective -> objective.getId() == objectiveId)
                .findFirst()
                .orElse(null);
    }

    // TODO: Create addObjective(), updateObjective() and deleteObjective() methods.

    public static int generateNewObjectiveId() {
        List<Objective> objectiveList = getAllObjectives();
        
        if (objectiveList.isEmpty()) return 1;
        int maxId = objectiveList.stream()
                .mapToInt(Objective::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }
}
