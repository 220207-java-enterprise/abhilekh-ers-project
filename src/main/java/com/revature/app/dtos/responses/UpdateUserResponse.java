package com.revature.app.dtos.responses;

import com.revature.app.models.User;

// will obfuscate email and password for privacy reason
public class UpdateUserResponse {

    private String id;
    private String givenName;
    private String surname;
    private String username;
    private String role;

    public UpdateUserResponse(){
        super();
    }

    public UpdateUserResponse(User user){
        this.id = user.getId();
        this.givenName = user.getGivenName();
        this.surname = user.getSurname();
        this.username = user.getUsername();
        this.role = user.getRole().getRoleName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id='" + id + '\'' +
                ", givenName='" + givenName + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

