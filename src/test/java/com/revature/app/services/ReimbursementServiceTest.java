package com.revature.app.services;


// **************************************************************
//  TEST CLASS SUITE FOR ReimbursementService
//  encapsulates test cases for methods in ReimbursementService
// **************************************************************

import com.revature.app.daos.ReimbursementDAO;
import com.revature.app.daos.ReimbursementStatusDAO;
import com.revature.app.daos.ReimbursementTypeDAO;
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

//    private ReimbursementService sut;
//
//    private ReimbursementDAO mockReimbursementDAO;
//    private ReimbursementTypeDAO mockReimbursementTypeDAO;
//    private ReimbursementStatusDAO mockReimbursementStatusDAO;
//
//    @Before
//    public void setup(){
//        mockReimbursementDAO = mock(ReimbursementDAO.class);
//        sut = new ReimbursementService(mockReimbursementDAO);
//    }
//
//
//    @Test(expected = InvalidRequestException.class)
//    public void test_addNewReimbursement_throwsInvalidRequestException_givenNegativeAmount(){
//        // Arrange
//        ReimbursementService spiedSut = Mockito.spy(sut);
//        NewReimbursementRequest invalidReimbursementRequest = new NewReimbursementRequest(-2,"description","1",
//                "LODGING");
//        Reimbursement invalidReimbursement = invalidReimbursementRequest.extractReimbursement();
//
//        try {
//            spiedSut.addNewReimbursement(invalidReimbursementRequest);
//        } finally {
//            verify(mockReimbursementDAO, times(0)).save(invalidReimbursement);
//        }
//    }
//
//    @Test
//    public void test_getAllReimbursements_returnsNonNullList(){
//        List<Reimbursement> allReimbursements = new ArrayList<>();
//        when(mockReimbursementDAO.getAll()).thenReturn(allReimbursements);
//        List<ReimbursementResponse> result = sut.getAllReimbursements();
//
//        Assert.assertNotNull(result);
//    }
//
//    @Test
//    public void test_getPendingReimbursements_returnsNonNullList(){
//        List<Reimbursement> pendingReimbursements = new ArrayList<>();
//        when(mockReimbursementDAO.getAllPending()).thenReturn(pendingReimbursements);
//        List<ReimbursementResponse> result = sut.getAllPendingReimbursements();
//
//        Assert.assertNotNull(result);
//    }
//
//    @Test
//    public void test_getDeniedReimbursements_returnsNonNullList(){
//        List<Reimbursement> deniedReimbursements = new ArrayList<>();
//        when(mockReimbursementDAO.getAllDenied()).thenReturn(deniedReimbursements);
//        List<ReimbursementResponse> result = sut.getAllDeniedReimbursements();
//
//        Assert.assertNotNull(result);
//    }
//
//    @Test
//    public void test_getApprovedReimbursements_returnsNonNullList(){
//        List<Reimbursement> approvedReimbursements = new ArrayList<>();
//        when(mockReimbursementDAO.getAllApproved()).thenReturn(approvedReimbursements);
//        List<ReimbursementResponse> result = sut.getAllApprovedReimbursements();
//
//        Assert.assertNotNull(result);
//    }

//    @Test
//    public void test_getReimbursementById_returnsReimbursement(){
//        ReimbursementService spiedSut = Mockito.spy(sut);
//        Reimbursement reimbursement = new Reimbursement("1",200.00f,"description", "FOOD");
//        when(mockReimbursementDAO.getById("1")).thenReturn(reimbursement);
//        Reimbursement reimbursementById = spiedSut.getReimbursementById("1");
//
//        Assert.assertNotNull(reimbursementById);
//    }


    //    @Test
//    public void test_addNewReimbursement_returnsReimbursement_givenValidReimbursement(){
//        ReimbursementService spiedSut = Mockito.spy(sut);
//        NewReimbursementRequest validReimbursementRequest = new NewReimbursementRequest(100, "description", "1",
//               "LODGING");
//        Reimbursement validReimbursement = validReimbursementRequest.extractReimbursement();
//        validReimbursement.setType(new ReimbursementType("1","LODGING"));
//        validReimbursement.setStatusId("1");
//        System.out.println(validReimbursementRequest.getTypeName());
//        System.out.println(validReimbursementRequest);
//        System.out.println(validReimbursement);
//        System.out.println(mockReimbursementTypeDAO.getByName("LODGING"));
//        when(mockReimbursementTypeDAO.getByName(validReimbursementRequest.getTypeName())).thenReturn(new ReimbursementType());
//        doNothing().when(mockReimbursementDAO).save(validReimbursement);
//        Reimbursement savedReimbursement = sut.addNewReimbursement(validReimbursementRequest);
//        Assert.assertNotNull(savedReimbursement);
//    }
}
