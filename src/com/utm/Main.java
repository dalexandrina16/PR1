package com.utm;

import com.utm.entity.ThreadEntity;
import com.utm.service.DataAccessService;

import java.io.IOException;

public class Main {

    private static final String urlHome = "http://localhost:5000/register";

    public static void main(String[] args) throws IOException {
        new DataAccessService().getAccessToken(urlHome);
        new ThreadEntity(urlHome).run();
    }
}
