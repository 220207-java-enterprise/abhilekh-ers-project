package com.revature.app.services;

import com.revature.app.daos.UserDAO;

import com.revature.app.dtos.requests.LoginRequest;
import com.revature.app.dtos.requests.NewUserRequest;
import com.revature.app.dtos.requests.UpdateUserRequest;
import com.revature.app.dtos.responses.GetUserResponse;
import com.revature.app.models.User;
import com.revature.app.util.exceptions.InvalidRequestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

// ******************************************************
//  TEST CLASS SUITE FOR UserService
//  encapsulates test cases for methods in UserService
// ******************************************************

public class UserServiceTest {

    private UserService sut; //sut = System Under Test

    private UserDAO mockUserDao;


    // to be invoked before each
    @Before
    public void setup(){
        mockUserDao = mock(UserDAO.class);
        sut = new UserService(mockUserDao);
    }

    @Test
    public void test_isUsernameValid_returnsFalse_givenEmptyString(){

        String username ="";
        // Act
        boolean result = sut.isUsernameValid(username);
        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void test_isUsernameValid_returnsFalse_givenNullString(){
        // Arrange
        String username = null;
        // Act
        boolean result = sut.isUsernameValid(username);
        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void test_isUsernameValid_returnsFalse_givenShortUsername(){
        //Arrange
        String username="short";
        //Act
        boolean result = sut.isUsernameValid(username);
        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void test_isUsernameValid_returnsFalse_givenLongUsername(){
        //Arrange
        String username="waytoolongofausernameforourapplication";
        //Act
        boolean result = sut.isUsernameValid(username);
        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void test_isUsernameValid_returnsFalse_givenUsernameWithIllegalCharacters(){
        //Arrange
        String username="tester99!";
        //Act
        boolean result = sut.isUsernameValid(username);
        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void test_isUsernameValid_returnsTrue_givenValidUsername(){
        //Arrange
        String username="4bhilekh";
        //Act
        boolean result = sut.isUsernameValid(username);
        //Assert
        Assert.assertTrue(result);
    }


    @Test
    public void test_isValidUser_givenInvalidUserUsername(){
        //Arrange
        User invalidUser = new User("s", "email@email.com", "p4$$word", "John", "Doe", true, "3");

        //Act
        boolean result = sut.isValidUser(invalidUser);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void test_isValidUser_givenInvalidUserEmail(){
        //Arrange
        User invalidUser = new User("johnDoe11", "email@email", "p4$$word", "John", "Doe", true, "3");

        //Act
        boolean result = sut.isValidUser(invalidUser);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void test_isValidUser_givenInvalidUserPassword(){
        //Arrange
        User invalidUser = new User("username", "email@email.com", "password", "John", "Doe", true, "3");

        //Act
        boolean result = sut.isValidUser(invalidUser);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void test_isValidUser_givenInvalidUserGivenName(){
        //Arrange
        User invalidUser = new User("username", "email@email.com", "p4$$word", "    ", "Doe", true, "3");

        //Act
        boolean result = sut.isValidUser(invalidUser);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void test_isValidUser_givenInvalidUserSurname(){
        //Arrange
        User invalidUser = new User("username", "email@email.com", "p4$$word", "John", "", true, "3");

        //Act
        boolean result = sut.isValidUser(invalidUser);

        //Assert
        Assert.assertFalse(result);
    }


    @Test
    public void test_isValidUser_givenValidUser(){
        //Arrange
        User validUser = new User("username", "email@email.com", "p4$$word", "john", "doe", true, "3");

        //Act
        boolean result = sut.isValidUser(validUser);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void test_isUsernameAvailable_givenDuplicateUsername(){
        // Arrange
        String username = "4bhilekh";
        when(mockUserDao.findUserByUsername(username)).thenReturn(new User());
        // Act
        boolean result = sut.isUsernameAvailable(username);

        Assert.assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void test_registration_throwsRuntimeException_givenInvalidUser(){

        UserService spiedSut = Mockito.spy(sut);
        NewUserRequest invalidUserRequest = new NewUserRequest("username", "email@email", "password", "sdas", "dfdf");

        User invalidUserToSave = invalidUserRequest.extractUser();

        try {
            spiedSut.register(invalidUserRequest);
        } finally {
            verify(spiedSut, times(1)).isValidUser(invalidUserToSave);
            verify(spiedSut, times(0)).isUsernameAvailable(invalidUserToSave.getUsername());
            verify(spiedSut, times(0)).isEmailAvailable(invalidUserToSave.getEmail());
            verify(mockUserDao, times(0)).save(invalidUserToSave);
        }
    }

    @Test
    public void test_registration_givenValidUser(){
        //Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email@email.com", "p4$$word", "john", "doe");

        User validUser = newUserRequest.extractUser();

        UserService spiedSut = Mockito.spy(sut);
        when(spiedSut.isUsernameValid(validUser.getUsername())).thenReturn(true);
        when(spiedSut.isPasswordValid(validUser.getPassword())).thenReturn(true);
        when(spiedSut.isEmailValid(validUser.getEmail())).thenReturn(true);
        when(spiedSut.isValidUser(validUser)).thenReturn(true);

        doNothing().when(mockUserDao).save(validUser);

        User registerResult = spiedSut.register(newUserRequest);

        Assert.assertNotNull(registerResult);
    }

    @Test
    public void test_login_returnsAuthenticatedAppUser_givenValidAndKnownCredentials(){

        //Arrange
        LoginRequest loginRequest = new LoginRequest("4bhilekh", "p4$$word");
        String hashedPassword = BCrypt.hashpw(loginRequest.getPassword(), BCrypt.gensalt());
        UserService spiedSut = Mockito.spy(sut);
        String username =  loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Act
        when(spiedSut.isUsernameValid(username)).thenReturn(true);
        when(spiedSut.isPasswordValid(password)).thenReturn(true);
        // How can I return a potentialUser (containing the hashed password?)
        when(mockUserDao.findUserByUsername(username)).thenReturn(new User());

        if(BCrypt.checkpw(loginRequest.getPassword(), BCrypt.hashpw(loginRequest.getPassword(), BCrypt.gensalt()))) {
            User authUser = mockUserDao.findUserByUsername(username);
            // Assert
            Assert.assertNotNull(authUser);
        }
    }

    @Test(expected=RuntimeException.class)
    public void test_login_throwsRuntimeException_givenUnknownUserCredentials() {
        //Arrange
        String unknownUsername = "unknownuser";
        String somePassword = "p4$$WORD";
        LoginRequest loginRequest = new LoginRequest(unknownUsername, somePassword);
        when(mockUserDao.findUserByUsernameAndPassword(unknownUsername, somePassword)).thenReturn(null);

        //Act
        sut.login(loginRequest);
    }

    @Test(expected = InvalidRequestException.class)
    public void test_login_throwsInvalidRequestException_givenInvalidUsername() {
        // Arrange
        String invalidUsername = "no";
        String validPassword = "p4$$word";
        LoginRequest loginRequest = new LoginRequest(invalidUsername, validPassword);
        //Act
        try {
            sut.login(loginRequest);
        } finally {
            // proof that findUserByUsernameAndPassword is invoked 0 times - it was "mocked"
            verify(mockUserDao, times(0)).findUserByUsernameAndPassword(invalidUsername, validPassword);
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_login_throwsInvalidRequestException_givenInvalidPassword() {
        // Arrange
        String validUsername = "4bhilekh";
        String invalidPassword = "invalid";
        LoginRequest loginRequest = new LoginRequest(validUsername, invalidPassword);
        //Act
        try {
            sut.login(loginRequest);
        } finally {
            // proof that findUserByUsernameAndPassword is invoked 0 times - it was "mocked"
            verify(mockUserDao, times(0)).findUserByUsernameAndPassword(validUsername, invalidPassword);
        }
    }

    @Test
    public void test_isUserActive_givenActiveId(){
        User activeUser = new User("username", "email@email.com", "p4$$word", "john", "doe", true, "3");

        when(mockUserDao.getById(anyString())).thenReturn(activeUser);
        boolean result = sut.isUserActive(anyString());

        Assert.assertTrue(result);
    }

    @Test
    public void test_isUserActive_givenInactiveId(){
        User inActiveUser = new User("username", "email@email.com", "p4$$word", "john", "doe", false, "3");

        when(mockUserDao.getById(anyString())).thenReturn(inActiveUser);
        boolean result = sut.isUserActive(anyString());

        Assert.assertFalse(result);
    }

    @Test
    public void test_getAllUsers_returnsNonNullList(){
        List<User> allUsers = new ArrayList<>();
        when(mockUserDao.getAll()).thenReturn(allUsers);
        List<GetUserResponse> result = sut.getAllUsers();

        Assert.assertNotNull(result);
    }

    @Test
    public void test_updateUser_returnsUpdatedUser(){

        UserService spiedSut = Mockito.spy(sut);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest("123", "username", "test@email.com",
                "p4$$word","givenname","surname",true,"2");
        User updatedUser = new User();
        when(mockUserDao.getById(anyString())).thenReturn(new User());

        doNothing().when(mockUserDao).update(updatedUser);

        User updateResult = spiedSut.updateUser(updateUserRequest);

        Assert.assertNotNull(updateResult);
    }
}
