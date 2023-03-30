package com.SchoolioApi.controllers;

import com.SchoolioApi.helpers.JsonTransformer;
import com.SchoolioApi.oauth.Token;
import com.SchoolioApi.objects.Account;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        int registerStatus = registerResponse.getStatusLine().getStatusCode();
        if (registerStatus != 200) {
            response.status(registerStatus);
            return EntityUtils.toString(registerResponse.getEntity());
        }

        String payload = EntityUtils.toString(registerResponse.getEntity());
        String userId = new JsonTransformer().render(payload).get("id").toString();

        HttpResponse assignResponse = assignUserToApp(userId);
        int assignStatus = assignResponse.getStatusLine().getStatusCode();
        if (assignStatus != 200) {
            response.status(assignStatus);
            return EntityUtils.toString(registerResponse.getEntity());
        }

        HttpResponse getTokenResponse = Token.getToken(account.username(), account.password());
        int getTokenStatus = getTokenResponse.getStatusLine().getStatusCode();
        response.status(getTokenStatus);
        return EntityUtils.toString(getTokenResponse.getEntity());
    }

    private void convertUrlEncodedToMap(String urlEncoded) {
        String encodedParams;
        encodedParams = URLDecoder.decode(urlEncoded, StandardCharsets.UTF_8);

        String[] params = encodedParams.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            accountMap.put(keyValue[0], keyValue[1]);
        }
    }
}
