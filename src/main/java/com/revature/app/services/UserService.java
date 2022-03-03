package com.revature.app.services;

import com.revature.app.daos.UserDAO;
import com.revature.app.dtos.requests.LoginRequest;
import com.revature.app.dtos.requests.NewUserRequest;
import com.revature.app.dtos.requests.UpdateUserRequest;
import com.revature.app.dtos.responses.GetUserResponse;
import com.revature.app.models.User;
import com.revature.app.models.UserRole;
import com.revature.app.util.exceptions.AuthenticationException;
import com.revature.app.util.exceptions.InvalidRequestException;
import com.revature.app.util.exceptions.ResourceConflictException;
import org.mindrot.jbcrypt.BCrypt;

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

        newUser.setId(UUID.randomUUID().toString());
        newUser.setRole(new UserRole("3","EMPLOYEE"));
        newUser.setIsActive(false);

        // HASH PASSWORD
        String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
        System.out.println(hashed);
        newUser.setPassword(hashed);
        System.out.println(newUser);

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

        User potentialUser = userDAO.findUserByUsername(username);

        String potentialUserHashedPass = potentialUser.getPassword();
        String loginRequestHashedPass = BCrypt.hashpw(loginRequest.getPassword(), BCrypt.gensalt());

        System.out.println(BCrypt.checkpw(loginRequest.getPassword(), potentialUserHashedPass));

        if(!BCrypt.checkpw(loginRequest.getPassword(), potentialUserHashedPass)) {
            System.out.println("PASSWORD DID NOT MATCH");
            throw new AuthenticationException();
        }

        User authUser = userDAO.findUserByUsername(username);
        System.out.println(authUser);

        if (authUser == null){
            throw new AuthenticationException();
        }

        return authUser;
    }


    // ***********************************
    //      GET ALL USERS
    // ***********************************
    public List<GetUserResponse> getAllUsers(){

        // **********************
        // MAPPING USING STREAMS        //todo order allUsers using comparable/comparators
        //***********************
        return userDAO.getAll()
                .stream()
                .map(GetUserResponse::new)
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

    // ***********************************
    //      UPDATE A USER
    // ***********************************
    public User updateUser(UpdateUserRequest updateUserRequest){

        User updatedUser = userDAO.getById(updateUserRequest.getId());

        if (updateUserRequest.getUsername()!=null){
            updatedUser.setUsername(updateUserRequest.getUsername());
        }
        if (updateUserRequest.getEmail()!=null){
            updatedUser.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getGivenName()!=null){
            updatedUser.setGivenName(updateUserRequest.getGivenName());
        }
        if (updateUserRequest.getSurname()!=null){
            updatedUser.setSurname(updateUserRequest.getSurname());
        }
        if (updateUserRequest.getPassword()!=null){
            updatedUser.setPassword(updateUserRequest.getPassword());
        }
        if (updateUserRequest.getRoleName()!=null){
            if (updateUserRequest.getRoleName().equals("ADMIN")) updatedUser.setRole(new UserRole("1","ADMIN"));
            else if (updateUserRequest.getRoleName().equals("FINANCE_MANAGER")) updatedUser.setRole(new UserRole("2",
             "FINANCE_MANAGER"));
            else if (updateUserRequest.getRoleName().equals("EMPLOYEE")) updatedUser.setRole(new UserRole("3",
                    "EMPLOYEE"));
        }

        if (updateUserRequest.getIsActive() == true){
            updatedUser.setIsActive(true);
        } else if (updateUserRequest.getIsActive() == false){
            updatedUser.setIsActive(false);
        }

        userDAO.update(updatedUser);

        return updatedUser;
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
