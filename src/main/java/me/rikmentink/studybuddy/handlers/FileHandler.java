package me.rikmentink.studybuddy.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import me.rikmentink.studybuddy.model.Project;
import me.rikmentink.studybuddy.model.Student;

public class FileHandler {

    public static ArrayList<Project> readProjects() {
        ArrayList<Project> projects = new ArrayList<>();

        try {
            if (!Files.exists(Path.of("data/projects.obj"))) {
                Files.createFile(Path.of("data/projects.obj"));
                return projects;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/projects.obj"))) {
                projects = (ArrayList<Project>) ois.readObject();
            } 
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
            
        return projects;
    }

    public static void createProject(Project project) {
        try {
            if (!Files.exists(Path.of("data/projects.obj"))) {
                Files.createFile(Path.of("data/projects.obj"));
                return;
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/projects.obj"))) {
                oos.writeObject(project);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Student> readStudents() {
        ArrayList<Student> students = new ArrayList<>();

        try {
            if (!Files.exists(Path.of("data/projects.obj"))) {
                Files.createFile(Path.of("data/projects.obj"));
                return students;
            }
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/students.obj"))) {
                students = (ArrayList<Student>) ois.readObject();
            } 
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
            
        return students;
    }
}
