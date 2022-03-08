package com.revature.app.daos;

import com.revature.app.models.UserRole;
import com.revature.app.util.ConnectionFactory;
import com.revature.app.util.exceptions.ResourcePersistenceException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class UserRoleDAO implements CrudDAO<UserRole> {


    //=============================================================================================================
    //      BASIC CRUD METHODS
    //=============================================================================================================


    // ***************************************
    //  CREATE A USER ROLE
    // ***************************************
    @Override
    public void save(UserRole newUserRole) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user_roles VALUES (?,?)");

            pstmt.setString(1, newUserRole.getId());
            pstmt.setString(2, newUserRole.getRoleName());

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 1){
                throw new ResourcePersistenceException("Failed to persist user role in database");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // ***************************************
    //  READ ONE USE ROLE BY ID
    // ***************************************
    @Override
    public UserRole getById(String id) {
        UserRole foundUserRole = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user_roles WHERE id=?");
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                foundUserRole = new UserRole();
                foundUserRole.setId(rs.getString("id"));
                foundUserRole.setRoleName(rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foundUserRole;
    }


    // ***************************************
    //  READ ALL USER ROLES
    // ***************************************
    @Override
    public ArrayList<UserRole> getAll() {

        ArrayList<UserRole> allUserRoles = new ArrayList<UserRole>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user_roles");

            UserRole oneUserRole = null;
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                oneUserRole = new UserRole();
                oneUserRole.setId(rs.getString("id"));
                oneUserRole.setRoleName(rs.getString("role"));

                allUserRoles.add(oneUserRole);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allUserRoles;
    }

    // ***************************************
    //  UPDATE A USER ROLE
    // ***************************************
    @Override
    public void update(UserRole updatedUserRole) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE user_roles SET role=? WHERE id=?");

            pstmt.setString(1, updatedUserRole.getRoleName());
            pstmt.setString(2, updatedUserRole.getId());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted != 1) {
                throw new ResourcePersistenceException("Failed to update user role in database");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ***************************************
    //  DELETE USER ROLE BY ID
    // ***************************************
    @Override
    public void deleteById(String id) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM user_roles WHERE id=?");

            pstmt.setString(1, id);

            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted != 1){
                throw new ResourcePersistenceException("Failed to delete user role from database");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


}

