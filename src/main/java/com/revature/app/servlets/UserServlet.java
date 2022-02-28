package com.revature.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.dtos.NewUserRequest;
import com.revature.app.dtos.ResourceCreationResponse;
import com.revature.app.models.User;
import com.revature.app.services.UserService;

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
        this.userService = userService; this.mapper=mapper;
    }

    // Get all or one User endpoint
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("<h1>/get users works!</h1>");
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
            User newUser = userService.register(newUserRequest);
            resp.setStatus(201); // new User successfully created
            resp.setContentType("application/json");
            String payload = mapper.writeValueAsString(new ResourceCreationResponse(newUser.getId()));
            respWriter.write(payload);

        } catch(Exception e){
            // todo make more catch blocks for different exceptions
            e.printStackTrace();
            resp.setStatus(500); // internal server error
        }

    }
}
