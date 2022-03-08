package com.revature.app.services;


// **************************************************************
//  TEST CLASS SUITE FOR ReimbursementService
//  encapsulates test cases for methods in ReimbursementService
// **************************************************************

import com.revature.app.daos.ReimbursementDAO;
import org.junit.Before;

import static org.mockito.Mockito.*;

public class ReimbursementServiceTest {

    private ReimbursementService sut;

    private ReimbursementDAO mockReimbursementDAO;

    @Before
    public void setup(){
        mockReimbursementDAO = mock(ReimbursementDAO.class);
        sut = new ReimbursementService(mockReimbursementDAO);
    }


//    @Test
//    public void test_getAllPendingReimbursements_returnsNonNullList(){
//        List<Reimbursement> allPendingReimbursements = new ArrayList<>();
//        when(mockReimbursementDAO.getAllPending()).thenReturn(allPendingReimbursements);
//        List<ReimbursementResponse> result = sut.getAllPendingReimbursements();
//        Assert.assertNotNull(result);
//    }



}
