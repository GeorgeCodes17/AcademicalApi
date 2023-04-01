package com.SchoolioApi.controllers;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;

import static com.SchoolioApi.okta.TokenOkta.getToken;

public class BearerByRefresh implements Route {
    @Override
    public Object handle(Request request, Response response) throws IOException {
        String refreshToken = request.headers("Authorization");

        HttpResponse httpResponse;
        try {
            httpResponse = getToken(refreshToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        response.status(httpResponse.getStatusLine().getStatusCode());
        return EntityUtils.toString(httpResponse.getEntity());
    }
}
