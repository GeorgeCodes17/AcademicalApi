package com.SchoolioApi.controllers;

import com.SchoolioApi.helpers.UrlEncodedConverter;
import com.SchoolioApi.objects.Account;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.util.Map;

import static com.SchoolioApi.okta.TokenOkta.getToken;

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
            throw new RuntimeException(e);
        }
        response.status(httpResponse.getStatusLine().getStatusCode());
        return EntityUtils.toString(httpResponse.getEntity());
    }
}
