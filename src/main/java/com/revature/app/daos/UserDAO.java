package com.revature.app.daos;

import com.revature.app.models.User;
import com.revature.app.models.UserRole;
import com.revature.app.util.ConnectionFactory;
import com.revature.app.util.exceptions.DataSourceException;
import com.revature.app.util.exceptions.ResourcePersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO  implements CrudDAO<User>{

    private final String rootSelect =
                    "SELECT u.id, u.given_name, u.surname, u.email, u.username, u.password, u.role_id, ur.role, " +
                            "u.is_active FROM users u JOIN user_roles ur ON u.role_id = ur.id ";

    //=============================================================================================================
    //      BASIC CRUD METHODS
    //=============================================================================================================


    // ***************************************
    //  CREATE A USER
    // ***************************************
    @Override
    public void save(User newUser) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users VALUES (?,?,?,?,?,?,?,?)");

            pstmt.setString(1, newUser.getId());
            pstmt.setString(2, newUser.getUsername());
            pstmt.setString(3, newUser.getEmail());
            pstmt.setString(4, newUser.getPassword());
            pstmt.setString(5, newUser.getGivenName());
            pstmt.setString(6, newUser.getSurname());
            pstmt.setBoolean(7, newUser.getIsActive());
            pstmt.setString(8, newUser.getRole().getId());

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 1){
                throw new ResourcePersistenceException("Failed to persist user in database");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    // ***************************************
    //  READ ONE USER BY ID
    // ***************************************
    @Override
    public User getById(String id) {

        User foundUser = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement(rootSelect + "WHERE id=?");

            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                foundUser = new User();
                foundUser.setId(rs.getString("id"));
                foundUser.setUsername(rs.getString("username"));
                foundUser.setEmail(rs.getString("email"));
                foundUser.setPassword(rs.getString("password"));
                foundUser.setGivenName(rs.getString("given_name"));
                foundUser.setSurname(rs.getString("surname"));
                foundUser.setIsActive(rs.getBoolean("is_active"));
                foundUser.setRole(new UserRole(rs.getString("role_id"),rs.getString("role")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return foundUser;
    }


    // ***************************************
    //  READ ALL USERS
    // ***************************************
    @Override
    public List<User> getAll() {

        List<User> allUsers = new ArrayList<User>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            ResultSet rs = conn.createStatement().executeQuery(rootSelect);
            while (rs.next()) {
                User oneUser = new User();
                oneUser.setId(rs.getString("id"));
                oneUser.setUsername(rs.getString("username"));
                oneUser.setEmail(rs.getString("email"));
                oneUser.setPassword(rs.getString("password"));
                oneUser.setGivenName(rs.getString("given_name"));
                oneUser.setSurname(rs.getString("surname"));
                oneUser.setIsActive(rs.getBoolean("is_active"));
                oneUser.setRole(new UserRole(rs.getString("role_id"),rs.getString("role")));
                allUsers.add(oneUser);
            }
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }

        return allUsers;
    }


    // ***************************************
    //  UPDATE A USER BY ID
    // ***************************************
    @Override
    public void update(User updatedUser) {


        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE users SET username=?, email=?, password=?, given_name=?, surname=?, is_active=?, " +
                            "role_id=? WHERE id=?"
            );

            pstmt.setString(1, updatedUser.getUsername());
            pstmt.setString(2, updatedUser.getEmail());
            pstmt.setString(3, updatedUser.getPassword());
            pstmt.setString(4, updatedUser.getGivenName());
            pstmt.setString(5, updatedUser.getSurname());
            pstmt.setBoolean(6, updatedUser.getIsActive());
            pstmt.setString(7, updatedUser.getRole().getId());
            pstmt.setString(8, updatedUser.getId());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted != 1){
                throw new ResourcePersistenceException("Failed to update user in database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // ***************************************
    //  DELETE USER BY ID
    // ***************************************
    @Override
    public void deleteById(String id) {
        User deletedUser = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE id=?");

            pstmt.setString(1, id);

            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted != 1){
                throw new ResourcePersistenceException("Failed to delete user from database");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }



    //=============================================================================================================
    //  CLASS SPECIFIC QUERY METHODS
    //=============================================================================================================

    // ***************************************
    //  GET USER BY LOGIN CREDENTIALS
    // ***************************************
    public User findUserByUsernameAndPassword(String username, String password){

        User authUser = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement(rootSelect+"WHERE u.username=? AND u.password=?");

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                authUser = new User();
                authUser.setId(rs.getString("id"));
                authUser.setUsername(rs.getString("username"));
                authUser.setEmail(rs.getString("email"));
                authUser.setPassword(rs.getString("password"));
                authUser.setGivenName(rs.getString("given_name"));
                authUser.setSurname(rs.getString("surname"));
                authUser.setIsActive(rs.getBoolean("is_active"));
                authUser.setRole(new UserRole(rs.getString("id"), rs.getString("role")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return authUser;
    }

    // ***************************************
    //  GET USERS BY ROLE_ID
    // ***************************************
    public ArrayList<User> getUsersByRole(String role_id) {

        ArrayList<User> roleUsers = new ArrayList<>();

        // todo learn how to store many users inside ArrayList

        return null;
    }

    // ***************************************
    //  GET USER BY USERNAME
    // ***************************************
    public User findUserByUsername(String username){

        User foundUser = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement(rootSelect+"WHERE username=?");

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                foundUser = new User();
                foundUser.setId(rs.getString("id"));
                foundUser.setUsername(rs.getString("username"));
                foundUser.setEmail(rs.getString("email"));
                foundUser.setPassword(rs.getString("password"));
                foundUser.setGivenName(rs.getString("given_name"));
                foundUser.setSurname(rs.getString("surname"));
                foundUser.setIsActive(rs.getBoolean("is_active"));
                foundUser.setRole(new UserRole(rs.getString("id"), rs.getString("role")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return foundUser;
    }

    // ***************************************
    //  GET USER BY USERNAME
    // ***************************************
    public User findUserByEmail(String email){

        User foundUser = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement(rootSelect+"WHERE email=?");

            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                foundUser = new User();
                foundUser.setId(rs.getString("id"));
                foundUser.setUsername(rs.getString("username"));
                foundUser.setEmail(rs.getString("email"));
                foundUser.setPassword(rs.getString("password"));
                foundUser.setGivenName(rs.getString("given_name"));
                foundUser.setSurname(rs.getString("surname"));
                foundUser.setIsActive(rs.getBoolean("is_active"));
                foundUser.setRole(new UserRole(rs.getString("id"), rs.getString("role")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return foundUser;
    }
}


