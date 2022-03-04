package com.revature.app.dtos.requests;

public class GetUserReimbursementRequest {

    String authorId;

    public GetUserReimbursementRequest(){}

    public GetUserReimbursementRequest(String authorId){
        this.authorId = authorId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "GetUserReimbursementRequest{" +
                "authorId='" + authorId + '\'' +
                '}';
    }
}
