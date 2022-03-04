package com.revature.app.services;


// **************************************************************
//  TEST CLASS SUITE FOR ReimbursementService
//  encapsulates test cases for methods in ReimbursementService
// **************************************************************

import com.revature.app.daos.ReimbursementDAO;
import com.revature.app.dtos.responses.ReimbursementResponse;
import com.revature.app.models.Reimbursement;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

public class ReimbursementServiceTest {

//    private ReimbursementService sut;
//
//    private ReimbursementDAO mockReimbursementDAO;
//
//    @Before
//    public void setup(){
//        mockReimbursementDAO = mock(ReimbursementDAO.class);
//        sut = new ReimbursementService(mockReimbursementDAO);
//    }
//
//
//    @Test
//    public void test_getAllPendingReimbursements_returnsListOfPendingReimbursements(){
//
//         try{
//             mockReimbursementDAO.getAllPending()
//                     .stream()
//                     .map(ReimbursementResponse::new)
//                     .collect(Collectors.toList());
//         } finally {
//             verify(mockReimbursementDAO,times(0)).getAllPending();
//         }
//    }
//


}
