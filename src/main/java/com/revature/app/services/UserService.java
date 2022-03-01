package com.revature.app.services;

import com.revature.app.daos.UserDAO;
import com.revature.app.dtos.requests.LoginRequest;
import com.revature.app.dtos.requests.NewUserRequest;
import com.revature.app.dtos.responses.UserResponse;
import com.revature.app.models.User;
import com.revature.app.models.UserRole;
import com.revature.app.util.exceptions.AuthenticationException;
import com.revature.app.util.exceptions.InvalidRequestException;
import com.revature.app.util.exceptions.ResourceConflictException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserService {

    private UserDAO userDAO;
    private PrismService prismService;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    // ====================================
    //      AUTHORIZATION METHODS
    // ====================================

    // ***********************************
    //      REGISTER USER
    // ***********************************

    public User register(NewUserRequest newUserRequest){

        User newUser = newUserRequest.extractUser();

        if (!isValidUser(newUser)) {
            throw new InvalidRequestException("Bad registration details given.");
        }

        boolean usernameAvailable = isUsernameAvailable(newUser.getUsername());
        boolean emailAvailable = isEmailAvailable(newUser.getEmail());

        if (!usernameAvailable || !emailAvailable) {
            String msg = "The values provided for the following fields are already taken by other users: ";
            if (!usernameAvailable) msg += "username ";
            if (!emailAvailable) msg += "email";
            throw new ResourceConflictException(msg);
        }

        // TODO encrypt provided password before storing in the database

        newUser.setId(UUID.randomUUID().toString());
        newUser.setRole(new UserRole("7c3521f5-ff75-4e8a-9913-01d15ee4da03","EMPLOYEE"));
        userDAO.save(newUser);

        return newUser;
    }

    // ***********************************
    //      LOGIN USER
    // ***********************************

    public User login(LoginRequest loginRequest){

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (!isUsernameValid(username) || !isPasswordValid(password)){
            throw new InvalidRequestException("Invalid credentials provided");
        }

        // TODO encrypt provided password

        User authUser = userDAO.findUserByUsernameAndPassword(username, password);

        if (authUser == null){
            throw new AuthenticationException();
        }

        return authUser;
    }


    // ***********************************
    //      GET ALL USERS
    // ***********************************
    public List<UserResponse> getAllUsers(){

        // **********************
        // MAPPING USING STREAMS        //todo order allUsers using comparable/comparators
        //***********************
        return userDAO.getAll()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());

        // *******************
        // PRE JAVA-8 mapping
        //********************

        /*
        List<User> users = userDAO.getAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user: users){
            userResponses.add(new UserResponse(user));
        }
        return userResponses;
        */
    }

    // ====================================
    //      VALIDATION METHODS
    // ====================================
    public boolean isValidUser(User user){

        // check first and last name are not empty or filled with white space
        if(user.getGivenName().trim().equals("") || user.getSurname().trim().equals("")){
            System.out.println("Bad first/last name");
            return false;
        }

        if (!isUsernameValid(user.getUsername())){
            System.out.println("Bad username");
            return false;
        }

        if (!isPasswordValid(user.getPassword())){
            System.out.println("Bad Password");
            return false;
        }

        if (!isEmailValid(user.getEmail())){
            System.out.println("Bad Email");
            return false;
        }

        System.out.println("Valid information was provided.");
        return true;
    }

    public boolean isUsernameValid(String username) {
        if (username == null) return false;
        return username.matches("^[a-zA-Z0-9]{8,25}");
    }

    public boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }

    public boolean isEmailValid(String email) {
        return email.matches("^[^@\\s]+@[^@\\s.]+\\.[^@.\\s]+$");
    }

    public boolean isUsernameAvailable(String username){
        return userDAO.findUserByUsername(username) == null;
    }

    public boolean isEmailAvailable(String email){
        return userDAO.findUserByEmail(email) == null;
    }
}
