package com.revature.app.services;

import com.revature.app.daos.UserDAO;
import com.revature.app.dtos.NewUserRequest;
import com.revature.app.models.User;
import com.revature.app.util.exceptions.AuthenticationException;
import com.revature.app.util.exceptions.InvalidRequestException;

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


        // map dto and models using extractUser()
        User newUser = newUserRequest.extractUser();

        System.out.println("checking validations...");
        if (!isValidUser(newUser)){
            throw new RuntimeException("Invalid Registration information provided.");
        }

        // TODO validate that the email and username are unique

        // TODO encrypt password before storing to database

        newUser.setId(UUID.randomUUID().toString());

        userDAO.save(newUser);

        System.out.printf("Registration info provided: %s\n", newUser.toString());
        return newUser;
    }

    // ***********************************
    //      LOGIN USER
    // ***********************************

    public User login(String username, String password){

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
}
