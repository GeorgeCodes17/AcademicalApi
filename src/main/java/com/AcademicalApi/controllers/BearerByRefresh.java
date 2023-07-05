package com.AcademicalApi.controllers;

import com.AcademicalApi.Main;
import com.AcademicalApi.exceptions.GetBearerException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Level;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.AcademicalApi.okta.TokenOkta.getToken;

public class BearerByRefresh implements Route {
    @Override
    public Object handle(Request request, Response response) throws IOException {
        String refreshToken = request.headers("Authorization");

        HttpResponse httpResponse;
        try {
            httpResponse = getToken(refreshToken);
        } catch (IOException | URISyntaxException e) {
            GetBearerException getBearerException = new GetBearerException("Failed to get bearer using refresh token", e);
            Main.logAll(Level.WARN, getBearerException);
            response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return getBearerException;
        }
        response.status(httpResponse.getStatusLine().getStatusCode());
        return EntityUtils.toString(httpResponse.getEntity());
    }
}
