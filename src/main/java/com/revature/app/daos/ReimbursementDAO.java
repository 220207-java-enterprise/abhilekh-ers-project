package com.revature.app.daos;

import com.revature.app.models.Reimbursement;
import com.revature.app.models.User;
import com.revature.app.util.ConnectionFactory;
import com.revature.app.util.exceptions.ResourcePersistenceException;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;

public class ReimbursementDAO implements CrudDAO<Reimbursement> {

    //=============================================================================================================
    //      BASIC CRUD METHODS
    //=============================================================================================================


    // ***************************************
    //  CREATE A REIMBURSEMENT
    // ***************************************
    @Override
    public void save(Reimbursement newReimbursement) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO reimbursements VALUES (?,?,?,?,?,?,?,?,?,?,?)");

            pstmt.setString(1, newReimbursement.getId());
            pstmt.setFloat(2, newReimbursement.getAmount());
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setTimestamp(4, newReimbursement.getResolved());
            pstmt.setString(5, newReimbursement.getDescription());
            pstmt.setString(6, newReimbursement.getReceipt());
            pstmt.setString(7, newReimbursement.getPaymentId());
            pstmt.setString(8, newReimbursement.getAuthorId());
            pstmt.setString(9, newReimbursement.getResolverId());
            pstmt.setString(10, newReimbursement.getStatusId());
            pstmt.setString(11, newReimbursement.getTypeId());

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
                pstmt.setString(5, foundReimbursement.getReceipt());
                pstmt.setString(6, foundReimbursement.getPaymentId());
                pstmt.setString(7, foundReimbursement.getAuthor().getId());
                pstmt.setString(8, foundReimbursement.getResolver().getId());
                pstmt.setString(9, foundReimbursement.getStatus().getId());
                pstmt.setString(10, foundReimbursement.getType().getId());
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
            pstmt.setString(5, updatedReimbursement.getReceipt());
            pstmt.setString(6, updatedReimbursement.getPaymentId());
            pstmt.setString(7, updatedReimbursement.getAuthor().getId());
            pstmt.setString(8, updatedReimbursement.getResolver().getId());
            pstmt.setString(9, updatedReimbursement.getStatus().getId());
            pstmt.setString(10, updatedReimbursement.getType().getId());
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
