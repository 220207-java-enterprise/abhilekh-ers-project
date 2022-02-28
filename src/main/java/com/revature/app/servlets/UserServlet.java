package com.revature.app.servlets;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.dtos.requests.NewUserRequest;
import com.revature.app.dtos.responses.ResourceCreationResponse;
import com.revature.app.models.User;
import com.revature.app.services.UserService;
import com.revature.app.util.exceptions.InvalidRequestException;
import com.revature.app.util.exceptions.ResourceConflictException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


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

        if(req.getRequestURI().split("/")[3].equals("availability")){
            checkAvailability(req, resp);
            return;
        }

        // todo implement security logic here to protect sensitive operations

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
