package me.rikmentink.studybuddy.controller;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import me.rikmentink.studybuddy.model.Student;

@Path("/auth")
public class AuthController {
    public static final Key key = MacProvider.generateKey();

    /**
     * Handles a login request by authenticating the user's email and password.
     * When a request is successful, return a JSON web token in the response.
     * 
     * @param request The login request with the user data.
     * @return A response containing a JWT and user id if succesful, otherwise 
     *         returns error 401.
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request) {
        Student requestedStudent = authenticate(request.email, request.password);
        if (requestedStudent != null) {
            String token = generateToken(request.email);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", String.valueOf(requestedStudent.getId()));

            return Response.ok(response).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).entity(new SimpleEntry<>("message", "The username or password is incorrect!")).build();
    }

    /**
     * Handles a register request by checking whether the e-mail address is
     * unique, and if yes save the account data. When a request is successful,
     * return a JSON web token and the account ID in the response.
     * 
     * @param request The register request with the user data.
     * @return A response containing a JWT and user ID if succesful, otherwise 
     *         returns error 401.
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterRequest request) {
        if (request.firstname == null || request.lastname == null || request.email == null || request.password == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new SimpleEntry<>("message", "Required information is missing.")).build();
        }

        if (!isEmailUnique(request.email)) {
            return Response.status(Response.Status.CONFLICT).entity(new SimpleEntry<>("message", "This email address is already in use.")).build();
        }

        int userId = saveAccountDetails(request);
        if (userId == -1) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new SimpleEntry<>("message", "Failed to save account details.")).build();
        }

        String token = generateToken(request.email);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", String.valueOf(userId));

        return Response.ok(response).build();
    }

    /**
     * Authenticates a user by checking if their email and password match any 
     * of the students' email and password from the data.
     * 
     * @param email The email address of the user trying to authenticate.
     * @param password The password entered by the user trying to authenticate.
     * @return Returns whether a match of email and password was found in data.
     */
    private Student authenticate(String email, String password) {
        List<Student> users = Student.getAllStudents();

        return users.stream()
            .filter(user -> {
                String userEmail = user.getEmail();
                String userPassword = user.getPassword();
                
                return userEmail.equals(email) && userPassword.equals(password);
            })
            .findFirst()
            .orElse(null);
    }

    /**
     * This function generates a token with a 30-minute expiration time using
     * the user's email address as the subject and a secret key for signing.
     * 
     * @param email The user's email which will be used as token subject.
     * @return A unique JSON web token that has been generated.
     */
    private String generateToken(String email) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MINUTE, 30);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expiration.getTime())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    /**
     * Checks if a given email is unique among all students' emails.
     * 
     * @param email The email address that we want to check for uniqueness.
     * @return Returns true whether the given email is unique, false otherwise.
     */
    private boolean isEmailUnique(String email) {
        return !Student.getAllStudents().stream()
                .anyMatch(student -> student.getEmail().equals(email));
    }

    /**
     * The function creates a new student object with the details provided in the register request and
     * adds it to a list of students.
     * 
     * @param request The parameter "request" is of type RegisterRequest, which is likely a custom
     * class that contains the details of a student's registration request, such as their first name,
     * last name, email, and password.  
     * @return The method `saveAccountDetails` is returning an integer value which is the result of
     * calling the static method `addStudent` of the `Student` class with the `student` object as a
     * parameter. The integer value returned by `addStudent` is likely an identifier or index assigned
     * to the newly created student object.
     */
    private int saveAccountDetails(RegisterRequest request) {
        Student student = new Student(
            request.firstname,
            request.lastname,
            request.email,
            request.password,
            new ArrayList<>()
        );
        return Student.addStudent(student);
    }
}

class LoginRequest {
    public String email;
    public String password;
}

class RegisterRequest {
    public String firstname;
    public String lastname;
    public String email;
    public String password;
}