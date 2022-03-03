package com.revature.app.daos;

import com.revature.app.models.*;
import com.revature.app.util.ConnectionFactory;
import com.revature.app.util.exceptions.DataSourceException;
import com.revature.app.util.exceptions.ResourcePersistenceException;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDAO implements CrudDAO<Reimbursement> {

    //=============================================================================================================
    //      BASIC CRUD METHODS
    //=============================================================================================================

    private UserDAO userDAO = new UserDAO();

    private final String rootSelect = "SELECT rmb.id, rmb.amount, rmb.submitted, rmb.resolved, rmb.description, rmb.receipt, rmb.payment_id,\n" +
            "rmb.author_id, rmb.resolver_id, rmb.status_id, rmb.type_id, rs.status, rt.type \n" +
            "FROM reimbursements rmb\n" +
            "JOIN reimbursement_statuses rs\n" +
            "ON rmb.status_id = rs.id\n" +
            "JOIN reimbursement_types rt\n" +
            "ON rmb.type_id = rt.id;";

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
            pstmt.setString(11, newReimbursement.getType().getId());

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
    public List<Reimbursement> getAll() {

        List<Reimbursement> allReimbursements = new ArrayList<>();
        Reimbursement oneReimbursement = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            ResultSet rs = conn.createStatement().executeQuery(rootSelect);
            while(rs.next()) {
                oneReimbursement = new Reimbursement();
                oneReimbursement.setId(rs.getString("id"));
                oneReimbursement.setAmount(rs.getFloat("amount"));
                oneReimbursement.setSubmitted(rs.getTimestamp("submitted"));
                oneReimbursement.setResolved(rs.getTimestamp("resolved"));
                oneReimbursement.setDescription(rs.getString("description"));
                oneReimbursement.setReceipt(rs.getString("receipt"));
                oneReimbursement.setPaymentId(rs.getString("payment_id"));
                oneReimbursement.setAuthor(userDAO.getById(rs.getString("author_id")));
                oneReimbursement.setResolver(userDAO.getById(rs.getString("resolver_id")));
                oneReimbursement.setStatus(new ReimbursementStatus(rs.getString("status_id"),
                        rs.getString("status")));
                oneReimbursement.setType(new ReimbursementType(rs.getString("type_id"),
                        rs.getString("type")));

                allReimbursements.add(oneReimbursement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }

        return allReimbursements;
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
