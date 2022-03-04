package com.revature.app.dtos.requests;


public class RecallReimbursementRequest {

    String id;

    public RecallReimbursementRequest(){}
    public RecallReimbursementRequest(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RecallReimbursementRequest{" +
                "id='" + id + '\'' +
                '}';
    }
}
