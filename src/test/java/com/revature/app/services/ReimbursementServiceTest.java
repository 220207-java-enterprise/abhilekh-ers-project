package com.revature.app.services;


// **************************************************************
//  TEST CLASS SUITE FOR ReimbursementService
//  encapsulates test cases for methods in ReimbursementService
// **************************************************************

import com.revature.app.daos.ReimbursementDAO;
import com.revature.app.dtos.requests.NewReimbursementRequest;
import com.revature.app.dtos.responses.ReimbursementResponse;
import com.revature.app.models.Reimbursement;
import com.revature.app.util.exceptions.InvalidRequestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ReimbursementServiceTest {

    private ReimbursementService sut;

    private ReimbursementDAO mockReimbursementDAO;

    @Before
    public void setup(){
        mockReimbursementDAO = mock(ReimbursementDAO.class);
        sut = new ReimbursementService(mockReimbursementDAO);
    }


    @Test(expected = InvalidRequestException.class)
    public void test_addNewReimbursement_throwsInvalidRequestException_givenNegativeAmount(){
        // Arrange
        ReimbursementService spiedSut = Mockito.spy(sut);
        NewReimbursementRequest invalidReimbursementRequest = new NewReimbursementRequest(-2,"description","1",
                "LODGING");
        Reimbursement invalidReimbursement = invalidReimbursementRequest.extractReimbursement();

        try {
            spiedSut.addNewReimbursement(invalidReimbursementRequest);
        } finally {
            verify(mockReimbursementDAO, times(0)).save(invalidReimbursement);
        }
    }

//    @Test
//    public void test_addNewReimbursement_returnsReimbursement_givenValidReimbursement(){
//        ReimbursementService spiedSut = Mockito.spy(sut);
//        NewReimbursementRequest validReimbursementRequest = new NewReimbursementRequest(100, "description", "1",
//                "LODGING");
//        Reimbursement validReimbursement = validReimbursementRequest.extractReimbursement();
//
//
//        doNothing().when(mockReimbursementDAO).save(validReimbursement);
//
//        Reimbursement addResult = spiedSut.addNewReimbursement(validReimbursementRequest);
//
//        Assert.assertNotNull(addResult);
//    }



}
