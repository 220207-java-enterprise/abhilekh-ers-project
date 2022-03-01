package com.revature.app.servlets;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.dtos.requests.NewUserRequest;
import com.revature.app.dtos.responses.Principal;
import com.revature.app.dtos.responses.ResourceCreationResponse;
import com.revature.app.dtos.responses.UserResponse;
import com.revature.app.models.User;
import com.revature.app.services.UserService;
import com.revature.app.util.exceptions.InvalidRequestException;
import com.revature.app.util.exceptions.ResourceConflictException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


// Mapping: /users/*
public class UserServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;

    public UserServlet(UserService userService, ObjectMapper mapper){
        this.userService = userService;
        this.mapper=mapper;
    }

    // Get all or one User endpoint
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] reqFrags = req.getRequestURI().split("/");
        if(reqFrags.length==4 && reqFrags[3].equals("availability")){
            checkAvailability(req, resp);
            return;
        }

        // todo implement security logic here to protect sensitive operations

        HttpSession session = req.getSession(false);

        // if there is no session data, return 401 error
        if (session == null){
            resp.setStatus(401);
            return;
        }

        Principal requester = (Principal) session.getAttribute("authUser");

        if (!requester.getRole().equals("ADMIN")){
            resp.setStatus(403); // forbidden
        }

        List<UserResponse> users = userService.getAllUsers();
        System.out.println("ALL USERS--> " + users.toString());

        String payload = mapper.writeValueAsString(users);
        resp.setContentType("application/json");
        resp.getWriter().write(payload);

        // redeploying will destroy session and it's attributes
        resp.setStatus(200);
    }

    // Register a User endpoint
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();

        try{
            // we should find JSON data in body
            // we want to map it using ObjectMapper

            // next line expects newUserRequest to have a no args constructor
            NewUserRequest newUserRequest = mapper.readValue(req.getInputStream(), NewUserRequest.class);
            System.out.println("NEW USER REQUEST--------------> "+newUserRequest.toString());
            User newUser = userService.register(newUserRequest);
            System.out.println("NEW USER--------------> "+newUser.toString());
            resp.setStatus(201); // CREATED
            resp.setContentType("application/json");
            String payload = mapper.writeValueAsString(new ResourceCreationResponse(newUser.getId()));
            System.out.println("PAYLOAD--------------> " + payload);
            respWriter.write(payload);

        } catch (InvalidRequestException | DatabindException e) {
            e.printStackTrace();
            resp.setStatus(400); // BAD REQUEST
        } catch (ResourceConflictException e) {
            e.printStackTrace();
            resp.setStatus(409); // CONFLICT
        } catch (Exception e) {
            e.printStackTrace(); // include for debugging purposes; ideally log it to a file
            resp.setStatus(500);
        }

    }

    protected void checkAvailability(HttpServletRequest req, HttpServletResponse resp){
        String usernameValue = req.getParameter("username");
        String emailValue = req.getParameter("email");
        if(usernameValue != null){
            if(userService.isUsernameAvailable(usernameValue)){
                resp.setStatus(204); // No content in body
            } else{
                resp.setStatus(409); // Conflict
            }
        } else if (emailValue != null){
            if(userService.isEmailAvailable(emailValue)){
                resp.setStatus(204); // No content in body
            } else{
                resp.setStatus(409); // Conflict
            }
        }
    }
}
