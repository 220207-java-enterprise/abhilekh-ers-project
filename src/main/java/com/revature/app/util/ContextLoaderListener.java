package com.revature.app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.config.AppConfig;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    // IoC Container
    ApplicationContext appContext;

    private static Logger logger = LogManager.getLogger(ContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("Initializing ERS web application");

        appContext = new AnnotationConfigApplicationContext(AppConfig.class);

        // object mapper
        ObjectMapper mapper = new ObjectMapper();

        // manually grabbing beans to pass to servlets
        TokenService tokenService = appContext.getBean(TokenService.class);
        UserService userService = appContext.getBean(UserService.class);

        ReimbursementService reimbursementService = appContext.getBean(ReimbursementService.class);


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
