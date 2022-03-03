package com.revature.app.dtos.responses;

import com.revature.app.models.Reimbursement;
import com.revature.app.models.ReimbursementStatus;
import com.revature.app.models.ReimbursementType;
import com.revature.app.models.User;

import java.util.Date;

public class ReimbursementResponse {

    private String id;
    private float amount;
    private String description;
    private Date submitted;
    private Date resolved;
    private String paymentId;
    private String author;
    private String resolver;
    private String status;
    private String type;

    public ReimbursementResponse(){};

    public ReimbursementResponse(Reimbursement reimbursement) {
        this.id = reimbursement.getId();
        this.amount = reimbursement.getAmount();
        this.description = reimbursement.getDescription();
        this.submitted = reimbursement.getSubmitted();
        this.resolved = reimbursement.getResolved();
        this.paymentId = reimbursement.getPaymentId();
        this.author = reimbursement.getAuthor().getUsername();
//        this.resolver = reimbursement.getResolver().getUsername();
        this.status = reimbursement.getStatus().getStatus();
        this.type = reimbursement.getType().getType();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }

    public Date getResolved() {
        return resolved;
    }

    public void setResolved(Date resolved) {
        this.resolved = resolved;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
