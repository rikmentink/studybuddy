package me.rikmentink.studybuddy.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import me.rikmentink.studybuddy.model.Student;

public class FileHandler {
    private static final String DATA_URL = "/Users/rikmentink/Development/School/Projects/IPASS/studybuddy/data/data.json";
    private static final ObjectMapper objectMapper;
    
    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if (!Files.exists(Path.of(DATA_URL))) {
            try {
                Files.createFile(Path.of(DATA_URL));
            } catch (IOException e) {
                logError("Failed to create new data file: ", e);
            }
        }
    }

    /**
     * Reads all data from the json file.
     * 
     * @return List of Student objects representing all data.
     */
    public static List<Student> readData() {
        try {
            InputStream inputStream = new FileInputStream(new File(DATA_URL));
            return objectMapper.readValue(inputStream, new TypeReference<List<Student>>(){});
        } catch (IOException e) {
            logError("Failed to read data: ", e);
        }

        return new ArrayList<>();
    }

    /**
     * Writes the given list of students to the data file.
     * 
     * @param students The list of students to write to the data file.
     * @return True if the data was successfully written, false otherwise.
     */
    public static boolean writeData(List<Student> students) {
        try (Writer writer = new FileWriter(DATA_URL)) { 
            objectMapper.writeValue(writer, students);
            return true;
        } catch (IOException e) {
            logError("Failed to write students to the data file: ", e);
        }

        return false;
    }

    /** 
     * Logs errors to the console, including the exception message.
     * 
     * @param message The error message to log.
     */
    private static void logError(String message, IOException e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
