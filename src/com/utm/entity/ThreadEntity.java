package com.utm.entity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.utm.service.DataAccessService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadEntity extends Thread {

    private String homeUrl;
    private int level = 0;

    public ThreadEntity(String newUrl) {
        this.homeUrl = newUrl;
    }

    @Override
    public void run() {
        super.run();
        List<String> links = new ArrayList<>();
        try {
            URL connectionUrl = new URL(homeUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) connectionUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("X-Access-Token", new DataAccessService().getToken());
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            String url = "http://localhost:5000/";
            String obtained = getFromConnection(httpURLConnection);
            links.addAll(getAllLinks(obtained, url));
            System.out.println(links); // printing the obtained links
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
        if (!links.isEmpty()) {
            for (int i = level; i < links.size(); i++) {
                ThreadEntity localThread = new ThreadEntity(links.get(i));
                localThread.start();
                level++;
            }
        }
    }

    public String getFromConnection(HttpURLConnection httpURLConnection) throws IOException {
        StringBuilder obtainedBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null){
            obtainedBuilder.append(inputLine);
        }
        in.close();
        return obtainedBuilder.toString();
    }

    public ArrayList<String> getAllLinks(String obtained, String url) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(obtained).getAsJsonObject().get("link");
        ArrayList<String> links = new ArrayList<>();
        Gson gson = new Gson();
        String json;
        json = jsonElement.toString();
        Map<String, Object> map = new HashMap<>();
        map = (Map<String, Object>)gson.fromJson(json, map.getClass());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            links.add(url + entry.getValue());
        }
        return links;
    }

}
