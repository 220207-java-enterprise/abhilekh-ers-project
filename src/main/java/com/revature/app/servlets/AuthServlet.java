package com.revature.app.servlets;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import javax.servlet.http.HttpSession;
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

        // .login() should return an app user (but we don't want to expose their credentials -- so we need a
        // Principal class that will return non-confidential authUser information
        try{
            LoginRequest loginRequest = mapper.readValue(req.getInputStream(), LoginRequest.class);
            Principal principal = new Principal(userService.login(loginRequest));
            String payload = mapper.writeValueAsString(principal);  // payload will be a Principal Object

            // Stateful session management - will change to token based session management
            // server will remember who logged in and store a value
//            HttpSession httpSession = req.getSession();
//            httpSession.setAttribute("authUser", principal);

            resp.setContentType("application/json");

            String token = tokenService.generateToken(principal);
            resp.setHeader("Authorization", token);

            writer.write(payload);

        } catch (InvalidRequestException | DatabindException e){
            resp.setStatus(400); // BAD REQUEST (bad username/password)
        } catch(AuthenticationException e){
            resp.setStatus(401); // credentials unknown
        } catch (Exception e){
            resp.setStatus(500);
            e.printStackTrace();
        }
    }
}
