package com.example.androiddingjing.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsList {

    public int code;

    public String msg;

    @SerializedName("newslist")
    public List<News> newsList ;

}
