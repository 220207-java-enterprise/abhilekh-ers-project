package com.revature.app.dtos.requests;

public class UpdateReimbursementRequest {

    // admin can only approve/deny Reimbursement

    private String id;
    private String statusName;
    private String resolverId;

    private String authorId;

    private UpdateReimbursementRequest(){

    }

    private UpdateReimbursementRequest(String id, String statusName, String resolverId, String authorId){
        this.id = id;
        this.statusName = statusName;
        this.resolverId = resolverId;
        this.authorId = authorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getResolverId() {
        return resolverId;
    }

    public void setResolverId(String resolverId) {
        this.resolverId = resolverId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
