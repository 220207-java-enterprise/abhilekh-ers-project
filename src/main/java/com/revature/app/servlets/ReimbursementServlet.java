package com.revature.app.servlets;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.dtos.requests.NewReimbursementRequest;
import com.revature.app.dtos.requests.UpdateReimbursementRequest;
import com.revature.app.dtos.responses.Principal;
import com.revature.app.dtos.responses.ReimbursementResponse;
import com.revature.app.dtos.responses.ResourceCreationResponse;
import com.revature.app.dtos.responses.UpdateReimbursementResponse;
import com.revature.app.models.Reimbursement;
import com.revature.app.services.ReimbursementService;
import com.revature.app.services.TokenService;
import com.revature.app.util.exceptions.InvalidRequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ReimbursementServlet extends HttpServlet {

    private final TokenService tokenService;
    private final ReimbursementService reimbursementService;
    private final ObjectMapper mapper;

    public ReimbursementServlet(TokenService tokenService, ReimbursementService reimbursementService, ObjectMapper mapper) {
        this.tokenService = tokenService;
        this.reimbursementService = reimbursementService;
        this.mapper = mapper;
    }

    // Get all reimbursements
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if (requester == null){
            resp.getWriter().write("You are not logged in.");
            resp.setStatus(401);
            return;
        }

        if (requester.getRole().equals("EMPLOYEE") || requester.getRole().equals("ADMIN")){
            resp.getWriter().write("Please login as a Finance Manager to see all Reimburements in the system.");
            resp.setStatus(403);
            return;
        }

        String[] reqFrags = req.getRequestURI().split("/");
        if(reqFrags.length==4 && reqFrags[3].equals("pending")){

            List<ReimbursementResponse> reimbursementResponses = reimbursementService.getAllPendingReimbursements();
            String payload = mapper.writeValueAsString(reimbursementResponses);
            resp.setContentType("application/json");
            resp.getWriter().write(payload);
            resp.setStatus(200);

            return;
        }

        if(reqFrags.length==4 && reqFrags[3].equals("approved")){

            List<ReimbursementResponse> reimbursementResponses = reimbursementService.getAllAcceptedReimbursements();
            String payload = mapper.writeValueAsString(reimbursementResponses);
            resp.setContentType("application/json");
            resp.getWriter().write(payload);
            resp.setStatus(200);

            return;
        }

        if(reqFrags.length==4 && reqFrags[3].equals("denied")){
            resp.getWriter().write("Pending reimbursements here\n");

            List<ReimbursementResponse> reimbursementResponses = reimbursementService.getAllDeniedReimbursements();
            String payload = mapper.writeValueAsString(reimbursementResponses);
            resp.setContentType("application/json");
            resp.getWriter().write(payload);
            resp.setStatus(200);

            return;
        }

        List<ReimbursementResponse> reimbursementResponses = reimbursementService.getAllReimbursements();
        String payload = mapper.writeValueAsString(reimbursementResponses);
        resp.setContentType("application/json");
        resp.getWriter().write(payload);
        resp.setStatus(200);
    }

    // Create a reimbursement
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if (requester == null){
            resp.getWriter().write("You are not logged in.");
            resp.setStatus(401);
            return;
        }
        System.out.println(requester);
        System.out.println(requester.getRole().equals("EMPLOYEE"));
        if (!requester.getRole().equals("EMPLOYEE")){
            resp.getWriter().write("Please login as an Employee to make a Reimbursement request for approval.");
            resp.setStatus(403);
            return;
        }


        PrintWriter respWriter = resp.getWriter();

        try {

            NewReimbursementRequest newReimbursementRequest = mapper.readValue(req.getInputStream(),
                    NewReimbursementRequest.class);

            newReimbursementRequest.setAuthorId(requester.getId());

            Reimbursement newReimbursement = reimbursementService.addNewReimbursement(newReimbursementRequest);

            resp.setStatus(201);
            resp.setContentType("application/json");
            String payload = mapper.writeValueAsString(new ResourceCreationResponse(newReimbursement.getId()));
            respWriter.write(payload);
        } catch (InvalidRequestException | DatabindException e){
            resp.getWriter().write("Please submit a valid Reimbursement request");
            resp.setStatus(400);
        } catch (Exception e){
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if (requester == null){
            resp.getWriter().write("You are not logged in.");
            resp.setStatus(401);
            return;
        }

        if (!requester.getRole().equals("FINANCE_MANAGER")){
            resp.getWriter().write("Please login as a Finance Manager to update a Reimbursement.");
            resp.setStatus(403);
            return;
        }

        PrintWriter respWriter = resp.getWriter();

        try {
            UpdateReimbursementRequest updateReimbursementRequest =
                    mapper.readValue(req.getInputStream(),UpdateReimbursementRequest.class);

            updateReimbursementRequest.setResolverId(requester.getId());
            System.out.println("UPDATE RESOLVER ID --> "+ requester.getId());

            Reimbursement reimbursement = reimbursementService.updateReimbursement(updateReimbursementRequest);

            String payload = mapper.writeValueAsString(new UpdateReimbursementResponse(reimbursement));
            resp.setContentType("application/json");
            respWriter.write(payload);
            resp.setStatus(200);

        } catch (Exception e){
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
