package com.revature.app.services;

import com.revature.app.daos.UserDAO;
import com.revature.app.dtos.requests.LoginRequest;
import com.revature.app.dtos.requests.NewUserRequest;
import com.revature.app.models.User;
import com.revature.app.util.exceptions.AuthenticationException;
import com.revature.app.util.exceptions.InvalidRequestException;
import com.revature.app.util.exceptions.ResourceConflictException;

import java.util.UUID;

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
