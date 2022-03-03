package com.revature.app.dtos.responses;

public class ResourceUpdateResponse {

    private String id;

    public ResourceUpdateResponse(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
