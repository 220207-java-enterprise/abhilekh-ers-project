package com.revature.app.servlets;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.revature.app.dtos.requests.LoginRequest;
import com.revature.app.dtos.responses.Principal;
import com.revature.app.services.TokenService;
import com.revature.app.services.UserService;
import com.revature.app.util.exceptions.AuthenticationException;
import com.revature.app.util.exceptions.InvalidRequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthServlet extends HttpServlet {

    private final TokenService tokenService;
    private final UserService userService;
    private final ObjectMapper mapper;

    public AuthServlet(TokenService tokenService, UserService userService, ObjectMapper mapper){
        this.tokenService = tokenService;
        this.userService = userService;
        this.mapper = mapper;
    }

    // Login endpoint
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try{
            LoginRequest loginRequest = mapper.readValue(req.getInputStream(), LoginRequest.class);
            Principal principal = new Principal(userService.login(loginRequest));


            if(!userService.isUserActive(principal.getId())){
                resp.getWriter().write("User account is locked. Please contact the admin");
                resp.setStatus(403);
                return;
            }

            String payload = mapper.writeValueAsString(principal);

            resp.setContentType("application/json");

            String token = tokenService.generateToken(principal);
            resp.setHeader("Authorization", token);

            writer.write("Login Success.\n");
            writer.write(payload);

        } catch (InvalidRequestException | DatabindException e){
            writer.write("Send a valid request containing username and password.");
            resp.setStatus(400);
            return;
        } catch(AuthenticationException e){
            writer.write("Invalid Username/Password");
            resp.setStatus(401);
            return;
        } catch (Exception e){
            e.printStackTrace();
            resp.setStatus(500);

        }
    }
}
