package com.revature.app.dtos.responses;

import com.revature.app.models.User;

// will hold authenticated user information
public class Principal {

    private String id;
    private String username;

    // todo make this enum
    private String role_id;

    public Principal(){
    }

    public Principal(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role_id = user.getRoleId();
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

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role_id='" + role_id + '\'' +
                '}';
    }
}
