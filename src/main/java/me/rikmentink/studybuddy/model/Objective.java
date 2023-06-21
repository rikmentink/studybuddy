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
    private String note;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;

    public Objective(String name, String description, LocalDateTime deadline, String note) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.note = note;
    }

    public Objective(@JsonProperty("id") int id, 
                     @JsonProperty("name") String name, 
                     @JsonProperty("description") String description, 
                     @JsonProperty("deadline") LocalDateTime deadline,
                     @JsonProperty("note") String note) {
        this(name, description, deadline, note);
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

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
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
     * TODO: Document function.
     * 
     * @param objectiveId
     * @param updatedObjective
     * @return
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
                        objective.setDeadline(updatedObjective.getDeadline());
                        objective.setNote(updatedObjective.getNote());
                    });
            });

        return FileHandler.writeData(students);
    } 

    /**
     * TODO: Document function.
     * 
     * @param objectiveId
     * @return
     */
    public static boolean deleteObjective(int objectiveId) {
        List<Student> students = Student.getAllStudents();
        
        students.forEach(student -> student.getProjects().forEach(project ->
                project.getObjectives().removeIf(objective -> objective.getId() == objectiveId)));

        return FileHandler.writeData(students);
    } 

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
