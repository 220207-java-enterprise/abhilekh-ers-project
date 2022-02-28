package com.revature.app.util;

import com.revature.app.daos.UserDAO;
import com.revature.app.services.UserService;
import com.revature.app.servlets.AuthServlet;
import com.revature.app.servlets.UserServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing ERS web application");

        // wiring together dao, service, servlets
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);

        // servlets
        UserServlet userServlet = new UserServlet(userService);
        AuthServlet authServlet = new AuthServlet(userService);

        // Programmatic Servlet Registration - connects UserServlet to ContextLoaderListener
        ServletContext context = sce.getServletContext();
        context.addServlet("UserServlet", userServlet).addMapping("/users/*");
        context.addServlet("AuthServlet", authServlet).addMapping("/auth");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Shutting down ERS web application");

        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);

    }
}
