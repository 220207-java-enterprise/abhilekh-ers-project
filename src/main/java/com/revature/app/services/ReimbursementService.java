package com.revature.app.services;

import com.revature.app.daos.ReimbursementDAO;
import com.revature.app.daos.ReimbursementStatusDAO;
import com.revature.app.daos.ReimbursementTypeDAO;
import com.revature.app.daos.UserDAO;
import com.revature.app.dtos.requests.NewReimbursementRequest;
import com.revature.app.dtos.responses.ReimbursementResponse;
import com.revature.app.models.Reimbursement;
import com.revature.app.models.ReimbursementType;
import com.revature.app.models.User;

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

        Reimbursement newReimbursemant = newReimbursementRequest.extractReimbursement();

        // add/change some properties before saving to database
        newReimbursemant.setId(UUID.randomUUID().toString());

        newReimbursemant.setAmount(
                Float.parseFloat(String.format(
                        String.format("%6.2f", newReimbursementRequest.getAmount()
                ))));

        newReimbursemant.setStatusId("1");

        ReimbursementType type = reimbursementTypeDAO.getById(newReimbursementRequest.getTypeId());
        // todo maybe type name would be easier

        newReimbursemant.setType(type);

        reimbursementDAO.save(newReimbursemant);

        return newReimbursemant;
    }

    // ***********************************
    //      GET ALL REIMBURSEMENTS
    // ***********************************
    public List<ReimbursementResponse> getAllReimbursements(){
        List<Reimbursement> reimbursements = reimbursementDAO.getAll();
        List<ReimbursementResponse> reimbursementResponses = new ArrayList<>();
        for (Reimbursement reimbursement : reimbursements){
            reimbursementResponses.add(new ReimbursementResponse(reimbursement));
        }
        return reimbursementResponses;
    }
}
