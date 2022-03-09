package com.revature.app.dtos.requests;

import com.revature.app.models.Reimbursement;

public class NewReimbursementRequest {

    private float amount;
    private String description;
    private String authorId;    // todo use jwt to extract id -> in servlet maybe
    private String typeName;      // todo maybe type name?

    public NewReimbursementRequest(){

    }

    public NewReimbursementRequest(float amount, String description, String authorId, String typeName) {
        this.amount = amount;
        this.description = description;
        this.authorId = authorId;
        this.typeName = typeName;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Reimbursement extractReimbursement(){
        return new Reimbursement(amount, description, authorId, typeName);
    }

    @Override
    public String toString() {
        return "NewReimbursementRequest{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                ", authorId='" + authorId + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
