package com.revature.app.dtos.requests;

public class UpdateReimbursementRequest {

    // admin can only approve/deny Reimbursement

    private String id;
    private String statusName;
    private String resolverId;

    private UpdateReimbursementRequest(){

    }

    private UpdateReimbursementRequest(String id, String statusName, String resolverId){
        this.id = id;
        this.statusName = statusName;
        this.resolverId = resolverId;
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
}
