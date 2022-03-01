package com.revature.app.dtos.responses;

import com.revature.app.models.User;

// will hold authenticated user with just enough information we need to handle future requests
public class Principal {

    private String id;
    private String username;

    private String role;

    public Principal(){
    }

    public Principal(User authUser) {
        this.id = authUser.getId();
        this.username = authUser.getUsername();
        this.role = authUser.getRole().getRoleName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role_id) {
        this.role = role_id;
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role_id='" + role + '\'' +
                '}';
    }
}
