package com.revature.app.servlets;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.app.dtos.requests.*;
import com.revature.app.dtos.responses.*;
import com.revature.app.models.Reimbursement;
import com.revature.app.services.ReimbursementService;
import com.revature.app.services.TokenService;
import com.revature.app.util.exceptions.InvalidRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class ReimbursementServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(ReimbursementServlet.class);

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

        logger.debug("ReimbursementServlet#doGet invoked with args: "+ Arrays.asList(req, resp));

        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        String[] reqFrags = req.getRequestURI().split("/");
        if(reqFrags.length==4 && reqFrags[3].equals("byEmployee")){

            GetUserReimbursementRequest getUserReimbursementRequest = mapper.readValue(req.getInputStream(),
                    GetUserReimbursementRequest.class);

            if (!requester.getId().equals(getUserReimbursementRequest.getAuthorId())){
                resp.getWriter().write("You cannot view Reimbursements for this Employee");
                resp.setStatus(403);
                return;
            }

            List<ReimbursementResponse> reimbursementResponses =
                    reimbursementService.getReimbursementsByAuthor(getUserReimbursementRequest);

            String payload = mapper.writeValueAsString(reimbursementResponses);
            resp.setContentType("application/json");
            resp.getWriter().write(payload);
            logger.debug("ReimbursementServlet#doGet returned all Reimbursements by Employee");
            resp.setStatus(200);

            return;

        }
        if(reqFrags.length==4 && reqFrags[3].equals("pending")){

            List<ReimbursementResponse> reimbursementResponses = reimbursementService.getAllPendingReimbursements();
            String payload = mapper.writeValueAsString(reimbursementResponses);
            resp.setContentType("application/json");
            resp.getWriter().write(payload);
            logger.debug("ReimbursementServlet#doGet returned all Pending Reimbursements successfully.");

            resp.setStatus(200);

            return;
        }

        if(reqFrags.length==4 && reqFrags[3].equals("approved")){

            List<ReimbursementResponse> reimbursementResponses = reimbursementService.getAllAcceptedReimbursements();
            String payload = mapper.writeValueAsString(reimbursementResponses);
            resp.setContentType("application/json");
            resp.getWriter().write(payload);
            logger.debug("ReimbursementServlet#doGet returned all Approved Reimbursements successfully.");

            resp.setStatus(200);

            return;
        }

        if(reqFrags.length==4 && reqFrags[3].equals("denied")){
            List<ReimbursementResponse> reimbursementResponses = reimbursementService.getAllDeniedReimbursements();
            String payload = mapper.writeValueAsString(reimbursementResponses);
            resp.setContentType("application/json");
            resp.getWriter().write(payload);
            logger.debug("ReimbursementServlet#doGet returned all Denied Reimbursements successfully.");
            resp.setStatus(200);

            return;
        }

        if (requester == null){
            logger.debug("ReimbursementServlet#doGet returned null because requester was not logged in");
            resp.getWriter().write("You are not logged in.");
            resp.setStatus(401);
            return;
        }

        if (requester.getRole().equals("ADMIN")){
            logger.debug("ReimbursementServlet#doGet was not invoked by a Finance Manager.");
            resp.getWriter().write("Please login as a Finance Manager to see all or Employee to see your " +
                    "Reimburements in the system.");
            resp.setStatus(403);
            return;
        }


        List<ReimbursementResponse> reimbursementResponses = reimbursementService.getAllReimbursements();
        String payload = mapper.writeValueAsString(reimbursementResponses);
        resp.setContentType("application/json");
        resp.getWriter().write(payload);

        logger.debug("ReimbursementServlet#doGet returned all Reimbursements successfully.");

        resp.setStatus(200);
    }

    // Create a reimbursement
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.debug("ReimbursementServlet#doPost invoked with args: "+ Arrays.asList(req, resp));

        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if (requester == null){

            resp.getWriter().write("You are not logged in.");
            resp.setStatus(401);
            return;
        }
        if (!requester.getRole().equals("EMPLOYEE")){
            resp.getWriter().write("Please login as an Employee to make a Reimbursement request.");
            resp.setStatus(403);
            return;
        }


        PrintWriter respWriter = resp.getWriter();

        try {
            NewReimbursementRequest newReimbursementRequest = mapper.readValue(req.getInputStream(),
                    NewReimbursementRequest.class);

            newReimbursementRequest.setAuthorId(requester.getId());
            Reimbursement newReimbursement = reimbursementService.addNewReimbursement(newReimbursementRequest);
            resp.getWriter().write("Successfully added a Reimbursement.");
            resp.setContentType("application/json");
            String payload = mapper.writeValueAsString(new ResourceCreationResponse(newReimbursement.getId()));
            respWriter.write(payload);
            resp.setStatus(201);
        } catch (InvalidRequestException | DatabindException e){
            logger.error(e.getMessage(), e);
            resp.getWriter().write("Please submit a valid Reimbursement request");
            resp.setStatus(400);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            resp.getWriter().write("Something went wrong.");
            resp.setStatus(500);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.debug("ReimbursementServlet#doPut invoked with args: "+ Arrays.asList(req, resp));


        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if (requester == null){
            resp.getWriter().write("You are not logged in.");
            resp.setStatus(401);
            return;
        }

        if (requester.getRole().equals("ADMIN")){
            resp.getWriter().write("Login as Finance Manager to approve/deny Reimbursements\nOR\nLogin as Employee to" +
                    " edit your reimbursements.");
            resp.setStatus(403);
            return;

        } else if (requester.getRole().equals("EMPLOYEE")){
            PrintWriter respWriter = resp.getWriter();

            try{
                ManageMyReimbursementRequest manageMyReimbursementRequest = mapper.readValue(req.getInputStream(),
                        ManageMyReimbursementRequest.class);

                Reimbursement extractedReimbursement = manageMyReimbursementRequest.extractReimbursement();


                if (!reimbursementService.isAuthorizedToEdit(extractedReimbursement, requester)){
                        resp.getWriter().write("You are not authorized to view/update this reimbursement.");
                        resp.setStatus(403);
                        return;
                }

                //  only allow edit on PENDING reimbursements
                if (!reimbursementService.getReimbursementById(extractedReimbursement.getId()).getStatus().getId().equals("1")) {
                    resp.setStatus(403);
                    respWriter.write("You can only edit Reimbursements that are Pending.");
                    return;
                }

                Reimbursement reimbursement = reimbursementService.manageMyReimbursement(manageMyReimbursementRequest);
                String payload = mapper.writeValueAsString(new ManageMyReimbursementResponse(reimbursement));
                resp.setContentType("application/json");
                respWriter.write(payload);
                resp.setStatus(200);

                return;

            }catch (Exception e){
                logger.error(e.getMessage(), e);
                e.printStackTrace();
                resp.setStatus(500);
            }
        } else if (requester.getRole().equals("FINANCE_MANAGER")){
            PrintWriter respWriter = resp.getWriter();

            try {
                UpdateReimbursementRequest updateReimbursementRequest =
                        mapper.readValue(req.getInputStream(),UpdateReimbursementRequest.class);

                updateReimbursementRequest.setResolverId(requester.getId());

                Reimbursement reimbursement = reimbursementService.updateReimbursement(updateReimbursementRequest);

                String payload = mapper.writeValueAsString(new UpdateReimbursementResponse(reimbursement));
                resp.setContentType("application/json");
                respWriter.write(payload);
                resp.setStatus(200);

            } catch (Exception e){
                logger.error(e.getMessage(), e);
                e.printStackTrace();
                resp.setStatus(500);
            }
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if (requester == null) {
            resp.setStatus(401);
            return;
        }

        if (!requester.getRole().equals("EMPLOYEE")) {
            resp.setStatus(403);
            return;
        }

        PrintWriter respWriter = resp.getWriter();

        try {
            RecallReimbursementRequest recallRequest = mapper.readValue(req.getInputStream(),
                    RecallReimbursementRequest.class);

            Reimbursement recalledReimbursement = reimbursementService.getReimbursementById(recallRequest.getId());

            if (!requester.getId().equals(recalledReimbursement.getAuthor().getId())){
                respWriter.write("This Reimbursement does not belong to you. Recall Rejected.");
                resp.setStatus(403);
                return;
            }

            if (!recalledReimbursement.getStatus().getStatus().equals("PENDING")){
                respWriter.write("You can only recall pending Reimbursements. This one has already been " +
                        "approved/denied by the Finance Manager");
                resp.setStatus(403);
                return;
            }

            reimbursementService.deleteReimbursementById(recallRequest);

            respWriter.write("Successfully recalled this pending reimbursement.");
            resp.setStatus(202); // DELETED
            resp.setContentType("application/json");

        } catch (InvalidRequestException | DatabindException e) {
            e.printStackTrace();
            resp.setStatus(400);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }

    }
}
