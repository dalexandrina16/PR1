package com.utm.service;

import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataAccessService {

    private String token = "";

    public String getToken() {
        return token;
    }

    public void getAccessToken(String rurl) throws IOException {
        String plainData = "";
        URL url = new URL(rurl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        plainData = getTokenInput(httpURLConnection);
        token = new JsonParser()
                .parse(plainData)
                .getAsJsonObject()
                .get("access_token")
                .getAsString();
    }

    String getTokenInput(HttpURLConnection httpURLConnection) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String input;
        while ((input = in.readLine()) != null) {
            sb.append(input);
        }
        in.close();
        return sb.toString();
    }

}
