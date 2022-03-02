package com.revature.app.services;

import com.revature.app.daos.ReimbursementDAO;
import com.revature.app.daos.ReimbursementStatusDAO;
import com.revature.app.daos.ReimbursementTypeDAO;
import com.revature.app.daos.UserDAO;
import com.revature.app.dtos.requests.NewReimbursementRequest;
import com.revature.app.models.Reimbursement;
import com.revature.app.models.ReimbursementStatus;
import com.revature.app.models.ReimbursementType;
import com.revature.app.models.User;

import java.util.UUID;

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
        User author = userDAO.getById(newReimbursementRequest.getAuthorId());
        newReimbursemant.setAuthor(author);

        ReimbursementType type = reimbursementTypeDAO.getById(newReimbursementRequest.getTypeId());

        reimbursementDAO.save(newReimbursemant);

        return newReimbursemant;
    }
}
