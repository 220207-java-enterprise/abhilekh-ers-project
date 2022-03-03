package com.revature.app.services;

import com.revature.app.daos.ReimbursementDAO;
import com.revature.app.daos.ReimbursementStatusDAO;
import com.revature.app.daos.ReimbursementTypeDAO;
import com.revature.app.daos.UserDAO;
import com.revature.app.dtos.requests.NewReimbursementRequest;
import com.revature.app.dtos.requests.UpdateReimbursementRequest;
import com.revature.app.dtos.responses.GetUserResponse;
import com.revature.app.dtos.responses.ReimbursementResponse;
import com.revature.app.models.*;

import java.util.ArrayList;
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

    // *********************************
    //      UPDATE A REIMBURSEMENT
    // *********************************
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


        reimbursementDAO.update(updatedReimbursement);

        return updatedReimbursement;
    }
}
