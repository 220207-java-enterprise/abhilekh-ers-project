package com.revature.app.dtos;


// we will create json responses with id of new objects
public class ResourceCreationResponse {

    private String id;

    public ResourceCreationResponse(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
