package com.revature.app.daos;

import com.revature.app.models.User;
import com.revature.app.util.ConnectionFactory;
import com.revature.app.util.exceptions.ResourcePersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO  implements CrudDAO<User>{

    // CREATE
    @Override
    public void save(User newUser) {

        // create connection
        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            //prepare statement
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users VALUES (?,?,?,?,?,?,?,?)");

            //set ResultSet values to class attributes
            pstmt.setString(1, newUser.getId());
            pstmt.setString(2, newUser.getUsername());
            pstmt.setString(3, newUser.getEmail());
            pstmt.setString(4, newUser.getPassword());
            pstmt.setString(5, newUser.getGivenName());
            pstmt.setString(6, newUser.getSurname());
            pstmt.setBoolean(7, newUser.getIsActive());
            pstmt.setString(8, newUser.getRoleId());

            int rowsInserted = pstmt.executeUpdate(); // returns 1 if successful

            if (rowsInserted != 1){
                throw new ResourcePersistenceException("Failed to persist user to data source");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    // READ ONE
    @Override
    public User getById(String id) {

        User foundUser = null;

        // try to make a connection
        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            //prepared statement
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE id=?");
            // pass username and password to pstmt
            pstmt.setString(1, id);

            //store the query in ResultSet
            ResultSet rs = pstmt.executeQuery();
            System.out.println(rs);

            //iterate the ResultSet
            if(rs.next()){
                foundUser = new User();
                foundUser.setId(rs.getString("id"));
                foundUser.setUsername(rs.getString("username"));
                foundUser.setEmail(rs.getString("email"));
                foundUser.setPassword(rs.getString("password"));
                foundUser.setGivenName(rs.getString("given_name"));
                foundUser.setSurName(rs.getString("surname"));
                foundUser.setIsActive(rs.getBoolean("is_active"));
                foundUser.setRoleId(rs.getString("role_id"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return foundUser;
    }

    // READ ALL
    @Override
    public User[] getAll() {

        ArrayList<User> allUsers = new ArrayList<>();

        // todo learn how to store many users inside allUsers

        return null;
    }


    // UPDATE
    @Override
    public void update(User updatedObject) {

    }

    // DELETE
    @Override
    public void deleteById(String Id) {

    }


    // AUTH (READ ONE)
    public User findUserByUsernameAndPassword(String username, String password){

        User authUser = null;

        // try to make a connection
        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            //prepared statement
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            // pass username and password to pstmt
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            //store the query in ResultSet
            ResultSet rs = pstmt.executeQuery();

            //iterate the ResultSet
            if(rs.next()){
                authUser = new User();
                authUser.setId(rs.getString("id"));
                authUser.setUsername(rs.getString("username"));
                authUser.setEmail(rs.getString("email"));
                authUser.setPassword(rs.getString("password"));
                authUser.setGivenName(rs.getString("given_name"));
                authUser.setSurName(rs.getString("surname"));
                authUser.setIsActive(rs.getBoolean("is_active"));
                authUser.setRoleId(rs.getString("role_id"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return authUser;
    }


    public ArrayList<User> getUsersByRole(String role_id) {

        ArrayList<User> roleUsers = new ArrayList<>();

        // todo learn how to store many users inside ArrayList

        return null;
    }
}


