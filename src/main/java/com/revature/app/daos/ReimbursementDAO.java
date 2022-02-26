package com.revature.app.daos;

import com.revature.app.models.Reimbursement;
import com.revature.app.models.User;
import com.revature.app.util.ConnectionFactory;
import com.revature.app.util.exceptions.ResourcePersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReimbursementDAO implements CrudDAO<Reimbursement> {

    //=============================================================================================================
    //      BASIC CRUD METHODS
    //=============================================================================================================


    // ***************************************
    //  CREATE A REIMBURSEMENT
    // ***************************************
    @Override
    public void save(Reimbursement newObject) {

        Reimbursement reimbursement = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement("" +
                    "INSERT INTO reimbursements VALUES (?,?,?,?,?,?,?,?,?)");

            pstmt.setFloat(1, reimbursement.getAmount());
            pstmt.setTimestamp(2, reimbursement.getSubmitted());
            pstmt.setTimestamp(3, reimbursement.getResolved());
            pstmt.setString(4, reimbursement.getDescription());
            pstmt.setBlob(5, reimbursement.getReceipt());
            pstmt.setString(6, reimbursement.getPaymentId());
            pstmt.setString(7, reimbursement.getAuthorId());
            pstmt.setString(8, reimbursement.getResolverId());
            pstmt.setString(9, reimbursement.getStatusId());
            pstmt.setString(10, reimbursement.getTypeId());

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 1){
                throw new ResourcePersistenceException("Failed to persist reimbursement to data source");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

    }


    // ***************************************
    //  READ ONE REIMBURSEMENT BY ID
    // ***************************************
    @Override
    public Reimbursement getById(String id) {

        Reimbursement foundReimbursement = null;

        try(Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM reimbursements WHERE id=?");
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                pstmt.setFloat(1, foundReimbursement.getAmount());
                pstmt.setTimestamp(2, foundReimbursement.getSubmitted());
                pstmt.setTimestamp(3, foundReimbursement.getResolved());
                pstmt.setString(4, foundReimbursement.getDescription());
                pstmt.setBlob(5, foundReimbursement.getReceipt());
                pstmt.setString(6, foundReimbursement.getPaymentId());
                pstmt.setString(7, foundReimbursement.getAuthorId());
                pstmt.setString(8, foundReimbursement.getResolverId());
                pstmt.setString(9, foundReimbursement.getStatusId());
                pstmt.setString(10, foundReimbursement.getTypeId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foundReimbursement;
    }

    // ***************************************
    //  READ ALL REIMBURSEMENTS
    // ***************************************
    @Override
    public ArrayList<Reimbursement> getAll() {
        ArrayList<Reimbursement> allReimbursements = new ArrayList<>();

        // todo learn how to store many users inside allReimbursements

        return null;
    }


    // ***************************************
    //  UPDATE A REIMBURSEMENT BY ID
    // ***************************************
    @Override
    public void update(Reimbursement updatedReimbursement) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE reimbursements SET amount=?, submitted=?, resolved=?, description=?, receipt=?, " +
                            "payment_id=?, author_id=?, resolver_id=?, status_id=?, type_id=? WHERE id=?"
            );

            pstmt.setFloat(1, updatedReimbursement.getAmount());
            pstmt.setTimestamp(2, updatedReimbursement.getSubmitted());
            pstmt.setTimestamp(3, updatedReimbursement.getResolved());
            pstmt.setString(4, updatedReimbursement.getDescription());
            pstmt.setBlob(5, updatedReimbursement.getReceipt());
            pstmt.setString(6, updatedReimbursement.getPaymentId());
            pstmt.setString(7, updatedReimbursement.getAuthorId());
            pstmt.setString(8, updatedReimbursement.getResolverId());
            pstmt.setString(9, updatedReimbursement.getStatusId());
            pstmt.setString(10, updatedReimbursement.getTypeId());
            pstmt.setString(11, updatedReimbursement.getId());


            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted != 1){
                throw new ResourcePersistenceException("Failed to update user in database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




    // DELETE
    @Override
    public void deleteById(String id) {
        User deletedUser = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM reimbursements WHERE id=?");

            pstmt.setString(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted != 1){
                throw new ResourcePersistenceException("Failed to delete reimbursement from database");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
