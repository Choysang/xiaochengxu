package com.example.androiddingjing.mysql;

public class NetworkSettings {
    private static final String HOST = "82.156.188.205";
    private static final String PORT = "8080";
    public static final String SIGN_IN = "http://" + HOST + ":" + PORT + "/demo/sign/in";
    public static final String SIGN_UP = "http://" + HOST + ":" + PORT + "/demo/sign/up";
    public static final String UPDATE_INFO = "http://" + HOST + ":" + PORT + "/demo/update/userinfo";
    public static final String SEARCH_INFO = "http://" + HOST + ":" + PORT + "/demo/query/queryInfo";
}