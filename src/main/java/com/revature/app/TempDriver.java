package com.revature.app;

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
        }
    }
}
