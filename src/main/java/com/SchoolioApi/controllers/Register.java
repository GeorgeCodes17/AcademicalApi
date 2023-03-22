package com.SchoolioApi.controllers;

import com.SchoolioApi.helpers.JsonTransformer;
import com.SchoolioApi.oauth.Token;
import com.SchoolioApi.objects.Account;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static com.SchoolioApi.oauth.Register.assignUserToApp;
import static com.SchoolioApi.oauth.Register.registerUserToAuthProvider;

public class Register implements Route {

    Map<String, String> accountMap = new HashMap<>();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        convertUrlEncodedToMap(request.body());
        Account account = new Account(
                accountMap.get("username"),
                accountMap.get("password"),
                accountMap.get("first_name"),
                accountMap.get("last_name")
        );

        HttpResponse registerResponse = registerUserToAuthProvider(account);
        if (registerResponse.getStatusLine().getStatusCode() != 200) {
            return registerResponse;
        }

        String payload = EntityUtils.toString(registerResponse.getEntity());
        String userId = new JsonTransformer().render(payload).get("id").toString();

        HttpResponse assignResponse = assignUserToApp(userId);
        if (assignResponse.getStatusLine().getStatusCode() != 200) {
            return assignResponse;
        }

        return EntityUtils.toString(Token.getToken(account.username(), account.password()).getEntity());
    }

    private void convertUrlEncodedToMap(String urlEncoded) {
        String encodedParams;
        try {
            encodedParams = URLDecoder.decode(urlEncoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String[] params = encodedParams.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            accountMap.put(keyValue[0], keyValue[1]);
        }
    }
}
