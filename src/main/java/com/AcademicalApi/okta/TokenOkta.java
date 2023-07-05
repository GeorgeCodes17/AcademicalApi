package com.AcademicalApi.okta;

import com.AcademicalApi.Main;
import com.AcademicalApi.objects.Account;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.AcademicalApi.helpers.ConfigFile.config;

public class TokenOkta {
    private static final Properties config = config();
    private static final String AUTH_URL = config.getProperty("AUTH_URL");
    private static final String AUTH_USER_CREDS_HEADER = config.getProperty("AUTH_USER_CREDS_HEADER");
    private static final String CLIENT_ID = config.getProperty("CLIENT_ID");
    private static final String CLIENT_SECRET = config.getProperty("CLIENT_SECRET");

    public static HttpResponse getToken(Account account) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(AUTH_URL + "/oauth2/default/v1/token");
        request.setHeader("Authorization", AUTH_USER_CREDS_HEADER);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "password"));
        params.add(new BasicNameValuePair("username", account.username()));
        params.add(new BasicNameValuePair("password", account.password()));
        params.add(new BasicNameValuePair("scope", "openid profile email offline_access"));

        request.setEntity(new UrlEncodedFormEntity(params));
        return client.execute(request);
    }

    public static HttpResponse getToken(String refreshToken) throws IOException, URISyntaxException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost getAuthenticated = new HttpPost(AUTH_URL + "/oauth2/default/v1/token");
        getAuthenticated.addHeader("Content-Type", "application/x-www-form-urlencoded");

        URI uri;
        try {
            uri = new URIBuilder(getAuthenticated.getURI())
                .addParameter("grant_type", "refresh_token")
                .addParameter("client_id", CLIENT_ID)
                .addParameter("client_secret", CLIENT_SECRET)
                .addParameter("refresh_token", refreshToken)
                .build();
        } catch (URISyntaxException e) {
            Main.logAll(Level.WARN, "Failed to process URI a getToken(String) method: " + e);
            throw e;
        }
        getAuthenticated.setURI(uri);

        return client.execute(getAuthenticated);
    }
}
