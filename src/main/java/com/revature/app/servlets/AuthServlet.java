package com.revature.app.servlets;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.dtos.requests.LoginRequest;
import com.revature.app.dtos.responses.Principal;
import com.revature.app.services.TokenService;
import com.revature.app.services.UserService;
import com.revature.app.util.exceptions.AuthenticationException;
import com.revature.app.util.exceptions.InvalidRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class AuthServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(AuthServlet.class);

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

        logger.debug("AuthServlet#doPost invoked with args: "+ Arrays.asList(req, resp));

        try{
            LoginRequest loginRequest = mapper.readValue(req.getInputStream(), LoginRequest.class);
            Principal principal = new Principal(userService.login(loginRequest));

            if(!userService.isUserActive(principal.getId())){
                logger.debug("UserServlet#doGet returned 403 - User account is not active");
                resp.getWriter().write("User account is locked. Please contact the admin");
                resp.setStatus(403);
                return;
            }

            String payload = mapper.writeValueAsString(principal);

            resp.setContentType("application/json");

            String token = tokenService.generateToken(principal);
            resp.setHeader("Authorization", token);

            logger.debug("UserServlet#doGet returned AuthUser successfully.");

            writer.write("Login Success.\n");
            writer.write(payload);

        } catch (InvalidRequestException | DatabindException e){

            logger.debug("UserServlet#doGet returned 400 - Bad Request Sent");
            writer.write("Send a valid request containing username and password.");
            resp.setStatus(400);
            return;
        } catch(AuthenticationException e){
            logger.debug("UserServlet#doGet returned 401 - Invalid credentials provided");
            writer.write("Invalid Username/Password");
            resp.setStatus(401);
            return;
        } catch (Exception e){
            e.printStackTrace();
            resp.setStatus(500);

        }
    }
}
