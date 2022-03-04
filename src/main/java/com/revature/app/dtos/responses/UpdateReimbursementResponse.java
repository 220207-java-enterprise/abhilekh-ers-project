package com.revature.app.dtos.responses;

import com.revature.app.dtos.requests.UpdateReimbursementRequest;
import com.revature.app.models.Reimbursement;

import java.sql.Timestamp;

public class UpdateReimbursementResponse {

    private String id;
    private Timestamp resolved;
    private Float amount;
    private String description;
    private String resolverName;
    private String statusName;
    private String typeName;

    private UpdateReimbursementResponse(){}

    public UpdateReimbursementResponse(Reimbursement reimbursement) {
        this.id = reimbursement.getId();
        this.resolved = reimbursement.getResolved();
        this.amount = reimbursement.getAmount();
        this.description = reimbursement.getDescription();
        this.resolverName = reimbursement.getResolver().getUsername();
        this.statusName = reimbursement.getStatus().getStatus();
        this.typeName = reimbursement.getType().getType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getResolved() {
        return resolved;
    }

    public void setResolved(Timestamp resolved) {
        this.resolved = resolved;
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

    public String getResolverName() {
        return resolverName;
    }

    public void setResolverName(String resolverName) {
        this.resolverName = resolverName;
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
