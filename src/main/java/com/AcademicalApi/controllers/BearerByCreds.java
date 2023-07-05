package com.AcademicalApi.controllers;

import com.AcademicalApi.Main;
import com.AcademicalApi.exceptions.GetBearerException;
import com.AcademicalApi.helpers.UrlEncodedConverter;
import com.AcademicalApi.objects.Account;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Level;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.util.Map;

import static com.AcademicalApi.okta.TokenOkta.getToken;

public class BearerByCreds implements Route {
    private final UrlEncodedConverter urlEncodedConverter = new UrlEncodedConverter();
    @Override
    public Object handle(Request request, Response response) throws IOException {
        Map<String,String> accountMap = urlEncodedConverter.convert(request.body());
        Account account = new Account(
                accountMap.get("username"),
                accountMap.get("password")
        );

        HttpResponse httpResponse;
        try {
            httpResponse = getToken(account);
        } catch (IOException e) {
            Main.logAll(Level.WARN, new GetBearerException("Failed to get bearer using user creds", e));
            response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return e.getMessage();
        }
        response.status(httpResponse.getStatusLine().getStatusCode());
        return EntityUtils.toString(httpResponse.getEntity());
    }
}
