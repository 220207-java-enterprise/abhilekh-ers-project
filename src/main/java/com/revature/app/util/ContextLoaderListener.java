package com.revature.app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.daos.ReimbursementDAO;
import com.revature.app.daos.ReimbursementStatusDAO;
import com.revature.app.daos.ReimbursementTypeDAO;
import com.revature.app.daos.UserDAO;
import com.revature.app.services.ReimbursementService;
import com.revature.app.services.TokenService;
import com.revature.app.services.UserService;
import com.revature.app.servlets.AuthServlet;
import com.revature.app.servlets.ReimbursementServlet;
import com.revature.app.servlets.UserServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    private static Logger logger = LogManager.getLogger(ContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("Initializing ERS web application");

        // object mapper
        ObjectMapper mapper = new ObjectMapper();

        // token classes
        JwtConfig jwtConfig = new JwtConfig();
        TokenService tokenService = new TokenService(jwtConfig);

        // wiring together dao, service, servlets
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);

        ReimbursementDAO reimbursementDAO = new ReimbursementDAO();
        ReimbursementTypeDAO reimbursementTypeDAO = new ReimbursementTypeDAO();
        ReimbursementStatusDAO reimbursementStatusDAO = new ReimbursementStatusDAO();

        ReimbursementService reimbursementService = new ReimbursementService(
                reimbursementDAO, reimbursementTypeDAO, reimbursementStatusDAO, userDAO);


        // servlets
        UserServlet userServlet = new UserServlet(tokenService, userService, mapper);
        AuthServlet authServlet = new AuthServlet(tokenService, userService, mapper);
        ReimbursementServlet reimbursementServlet = new ReimbursementServlet(tokenService, reimbursementService,
                mapper);

        // Programmatic Servlet Registration - connects UserServlet to ContextLoaderListener
        ServletContext context = sce.getServletContext();
        context.addServlet("UserServlet", userServlet).addMapping("/users/*");
        context.addServlet("AuthServlet", authServlet).addMapping("/auth");
        context.addServlet("ReimbursementServlet", reimbursementServlet).addMapping("/reimbursements/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.debug("Shutting down ERS web application");

        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);

    }
}
