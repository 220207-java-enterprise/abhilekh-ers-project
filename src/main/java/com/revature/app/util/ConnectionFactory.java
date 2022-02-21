package com.revature.app.util;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    // member variable for Singleton Design Pattern - will be EAGERLY loaded in getInstance() method
    private static ConnectionFactory connectionFactory;

    // static method to force JDBC connection class loading
    static{
        try{
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    // instantiate props
    Properties props = new Properties();

    // constructor
    private ConnectionFactory(){
        // load props file
        try {
            props.load(new FileReader("src/main/resources/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // static method to get instance when class is loaded -> EAGER LOADING in Singleton Pattern
    // will use this method to instantiate the class
    public static ConnectionFactory getInstance(){
        if (connectionFactory == null){
            connectionFactory = new ConnectionFactory();
        }
        return connectionFactory;
    }

    // create connection
    public Connection getConnection() throws SQLException {

        // parse props file
        Connection conn = DriverManager.getConnection(props.getProperty("db-url"), props.getProperty("db-username"),
                props.getProperty("db-password"));

        if (conn == null){
            throw new RuntimeException("Could not establish connection with the database!");
        }
        return conn;
    }
}
