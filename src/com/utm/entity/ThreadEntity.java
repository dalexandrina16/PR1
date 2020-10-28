package com.utm.entity;

import com.utm.service.DataAccessService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ThreadEntity extends Thread {

    private String homeUrl;

    public ThreadEntity(String newUrl) {
        this.homeUrl = newUrl;
    }

    @Override
    public void run() {
        super.run();
        try {
            URL connectionUrl = new URL(homeUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) connectionUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("X-Access-Token", new DataAccessService().getToken());
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            StringBuilder content = new StringBuilder();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                content.append(inputLine);
            }
            in.close();
            System.out.println(content);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
