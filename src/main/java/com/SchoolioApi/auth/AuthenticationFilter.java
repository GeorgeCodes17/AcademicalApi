package com.SchoolioApi.auth;

import com.SchoolioApi.Main;
import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.JwtVerificationException;
import com.okta.jwt.JwtVerifiers;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Level;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.util.List;

import static com.SchoolioApi.helpers.ConfigFile.config;
import static spark.Spark.halt;

public class AuthenticationFilter implements Filter {
    private final List<String> IGNORE_ENDPOINTS = List.of(
            "/register",
            "/get-bearer-by-creds",
            "/get-bearer-by-refresh"
    );
    private final String authUrl = config().getProperty("AUTH_URL");

    @Override
    public void handle(Request request, Response response) {
        String requestedEndpoint = request.pathInfo();
        if (IGNORE_ENDPOINTS.contains(requestedEndpoint)) {
            return;
        }

        String accessToken = request.headers("Authorization");
        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer(authUrl + "/oauth2/default")
                .build();
        try {
            jwtVerifier.decode(accessToken);
        } catch (JwtVerificationException e) {
            Main.logAll(Level.TRACE, e);
            halt(HttpStatus.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
