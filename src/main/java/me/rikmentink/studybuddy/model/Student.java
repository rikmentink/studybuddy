package me.rikmentink.studybuddy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import me.rikmentink.studybuddy.handler.FileHandler;

public class Student implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Project> projects;

    public Student(String firstName, String lastName, String email, String password, List<Project> projects) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.projects = projects;
    }

    @JsonCreator
    public Student(@JsonProperty("id")int id, 
                   @JsonProperty("firstName") String firstName, 
                   @JsonProperty("lastName") String lastName, 
                   @JsonProperty("email") String email, 
                   @JsonProperty("password") String password, 
                   @JsonProperty("projects") List<Project> projects) {
        this(firstName, lastName, email, password, projects);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public List<Project> getProjects() {
        return this.projects != null ? this.projects : new ArrayList<>();
    }

    public boolean addProject(Project project) {
        return Project.addProject(this.id, project);
    }

    /**
     * Reads all students from the data file.
     * 
     * @return List of Student objects representing all students.
     */
    public static List<Student> getAllStudents() {
        return FileHandler.readData();
    }

    /**
     * Retrieves a specific student based on the student ID.
     * 
     * @param studentId The ID of the student to retrieve.
     * @return The Student object matching the provided ID, or null if not found.
     */
    public static Student getStudent(int studentId) {
        List<Student> students = getAllStudents();
        return students.stream()
                .filter(student -> student.getId() == studentId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new student to the list of students.
     * 
     * @param student The Student object to add.
     * @return The unique identifier if the student was successfully added, -1 
     * otherwise.
     */
    public static int addStudent(Student student) {
        student.setId(generateNewStudentId());

        List<Student> students = getAllStudents();
        students.add(student);
        
        if (FileHandler.writeData(students)) {
            return student.getId();
        } else {
            return -1;
        }
    }
    
    private static int generateNewStudentId() {
        List<Student> students = getAllStudents();
        
        if (students.isEmpty()) return 1;
        int maxId = students.stream()
                .mapToInt(Student::getId)
                .max()
                .orElse(0);
        return maxId + 1;
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