package com.revature.app.daos;

import com.revature.app.models.*;
import com.revature.app.util.ConnectionFactory;
import com.revature.app.util.exceptions.DataSourceException;
import com.revature.app.util.exceptions.ResourcePersistenceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDAO implements CrudDAO<Reimbursement> {

    //=============================================================================================================
    //      BASIC CRUD METHODS
    //=============================================================================================================

    private UserDAO userDAO = new UserDAO();
    private ReimbursementStatusDAO reimbursementStatusDAO = new ReimbursementStatusDAO();
    private ReimbursementTypeDAO reimbursementTypeDAO = new ReimbursementTypeDAO();

    private final String rootSelect = "SELECT rmb.id, rmb.amount, rmb.submitted, rmb.resolved, rmb.description, rmb.receipt, rmb.payment_id,\n" +
            "rmb.author_id, rmb.resolver_id, rmb.status_id, rmb.type_id, rs.status, rt.type \n" +
            "FROM reimbursements rmb\n" +
            "JOIN reimbursement_statuses rs\n" +
            "ON rmb.status_id = rs.id\n" +
            "JOIN reimbursement_types rt\n" +
            "ON rmb.type_id = rt.id ";

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

            if(rs.next()) {
                foundReimbursement = new Reimbursement();
                foundReimbursement.setId(rs.getString("id"));
                foundReimbursement.setAmount(rs.getFloat("amount"));
                foundReimbursement.setSubmitted(rs.getTimestamp("submitted"));
                foundReimbursement.setResolved(rs.getTimestamp("resolved"));
                foundReimbursement.setDescription(rs.getString("description"));
                foundReimbursement.setReceipt(rs.getString("receipt"));
                foundReimbursement.setPaymentId(rs.getString("payment_id"));

                User author = userDAO.getById(rs.getString("author_id"));
                foundReimbursement.setAuthor(author);

                ReimbursementStatus status = reimbursementStatusDAO.getById(rs.getString("status_id"));
                foundReimbursement.setStatus(status);

                ReimbursementType type = reimbursementTypeDAO.getById(rs.getString("type_id"));
                foundReimbursement.setType(type);

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
                    "UPDATE reimbursements SET resolved=?, status_id=?, resolver_id=? WHERE id=?"
            );

            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(2, updatedReimbursement.getStatus().getId());
            pstmt.setString(3, updatedReimbursement.getResolver().getId());
            pstmt.setString(4, updatedReimbursement.getId());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted != 1){
                throw new ResourcePersistenceException("Failed to update user in database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    // ***************************************
    //  DELETE A REIMBURSEMENT BY ID
    // ***************************************
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


    // ***************************************
    //  GET ALL PENDING REIMBURSEMENTS
    // ***************************************
    public List<Reimbursement> getAllPending(){
        List<Reimbursement> allPendingReimbursements = new ArrayList<>();
        Reimbursement onePendingReimbursement = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            ResultSet rs = conn.createStatement().executeQuery(rootSelect + "WHERE rmb.status_id='1'");
            while(rs.next()) {
                onePendingReimbursement = new Reimbursement();
                onePendingReimbursement.setId(rs.getString("id"));
                onePendingReimbursement.setAmount(rs.getFloat("amount"));
                onePendingReimbursement.setSubmitted(rs.getTimestamp("submitted"));
                onePendingReimbursement.setResolved(rs.getTimestamp("resolved"));
                onePendingReimbursement.setDescription(rs.getString("description"));
                onePendingReimbursement.setReceipt(rs.getString("receipt"));
                onePendingReimbursement.setPaymentId(rs.getString("payment_id"));
                onePendingReimbursement.setAuthor(userDAO.getById(rs.getString("author_id")));
                onePendingReimbursement.setResolver(userDAO.getById(rs.getString("resolver_id")));
                onePendingReimbursement.setStatus(new ReimbursementStatus(rs.getString("status_id"),
                        rs.getString("status")));
                onePendingReimbursement.setType(new ReimbursementType(rs.getString("type_id"),
                        rs.getString("type")));

                allPendingReimbursements.add(onePendingReimbursement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }

        return allPendingReimbursements;
    }

    // ***************************************
    //  GET ALL ACCEPTED REIMBURSEMENTS
    // ***************************************
    public List<Reimbursement> getAllAccepted(){
        List<Reimbursement> allAcceptedReimbursements = new ArrayList<>();
        Reimbursement oneAcceptedReimbursement = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            ResultSet rs = conn.createStatement().executeQuery(rootSelect + "WHERE rmb.status_id='2'");
            while(rs.next()) {
                oneAcceptedReimbursement = new Reimbursement();
                oneAcceptedReimbursement.setId(rs.getString("id"));
                oneAcceptedReimbursement.setAmount(rs.getFloat("amount"));
                oneAcceptedReimbursement.setSubmitted(rs.getTimestamp("submitted"));
                oneAcceptedReimbursement.setResolved(rs.getTimestamp("resolved"));
                oneAcceptedReimbursement.setDescription(rs.getString("description"));
                oneAcceptedReimbursement.setReceipt(rs.getString("receipt"));
                oneAcceptedReimbursement.setPaymentId(rs.getString("payment_id"));
                oneAcceptedReimbursement.setAuthor(userDAO.getById(rs.getString("author_id")));
                oneAcceptedReimbursement.setResolver(userDAO.getById(rs.getString("resolver_id")));
                oneAcceptedReimbursement.setStatus(new ReimbursementStatus(rs.getString("status_id"),
                        rs.getString("status")));
                oneAcceptedReimbursement.setType(new ReimbursementType(rs.getString("type_id"),
                        rs.getString("type")));

                allAcceptedReimbursements.add(oneAcceptedReimbursement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }

        return allAcceptedReimbursements;
    }

    // ***************************************
    //  GET ALL DENIED REIMBURSEMENTS
    // ***************************************
    public List<Reimbursement> getAllDenied(){
        List<Reimbursement> allDeniedReimbursements = new ArrayList<>();
        Reimbursement oneDeniedReimbursement = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            ResultSet rs = conn.createStatement().executeQuery(rootSelect + "WHERE rmb.status_id='3'");
            while(rs.next()) {
                oneDeniedReimbursement = new Reimbursement();
                oneDeniedReimbursement.setId(rs.getString("id"));
                oneDeniedReimbursement.setAmount(rs.getFloat("amount"));
                oneDeniedReimbursement.setSubmitted(rs.getTimestamp("submitted"));
                oneDeniedReimbursement.setResolved(rs.getTimestamp("resolved"));
                oneDeniedReimbursement.setDescription(rs.getString("description"));
                oneDeniedReimbursement.setReceipt(rs.getString("receipt"));
                oneDeniedReimbursement.setPaymentId(rs.getString("payment_id"));
                oneDeniedReimbursement.setAuthor(userDAO.getById(rs.getString("author_id")));
                oneDeniedReimbursement.setResolver(userDAO.getById(rs.getString("resolver_id")));
                oneDeniedReimbursement.setStatus(new ReimbursementStatus(rs.getString("status_id"),
                        rs.getString("status")));
                oneDeniedReimbursement.setType(new ReimbursementType(rs.getString("type_id"),
                        rs.getString("type")));

                allDeniedReimbursements.add(oneDeniedReimbursement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }

        return allDeniedReimbursements;
    }

    // ***************************************
    //  UPDATE MY REIMBURSEMENT BY ID
    // ***************************************

    public void updateMy(Reimbursement updatedReimbursement) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE reimbursements SET submitted=?, amount=?, description=?, type_id= ?, status_id='1' WHERE" +
                            " id=?"
            );

            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setFloat(2, updatedReimbursement.getAmount());
            pstmt.setString(3, updatedReimbursement.getDescription());
            pstmt.setString(4, updatedReimbursement.getType().getId());
            pstmt.setString(5, updatedReimbursement.getId());

            System.out.println(pstmt);
            System.out.println("-------------------");
            System.out.println(updatedReimbursement);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted != 1){
                throw new ResourcePersistenceException("Failed to update user in database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




    // ******************************************
    //     GET REIMBURSEMENTS BY USER ID
    // ******************************************

    public List<Reimbursement> getReimbursementsByUserId(String authorId){

        List<Reimbursement> allUserReimbursements = new ArrayList<>();
        Reimbursement oneUserReimbursement = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM reimbursements WHERE author_id=?");
            pstmt.setString(1, authorId);


            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                oneUserReimbursement = new Reimbursement();
                oneUserReimbursement.setId(rs.getString("id"));
                oneUserReimbursement.setAmount(rs.getFloat("amount"));
                oneUserReimbursement.setSubmitted(rs.getTimestamp("submitted"));
                oneUserReimbursement.setResolved(rs.getTimestamp("resolved"));
                oneUserReimbursement.setDescription(rs.getString("description"));
                oneUserReimbursement.setReceipt(rs.getString("receipt"));
                oneUserReimbursement.setPaymentId(rs.getString("payment_id"));
                oneUserReimbursement.setAuthor(userDAO.getById(rs.getString("author_id")));
                oneUserReimbursement.setResolver(userDAO.getById(rs.getString("resolver_id")));
                oneUserReimbursement.setStatus(reimbursementStatusDAO.getById(rs.getString("status_id")));
                oneUserReimbursement.setType(reimbursementTypeDAO.getById(rs.getString("type_id")));

                allUserReimbursements.add(oneUserReimbursement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }

        return allUserReimbursements;
    }
}
