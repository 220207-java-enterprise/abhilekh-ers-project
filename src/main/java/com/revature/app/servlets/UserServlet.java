package com.revature.app.servlets;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.dtos.requests.NewUserRequest;
import com.revature.app.dtos.requests.UpdateUserRequest;
import com.revature.app.dtos.responses.*;
import com.revature.app.models.User;
import com.revature.app.services.TokenService;
import com.revature.app.services.UserService;
import com.revature.app.util.exceptions.InvalidRequestException;
import com.revature.app.util.exceptions.ResourceConflictException;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


// Mapping: /users/*
public class UserServlet extends HttpServlet {

    private final TokenService tokenService;
    private final UserService userService;
    private final ObjectMapper mapper;

    public UserServlet(TokenService tokenService, UserService userService, ObjectMapper mapper){
        this.tokenService =tokenService;
        this.userService = userService;
        this.mapper=mapper;
    }

    // Get all or one User endpoint
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] reqFrags = req.getRequestURI().split("/");
        if(reqFrags.length==4 && reqFrags[3].equals("availability")){
            System.out.println(BCrypt.hashpw(("p4$$word"),BCrypt.gensalt()));
            checkAvailability(req, resp);
            return;
        }

        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if (requester==null){
            resp.getWriter().write("You need to be logged in.");
            resp.setStatus(401); // not logged in
            return;
        }

        if (!requester.getRole().equals("ADMIN")){
            resp.getWriter().write("Please login as an admin to complete this action.");
            resp.setStatus(403); // forbidden
            return;
        }

        List<GetUserResponse> users = userService.getAllUsers();
        String payload = mapper.writeValueAsString(users);
        resp.setContentType("application/json");
        resp.getWriter().write(payload);

        // redeploying will destroy session and it's attributes
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();

        try{
            NewUserRequest newUserRequest = mapper.readValue(req.getInputStream(), NewUserRequest.class);
            User newUser = userService.register(newUserRequest);
            resp.setStatus(201); // CREATED
            resp.setContentType("application/json");
            String payload = mapper.writeValueAsString(new ResourceCreationResponse(newUser.getId()));

            respWriter.write(payload);

        } catch (InvalidRequestException | DatabindException e) {
            resp.getWriter().write("Invalid Request. Data will not persist to database.");
            resp.setStatus(400); // BAD REQUEST
        } catch (ResourceConflictException e) {
            e.printStackTrace();
            resp.setStatus(409); // CONFLICT
        } catch (Exception e) {
            e.printStackTrace(); // include for debugging purposes; ideally log it to a file
            resp.setStatus(500);
        }

    }

    protected void checkAvailability(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String usernameValue = req.getParameter("username");
        String emailValue = req.getParameter("email");
        if(usernameValue != null){
            if(userService.isUsernameAvailable(usernameValue)){
                resp.getWriter().write("This Username is available.");
                resp.setStatus(200); // No content in body
            } else{
                resp.getWriter().write("This Username is already taken.");
                resp.setStatus(409); // Conflict
            }
        } else if (emailValue != null){
            if(userService.isEmailAvailable(emailValue)){
                resp.getWriter().write("This Email is available.");
                resp.setStatus(200); // No content in body
            } else{
                resp.getWriter().write("This Email is already in our system, try logging in.");
                resp.setStatus(409); // Conflict
            }
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if (requester==null){
            resp.getWriter().write("You need to be logged in.");
            resp.setStatus(401); // login before making an update
            return;
        }

        if (!requester.getRole().equals("ADMIN")){
            resp.getWriter().write("Please login as Admin to complete this action.");
            resp.setStatus(403); // forbidden
            return;
        }

        try{
        UpdateUserRequest updateUserRequest = mapper.readValue(req.getInputStream(), UpdateUserRequest.class);

        User user = userService.updateUser(updateUserRequest);

        String payload = mapper.writeValueAsString(new GetUserResponse(user));
        resp.setContentType("application/json");
        resp.getWriter().write("User updated.\n");
        resp.getWriter().write(payload);

        resp.setStatus(200);
        } catch (Exception e){
            resp.getWriter().write("User not found in our system.");
            resp.setStatus(404);
        }
    }
}
