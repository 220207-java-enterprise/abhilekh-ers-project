package com.revature.app.dtos.requests;

import com.revature.app.models.Reimbursement;

import java.sql.Timestamp;

public class UpdateMyReimbursementRequest {

    private String id;
    private Float amount;
    private String description;


    public UpdateMyReimbursementRequest(){}

    public UpdateMyReimbursementRequest(String id, Float amount, String description){
        this.id = id;
        this.amount = amount.floatValue();
        this.description = description;
    }

    public Reimbursement extractReimbursement(){
        return new Reimbursement(id, amount, description);
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

}
