package com.revature.app.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


// WATCH LECTURE
// 2-23-22 02:18:45PM -- LAST 5 MINUTES FOR BETTER UNDERSTANDING
public class PrismService {
    public void registerNewOrganizationUsingPrism(){

        // HIGH LEVEL - talking to prism without Servlets
        // set url to send request to, we make a connection, specify http method for our request, adding headers to
        // the request, writing to the
        // request body, and we are reading the response

        try {
            String baseUrl = "http://localhost:5000/prism";

            // Endpoint we are trying to talk to
            URL prismRegisterUrl = new URL(baseUrl + "/organizations");

            // Create a connection to send the request and receive a response
            HttpURLConnection registerConnection = (HttpURLConnection) prismRegisterUrl.openConnection();

            // Set the HTTP method you wish to use for this request
            registerConnection.setRequestMethod("POST");

            // HEADERS as key:value
            // details about what we are sending in this request
            registerConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            // details about what content type we will accept in response
            registerConnection.setRequestProperty("Accept", "application/json");

            // To send a request body, enable doOutput (not necessary if request body is not being sent)
            registerConnection.setDoOutput(true);

            // Define the data we want to send
            // todo rename to payload and make a class for- Register Org, Authenticate Org, Add Employee, Post Payment
            String newOrgRegistrationRequestPayload = "{\"name\": \"Abhilekh-Org\", \"key\": \"super-secret-key\"}";


            // this type of try block will close OutputStream automatically
            // will attact payload data to the request and send it
            try (OutputStream os = registerConnection.getOutputStream()){
                // turn json string into bytes so we can invoke os.write()
                byte[] payloadBytes = newOrgRegistrationRequestPayload.getBytes(StandardCharsets.UTF_8);
                // os.write() accepts bytes
                os.write(payloadBytes);
            }

            // Read the response from the connection input's stream - should return orgId and authCode
            try (BufferedReader br = new BufferedReader(new InputStreamReader(registerConnection.getInputStream(),
                    StandardCharsets.UTF_8))){
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine())!=null){
                    response.append(line.trim());
                }

                // todo parse this JSON into something useful
                // jackson will do it in the future
                System.out.println(response);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void authenticateOrganizationUsingPrism(){}

    public void registerNewEmployeeUsingPrism(){}

    public void postPaymentUsingPrism(){}

}
