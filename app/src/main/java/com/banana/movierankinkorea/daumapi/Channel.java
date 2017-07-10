package com.banana.movierankinkorea.daumapi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jihun on 2017-07-08.
 */

public class Channel {
    public int result;
    public String title;
    public int page;
    public int totalCount;
    @SerializedName("item")
    public List<Items> items;
}
