package com.revature.app.services;

import com.revature.app.daos.ReimbursementDAO;
import com.revature.app.daos.ReimbursementStatusDAO;
import com.revature.app.daos.ReimbursementTypeDAO;
import com.revature.app.daos.UserDAO;
import com.revature.app.dtos.requests.NewReimbursementRequest;
import com.revature.app.dtos.requests.ManageMyReimbursementRequest;
import com.revature.app.dtos.requests.UpdateReimbursementRequest;
import com.revature.app.dtos.responses.Principal;
import com.revature.app.dtos.responses.ReimbursementResponse;
import com.revature.app.models.*;
import com.revature.app.util.exceptions.InvalidRequestException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReimbursementService {

    private ReimbursementDAO reimbursementDAO;
    private ReimbursementTypeDAO reimbursementTypeDAO;
    private ReimbursementStatusDAO reimbursementStatusDAO;
    private UserDAO userDAO;

    private PrismService prismService;

    public ReimbursementService(ReimbursementDAO reimbursementDAO,
                                ReimbursementTypeDAO reimbursementTypeDAO,
                                ReimbursementStatusDAO reimbursementStatusDAO,
                                UserDAO userDAO
                                ){
        this.reimbursementDAO = reimbursementDAO;
        this.reimbursementTypeDAO = reimbursementTypeDAO;
        this.reimbursementStatusDAO = reimbursementStatusDAO;
        this.userDAO = userDAO;
    }

    // ***********************************
    //      ADD A NEW REIMBURSEMENT
    // ***********************************
    public Reimbursement addNewReimbursement(NewReimbursementRequest newReimbursementRequest){

        Reimbursement newReimbursement = newReimbursementRequest.extractReimbursement();

        // add/change some properties before saving to database
        newReimbursement.setId(UUID.randomUUID().toString());

        if (newReimbursement.getAmount() < 0){
            throw new InvalidRequestException();
        }

        newReimbursement.setAmount(
                Float.parseFloat(String.format(
                        String.format("%6.2f", newReimbursementRequest.getAmount()
                ))));

        newReimbursement.setStatusId("1");

        ReimbursementType type = reimbursementTypeDAO.getByName(newReimbursementRequest.getTypeName());
        newReimbursement.setType(type);

        reimbursementDAO.save(newReimbursement);

        return newReimbursement;
    }

    // ***********************************
    //      GET ONE REIMBURSEMENT BY ID
    // ***********************************
    public Reimbursement getReimbursementById(String id){
        return reimbursementDAO.getById(id);
    }

    // ***********************************
    //      GET ALL REIMBURSEMENTS
    // ***********************************
    public List<ReimbursementResponse> getAllReimbursements(){
        // **********************
        // MAPPING USING STREAMS        //todo order allUsers using comparable/comparators
        //***********************
        return reimbursementDAO.getAll()
                .stream()
                .map(ReimbursementResponse::new)
                .collect(Collectors.toList());
    }

    // ***********************************************
    //      UPDATE A REIMBURSEMENT AS FINANCE MANAGER
    // ***********************************************
    public Reimbursement updateReimbursement(UpdateReimbursementRequest updateReimbursementRequest){

//        updateReimbursementRequest.setResolverId();
        Reimbursement updatedReimbursement = reimbursementDAO.getById(updateReimbursementRequest.getId());

        updatedReimbursement.setResolver(userDAO.getById(updateReimbursementRequest.getResolverId()));

        if (updateReimbursementRequest.getStatusName()!=null){
            if (updateReimbursementRequest.getStatusName().equals("APPROVED")) updatedReimbursement.setStatus(new ReimbursementStatus("2",
                    "APPROVED"));
            else if (updateReimbursementRequest.getStatusName().equals("DENIED")) updatedReimbursement.setStatus(new ReimbursementStatus("3",
                    "DENIED"));
        }

        System.out.println("SENDING THIS ---> "+updatedReimbursement);

        reimbursementDAO.update(updatedReimbursement);

        return updatedReimbursement;
    }

    // *********************************
    //      UPDATE A REIMBURSEMENT
    // *********************************
    public Reimbursement manageMyReimbursement(ManageMyReimbursementRequest manageMyReimbursementRequest){

        Reimbursement updatedReimbursement = manageMyReimbursementRequest.extractReimbursement();

        if (manageMyReimbursementRequest.getAmount()==null && updatedReimbursement.getDescription()==null){
            Reimbursement viewReimbursement = reimbursementDAO.getById(manageMyReimbursementRequest.getId());
            return viewReimbursement;
        }

        updatedReimbursement.setAmount(
                Float.parseFloat(String.format(
                        String.format("%6.2f", manageMyReimbursementRequest.getAmount()
                        ))));

        updatedReimbursement.setStatus(new ReimbursementStatus("1","PENDING"));

        reimbursementDAO.updateMy(updatedReimbursement);

        return updatedReimbursement;
    }

    // ************************************
    //      GET ALL PENDING REIMBURSEMENTS
    // ************************************
    public List<ReimbursementResponse> getAllPendingReimbursements(){
        // **********************
        // MAPPING USING STREAMS
        //***********************
        return reimbursementDAO.getAllPending()
                .stream()
                .map(ReimbursementResponse::new)
                .collect(Collectors.toList());
    }

    // ************************************
    //      GET ALL ACCEPTED REIMBURSEMENTS
    // ************************************
    public List<ReimbursementResponse> getAllAcceptedReimbursements(){
        // **********************
        // MAPPING USING STREAMS
        //***********************
        return reimbursementDAO.getAllAccepted()
                .stream()
                .map(ReimbursementResponse::new)
                .collect(Collectors.toList());
    }

    // ************************************
    //      GET ALL DENIED REIMBURSEMENTS
    // ************************************
    public List<ReimbursementResponse> getAllDeniedReimbursements(){
        // **********************
        // MAPPING USING STREAMS
        //***********************
        return reimbursementDAO.getAllDenied()
                .stream()
                .map(ReimbursementResponse::new)
                .collect(Collectors.toList());
    }

    // ********************************************************
    //      IS EMPLOYEE AUTHORIZED TO EDIT THIS REIMBURSEMENT?
    // ********************************************************
    public boolean isAuthorizedToEdit(Reimbursement reimbursement, Principal requester){
        if (reimbursementDAO.getById(reimbursement.getId()).getAuthor().getId().equals(requester.getId())){
            return true;
        }
        return false;
    }

}
