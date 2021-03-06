package com.revature.app.models;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Objects;

public class Reimbursement {

    private String id;
    private Float amount;
    private Timestamp submitted;
    private Timestamp resolved;
    private String description;
    private String receipt;
    private String paymentId;

    private String authorId;
    private String resolverId;
    private String statusId;

    private String typeId;
    private String typeName;

    private User author;
    private User resolver;
    private ReimbursementStatus status;
    private ReimbursementType type;

    public Reimbursement(){
    }


    public Reimbursement(Float amount, String description, String authorId, String typeId) {
        this.amount = amount;
        this.description = description;
        this.authorId = authorId;
        this.typeId = typeId;
    }

    public Reimbursement(String id, Float amount, String description, String typeName) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.typeName = typeName;
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

    public Timestamp getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    public Timestamp getResolved() {
        return resolved;
    }

    public void setResolved(Timestamp resolved) {
        this.resolved = resolved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getResolverId() {
        return resolverId;
    }

    public void setResolverId(String resolverId) {
        this.resolverId = resolverId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getResolver() {
        return resolver;
    }

    public void setResolver(User resolver) {
        this.resolver = resolver;
    }

    public ReimbursementStatus getStatus() {
        return status;
    }

    public void setStatus(ReimbursementStatus status) {
        this.status = status;
    }

    public ReimbursementType getType() {
        return type;
    }

    public void setType(ReimbursementType type) {
        this.type = type;
    }


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reimbursement that = (Reimbursement) o;
        return Objects.equals(id, that.id) && Objects.equals(amount, that.amount) && Objects.equals(submitted, that.submitted) && Objects.equals(resolved, that.resolved) && Objects.equals(description, that.description) && Objects.equals(receipt, that.receipt) && Objects.equals(paymentId, that.paymentId) && Objects.equals(authorId, that.authorId) && Objects.equals(resolverId, that.resolverId) && Objects.equals(statusId, that.statusId) && Objects.equals(typeId, that.typeId) && Objects.equals(typeName, that.typeName) && Objects.equals(author, that.author) && Objects.equals(resolver, that.resolver) && Objects.equals(status, that.status) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, submitted, resolved, description, receipt, paymentId, authorId, resolverId, statusId, typeId, typeName, author, resolver, status, type);
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", submitted=" + submitted +
                ", resolved=" + resolved +
                ", description='" + description + '\'' +
                ", receipt='" + receipt + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", authorId='" + authorId + '\'' +
                ", resolverId='" + resolverId + '\'' +
                ", statusId='" + statusId + '\'' +
                ", typeId='" + typeId + '\'' +
                ", typeName='" + typeName + '\'' +
                ", author=" + author +
                ", resolver=" + resolver +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
