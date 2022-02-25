package com.revature.app;

import com.revature.app.daos.UserDAO;
import com.revature.app.models.User;
import com.revature.app.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class TempDriver {
    public static void main(String[] args) {

        // get instance of ConnectionFactory - only 1 instance possible because of Singleton Design Pattern
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

        Connection conn = null;

        try {
            conn = connectionFactory.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (conn == null) {
            System.out.println("Error!");
        } else {
            System.out.println("Success!");


            UserDAO crud = new UserDAO();

            User user = new User();

            user.setId("7c3521f5-ff75-4e8a-9913-01d15ee4db03");
            user.setUsername("test");
            user.setEmail("email@email.email");
            user.setPassword("p4$$word");
            user.setGivenName("fname");
            user.setSurname("lname");
            user.setIsActive(true);
            user.setRoleId("7c3521f5-ff75-4e8a-9913-01d15ee4da03");

            crud.update(user);
        }
    }
}
