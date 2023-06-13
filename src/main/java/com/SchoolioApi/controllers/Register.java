package com.SchoolioApi.controllers;

import com.SchoolioApi.Main;
import com.SchoolioApi.exceptions.RegisterUserException;
import com.SchoolioApi.helpers.JsonConverter;
import com.SchoolioApi.helpers.UrlEncodedConverter;
import com.SchoolioApi.okta.RegisterOkta;
import com.SchoolioApi.okta.TokenOkta;
import com.SchoolioApi.objects.Account;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Level;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.util.Map;

public class Register implements Route {
    private final JsonConverter jsonConverter = new JsonConverter();
    private final UrlEncodedConverter urlEncodedConverter = new UrlEncodedConverter();

    @Override
    public Object handle(Request request, Response response) {
        Map<String,String> accountMap = urlEncodedConverter.convert(request.body());
        Account account = new Account(
                accountMap.get("username"),
                accountMap.get("password"),
                accountMap.get("first_name"),
                accountMap.get("last_name")
        );

        HttpResponse registerResponse;
        try {
            registerResponse = RegisterOkta.registerUserToAuthProvider(account);
            int registerStatus = registerResponse.getStatusLine().getStatusCode();
            if (registerStatus != HttpStatus.SC_OK) {
                response.status(registerStatus);
                return EntityUtils.toString(registerResponse.getEntity());
            }
        } catch (IOException e) {
            RegisterUserException registerUserException = new RegisterUserException("Failed to register the user to Okta auth provider", e);
            Main.logAll(Level.ERROR, registerUserException);
            return registerUserException;
        }

        try {
            String payload = EntityUtils.toString(registerResponse.getEntity());
            String userId = jsonConverter.toHashmap(payload).get("id").toString();

            HttpResponse assignResponse = RegisterOkta.assignUserToApp(userId);
            int assignStatus = assignResponse.getStatusLine().getStatusCode();
            if (assignStatus != HttpStatus.SC_OK) {
                response.status(assignStatus);
                return EntityUtils.toString(registerResponse.getEntity());
            }
        } catch (IOException e) {
            RegisterUserException registerUserException = new RegisterUserException("Failed to assign user to the app", e);
            Main.logAll(Level.ERROR, registerUserException);
            return registerUserException;
        }

        try {
            HttpResponse getTokenResponse = TokenOkta.getToken(account);
            int getTokenStatus = getTokenResponse.getStatusLine().getStatusCode();
            response.status(getTokenStatus);
            return EntityUtils.toString(getTokenResponse.getEntity());
        } catch (IOException e) {
            RegisterUserException registerUserException = new RegisterUserException("Failed to assign user to the app", e);
            Main.logAll(Level.ERROR, registerUserException);
            return registerUserException;
        }
    }
}
