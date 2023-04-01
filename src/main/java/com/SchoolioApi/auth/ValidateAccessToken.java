package com.SchoolioApi.auth;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.JwtVerificationException;
import com.okta.jwt.JwtVerifiers;

import static com.SchoolioApi.helpers.ConfigFile.config;

public class ValidateAccessToken {
    private static final String authUrl = config().getProperty("AUTH_URL");

    public static void isValid(String accessToken) throws JwtVerificationException {
        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer(authUrl + "/oauth2/default")
                .build();

        jwtVerifier.decode(accessToken);
    }
}
