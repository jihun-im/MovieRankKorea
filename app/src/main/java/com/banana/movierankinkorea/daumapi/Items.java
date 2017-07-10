package com.banana.movierankinkorea.daumapi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jihun on 2017-07-08.
 */

public class Items {
    @SerializedName("thumbnail")
    public List<Thumbnail> thumbnailList;
    @SerializedName("open_info")
    public List<OpenInfo> openInfoList;
    @SerializedName("grades")
    public List<Grade> gradesList;
    @SerializedName("story")
    public List<Story> storyList;
    @SerializedName("video")
    public List<TrailerVideo> trailerVideoList;
}
