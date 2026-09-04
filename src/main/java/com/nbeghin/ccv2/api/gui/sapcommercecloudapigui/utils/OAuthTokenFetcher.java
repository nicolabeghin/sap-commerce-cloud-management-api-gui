package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OAuthTokenFetcher {

    private static final String TOKEN_URL = "https://ycloud.accounts.ondemand.com/oauth2/token";
    private static final String RESOURCE = "urn:sap:identity:application:provider:name:cp-dependency";

    private static final OkHttpClient client = new OkHttpClient();

    public static String fetchBearerToken(String clientId, String clientSecret) throws IOException {
        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(new FormBody.Builder()
                        .add("grant_type", "client_credentials")
                        .add("client_id", clientId)
                        .add("client_secret", clientSecret)
                        .add("resource", RESOURCE)
                        .build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
                throw new IOException("Token fetch failed (HTTP " + response.code() + "): " + body);
            }
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            if (!json.has("access_token")) {
                throw new IOException("Token response missing access_token: " + body);
            }
            return json.get("access_token").getAsString();
        }
    }
}
