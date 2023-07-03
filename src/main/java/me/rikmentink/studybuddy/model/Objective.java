package me.rikmentink.studybuddy.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import me.rikmentink.studybuddy.handler.FileHandler;

public class Objective {
    private int id;
    private String name;
    private String description;
    private int expectedTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;

    public Objective(String name, String description, int expectedTime, LocalDateTime deadline) {
        this.name = name;
        this.description = description;
        this.expectedTime = expectedTime;
        this.deadline = deadline;
    }

    public Objective(@JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("expectedTime") int expectedTime,
            @JsonProperty("deadline") LocalDateTime deadline) {
        this(name, description, expectedTime, deadline);
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExpectedTime() {
        return this.expectedTime;
    }

    public void setExpectedTime(int expectedTime) {
        this.expectedTime = expectedTime;
    }

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
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

    /**
     * Adds a new objective to the objectives of a specific project.
     * 
     * @param projectId The ID of the project to whom the objective belongs.
     * @param project   The Objective object to add.
     * @return True if the objective was successfully added, false otherwise.
     */
    public static boolean addObjective(int projectId, Objective objective) {
        objective.setId(generateNewObjectiveId());

        List<Student> students = Student.getAllStudents();
        Optional<Student> optionalStudent = students.stream()
                .filter(student -> student.getProjects().stream()
                        .anyMatch(project -> project.getId() == projectId))
                .findFirst();

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            Optional<Project> optionalProject = student.getProjects().stream()
                    .filter(project -> project.getId() == projectId)
                    .findFirst();

            if (optionalProject.isPresent()) {
                Project project = optionalProject.get();
                List<Objective> objectives = project.getObjectives();
                objectives.add(objective);

                return FileHandler.writeData(students);
            }
        }

        return false;
    }

    /**
     * Updates an objective with the given objectiveId in a list of students'
     * projects and
     * returns whether the data was successfully written to a file.
     * 
     * @param objectiveId      The unique identifier
     *                         of the objective that needs to be updated.
     * @param updatedObjective The updated information for the objective that needs
     *                         to be updated.
     * @return Whether the objective was succesfully updated or not.
     */
    public static boolean updateObjective(int objectiveId, Objective updatedObjective) {
        List<Student> students = Student.getAllStudents();

        students.stream()
                .flatMap(student -> student.getProjects().stream())
                .filter(project -> project.getObjectives().stream()
                        .anyMatch(objective -> objective.getId() == objectiveId))
                .findFirst()
                .ifPresent(project -> {
                    List<Objective> objectives = project.getObjectives();
                    objectives.stream()
                            .filter(objective -> objective.getId() == objectiveId)
                            .findFirst()
                            .ifPresent(objective -> {
                                objective.setName(updatedObjective.getName());
                                objective.setDescription(updatedObjective.getDescription());
                                objective.setExpectedTime(updatedObjective.getExpectedTime());
                                objective.setDeadline(updatedObjective.getDeadline());
                            });
                });

        return FileHandler.writeData(students);
    }

    /**
     * Deletes an objective with a specific ID from all projects of all students and
     * returns whether the data was successfully written.
     * 
     * @param objectiveId The unique identifier
     *                    of the objective that needs to be deleted.
     * @return Whether the objective was successfully deleted or not.
     */
    public static boolean deleteObjective(int objectiveId) {
        List<Student> students = Student.getAllStudents();

        students.forEach(student -> student.getProjects()
                .forEach(project -> project.getObjectives().removeIf(objective -> objective.getId() == objectiveId)));

        return FileHandler.writeData(students);
    }

    /**
     * Generates a new objective ID by finding the maximum ID from a list of
     * objectives
     * and incrementing it by 1.
     * 
     * @return The generated new objective ID.
     */
    public static int generateNewObjectiveId() {
        List<Objective> objectiveList = getAllObjectives();

        if (objectiveList.isEmpty())
            return 1;
        int maxId = objectiveList.stream()
                .mapToInt(Objective::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }
}
