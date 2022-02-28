package com.revature.app;


import com.revature.app.daos.UserDAO;
import com.revature.app.dtos.requests.NewUserRequest;
import com.revature.app.models.User;
import com.revature.app.services.UserService;

public class AppDriver {

    public static void main(String[] args) {
        System.out.println("project begins...");

        NewUserRequest newUserRequest = new NewUserRequest("jondoe11", "jondoe@gmail.com", "p4$$word", "Jon",
                "Doe", true, "7c3521f5-ff75-4e8a-9913-01d15ee4da03");

        User newUser = newUserRequest.extractUser();

        System.out.println(newUser);

        UserDAO ud = new UserDAO();
        UserService us = new UserService(ud);

        System.out.println(newUser.getEmail());
        us.register(newUserRequest);

    }
}
