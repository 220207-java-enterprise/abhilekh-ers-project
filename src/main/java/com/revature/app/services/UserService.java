package com.revature.app.services;

import com.revature.app.daos.UserDAO;
import com.revature.app.models.User;
import com.revature.app.util.exceptions.AuthenticationException;
import com.revature.app.util.exceptions.InvalidRequestException;

import java.util.UUID;

public class UserService {

    private UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    // ====================================
    //      AUTHORIZATION METHODS
    // ====================================

    // ***********************************
    //      REGISTER USER
    // ***********************************

    public User register(User newUser){

        if (!isUserValid(newUser)){
            throw new InvalidRequestException("Bad Registration details given");
        }

        // TODO validate that the email and username are unique

        // TODO encrypt password before storing to database

        newUser.setId(UUID.randomUUID().toString());

        userDAO.save(newUser);

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
    private boolean isUserValid(User user){

        if(user.getGivenName().trim().equals("") || user.getSurname().trim().equals("")){
            return false;
        }

        if (!isUsernameValid(user.getUsername())){
            return false;
        }

        if (!isPasswordValid(user.getPassword())){
            return false;
        }

        return isEmailValid(user.getEmail());
    }

    public boolean isUsernameValid(String username) {
        if (username == null) return false;
        return username.matches("^[a-zA-Z0-9]{8,25}");
    }

    public boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    public boolean isEmailValid(String email) {
        return email.matches("^[^@\\s]+@[^@\\s.]+\\.[^@.\\s]+$");
    }
}
