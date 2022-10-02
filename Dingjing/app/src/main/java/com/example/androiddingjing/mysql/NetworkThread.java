package com.example.androiddingjing.mysql;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class NetworkThread implements Callable<String> {
    private final String name;
    private final String account;
    private final String password;
    private final String email;
    private final String url;

    public NetworkThread(String name,String account, String password ,String email, String url) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.email = email;
        this.url = url;
    }

    @Override
    public String call() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            String data = "name=" + URLEncoder.encode(name, StandardCharsets.UTF_8.toString()) +
                          "&account=" + URLEncoder.encode(account, StandardCharsets.UTF_8.toString())+
                           "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8.toString()) +
                          "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = new byte[1024];
            int len = connection.getInputStream().read(bytes);
            return new String(bytes, 0, len, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}