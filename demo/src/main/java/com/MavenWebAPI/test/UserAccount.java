package com.MavenWebAPI.test;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;

import com.google.gson.Gson;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("portal/userAccount")
public class UserAccount {
    static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
            .getLogger(UserAccount.class.getName());
    @Context
    private UriInfo context;

    public UserAccount() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserAccount(
        @DefaultValue("") @QueryParam("id") String id)
        {
            log.info("REQ: " + id);
      
            DatabaseFactory databaseFactory = new DatabaseFactory();
            ResUserAccount res = new ResUserAccount();

            if (id == null || id.isEmpty()){
                res.setRc("40000");
                res.setRcDesc("BAD REQUEST");
                res.setMessage("User Id must be filled.");
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
            }

            if (id.length() > 10) {
                res.setRc("40001");
                res.setRcDesc("INVALID FORMAT");
                res.setMessage("Input is too long.");
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
            }

            res = databaseFactory.getUserAccount(id);

            Gson gson = new Gson();
            log.info("RES: " + gson.toJson(res));
            return Response.ok(res)
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Frame-Options", "deny")
                .header("Content-Security-Policy", "default-src 'none'")
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertUserAcount(ReqUserAccount reqUserAccount){
        DatabaseFactory databaseFactory = new DatabaseFactory();
        ResUserAccount res = new ResUserAccount();

        if (reqUserAccount.getUseremail() == null || reqUserAccount.getUseremail().isEmpty()
            ||reqUserAccount.getUsername() == null || reqUserAccount.getUsername().isEmpty()) {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("rc", "40000");
            errorResponse.put("rcDesc", "BAD REQUEST");
            errorResponse.put("message", "Useremail and Username must be filled.");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse.toString()).build();
        }


        boolean addData = databaseFactory.postUserAccount(reqUserAccount);
        if (!addData) {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("rc", "40002");
            errorResponse.put("rcDesc", "FAILED");
            errorResponse.put("message", "Failed to insert data.");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse.toString()).build();
        }

        JSONObject successResponse = new JSONObject();
        successResponse.put("rc", "20000");
        successResponse.put("rcDesc", "SUCCESS.");
        successResponse.put("message", "Insert data successfully.");
        return Response.status(Response.Status.OK).entity(successResponse.toString()).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAccount(
        @QueryParam("username") String username
    ) {
        DatabaseFactory databaseFactory = new DatabaseFactory();
        // Check if the required parameters are provided
        try {
            if (username == null || username.isEmpty()) {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("rc", "40000");
            errorResponse.put("rcDesc", "BAD REQUEST");
            errorResponse.put("message", "Username must be filled.");
            }

            // Call the method to delete the bank account
            boolean deleteSuccess = databaseFactory.deleteAccount(username);

            // Check if the bank account was successfully deleted
            if (deleteSuccess) {
                JSONObject successResponse = new JSONObject();
                successResponse.put("rc", "20000");
                successResponse.put("rcDesc", "SUCCESS.");
                successResponse.put("message", "User account deleted succesfully.");
                return Response.status(Response.Status.OK).entity(successResponse.toString()).build();
            } else {
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("rc", "40002");
                errorResponse.put("rcDesc", "FAILED");
                errorResponse.put("message", "Failed to delete account.");
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse.toString()).build();
            }
        } catch (Exception e) {
            log.error("ERROR:", e);
            ResUserAccount res = new ResUserAccount();
            res.setRc("50005");
            res.setRcDesc("Internal Server Error");
            res.setMessage("An unexpected error occurred while processing the request.");
            return Response.status(200).entity(res).build();
        }
        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAccount(ReqUserAccount reqUserAccount) {
        DatabaseFactory databaseFactory = new DatabaseFactory();
        ResUserAccount res = new ResUserAccount();

        // Check if the required parameters are provided
        if (reqUserAccount.getUsername() == null || reqUserAccount.getUsername().isEmpty()) {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("rc", "40000");
            errorResponse.put("rcDesc", "BAD REQUEST");
            errorResponse.put("message", "Username must be filled.");
        }

        // Call the method to update the bank account
        boolean updateSuccess = databaseFactory.updateUserAccount(reqUserAccount);

        // Check if the bank account was successfully updated
        if (updateSuccess) {
           JSONObject successResponse = new JSONObject();
            successResponse.put("rc", "20000");
            successResponse.put("rcDesc", "SUCCESS.");
            successResponse.put("message", "User account updated succesfully.");
            return Response.status(Response.Status.OK).entity(successResponse.toString()).build();
        } else {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("rc", "40002");
            errorResponse.put("rcDesc", "FAILED");
            errorResponse.put("message", "Failed to update account.");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse.toString()).build();
        }
    }
}
