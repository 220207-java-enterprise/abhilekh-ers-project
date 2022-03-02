package com.revature.app.dtos.responses;

import java.util.Date;

public class ReimbursementResponse {

    private String id;
    private float amount;
    private String description;
    private Date submitted;
    private Date resolved;
    private String payment_id;
    private String author;
    private String resolver;
    private String status;
    private String type;

    public ReimbursementResponse(){};

    public ReimbursementResponse(String id, float amount, String description, Date submitted, Date resolved, String payment_id, String author, String resolver, String status, String type) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.submitted = submitted;
        this.resolved = resolved;
        this.payment_id = payment_id;
        this.author = author;
        this.resolver = resolver;
        this.status = status;
        this.type = type;
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

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
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
