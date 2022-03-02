package com.revature.app.dtos.requests;

import com.revature.app.models.Reimbursement;

public class NewReimbursementRequest {

    private float amount;
    private String description;
    private String authorId;
    private String typeId;

    public NewReimbursementRequest(){

    }

    public NewReimbursementRequest(float amount, String description, String authorId, String typeId) {
        this.amount = amount;
        this.description = description;
        this.authorId = authorId;
        this.typeId = typeId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Reimbursement extractReimbursement(){
        return new Reimbursement(amount, description, authorId, typeId);
    }


}
