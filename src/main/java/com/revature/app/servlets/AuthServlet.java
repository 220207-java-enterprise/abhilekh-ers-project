package com.revature.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.dtos.requests.LoginRequest;
import com.revature.app.dtos.responses.Principal;
import com.revature.app.models.User;
import com.revature.app.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;

    public AuthServlet(UserService userService, ObjectMapper mapper){
        this.userService = userService;
        this.mapper = mapper;
    }

    // Login endpoint
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        // .login() should return an app user (but we don't want to expose their credentials -- so we need a
        // Principal class that will return non-confidential authUser information
        try{
            LoginRequest loginRequest = mapper.readValue(req.getInputStream(), LoginRequest.class);
            String payload = mapper.writeValueAsString(new Principal(userService.login(loginRequest)));


        } catch (Exception e){
            resp.setStatus(500);
            e.printStackTrace();
        }
    }
}
