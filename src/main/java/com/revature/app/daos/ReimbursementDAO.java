package com.revature.app.daos;

import com.revature.app.models.Reimbursement;
import com.revature.app.util.ConnectionFactory;
import com.revature.app.util.exceptions.ResourcePersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReimbursementDAO implements CrudDAO<Reimbursement> {

    // CREATE
    @Override
    public void save(Reimbursement newObject) {

        Reimbursement reimbursement = null;

        // create connection
        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            //prepare statement
            PreparedStatement pstmt = conn.prepareStatement("" +
                    "INSERT INTO reimbursements VALUES (?,?,?,?,?,?,?,?,?)");

            //set ResultSet values to class attributes
            pstmt.setFloat(1, reimbursement.getAmount());
            pstmt.setTimestamp(2, reimbursement.getSubmitted());
            pstmt.setTimestamp(3, reimbursement.getResolved());
            pstmt.setString(4, reimbursement.getDescription());
            pstmt.setBlob(5, reimbursement.getBlob());
            pstmt.setString(6, reimbursement.getPaymentId());
            pstmt.setString(7, reimbursement.getResolverId());
            pstmt.setString(8, reimbursement.getStatusId());
            pstmt.setString(9, reimbursement.getTypeId());

            int rowsInserted = pstmt.executeUpdate(); // returns 1 if successful

            if (rowsInserted != 1){
                throw new ResourcePersistenceException("Failed to persist reimbursement to data source");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    // READ ONE
    @Override
    public Reimbursement getById(String id) {

        Reimbursement foundReimbursement = null;

        // create connection
        try(Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM reimbursements WHERE id=?");
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                //set ResultSet values to class attributes
                pstmt.setFloat(1, foundReimbursement.getAmount());
                pstmt.setTimestamp(2, foundReimbursement.getSubmitted());
                pstmt.setTimestamp(3, foundReimbursement.getResolved());
                pstmt.setString(4, foundReimbursement.getDescription());
                pstmt.setBlob(5, foundReimbursement.getBlob());
                pstmt.setString(6, foundReimbursement.getPaymentId());
                pstmt.setString(7, foundReimbursement.getResolverId());
                pstmt.setString(8, foundReimbursement.getStatusId());
                pstmt.setString(9, foundReimbursement.getTypeId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foundReimbursement;
    }

    // READ ALL
    @Override
    public Reimbursement[] getAll() {
        ArrayList<Reimbursement> allReimbursements = new ArrayList<>();

        // todo learn how to store many users inside allReimbursements

        return null;
    }


    // UPDATE
    @Override
    public void update(Reimbursement updatedObject) {

    }


    //DELETE
    @Override
    public void deleteById(String Id) {

    }
}
