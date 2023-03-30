package com.SchoolioApi.controllers;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;

import static com.SchoolioApi.oauth.Token.getToken;

public class TokenFromCreds implements Route {
    @Override
    public Object handle(Request request, Response response) throws IOException {
        String username = request.queryParams("username");
        String password = request.queryParams("password");

        HttpResponse httpResponse;
        try {
            httpResponse = getToken(username, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        response.status(httpResponse.getStatusLine().getStatusCode());
        return EntityUtils.toString(httpResponse.getEntity());
    }
}
