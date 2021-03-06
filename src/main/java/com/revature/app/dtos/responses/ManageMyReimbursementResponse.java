package com.revature.app.dtos.responses;


import com.revature.app.models.Reimbursement;

import java.sql.Timestamp;

public class ManageMyReimbursementResponse {

    private String id;
    private Float amount;
    private String description;
    private Timestamp submitted;
    private String statusName;
    private String typeName;

    public ManageMyReimbursementResponse(){}

    public ManageMyReimbursementResponse(Reimbursement reimbursement){
        this.id = reimbursement.getId();
        this.amount = reimbursement.getAmount();
        this.description = reimbursement.getDescription();
        //this.submitted = reimbursement.getSubmitted();
        this.statusName = reimbursement.getStatus().getStatus();
        this.typeName = reimbursement.getType().getType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
