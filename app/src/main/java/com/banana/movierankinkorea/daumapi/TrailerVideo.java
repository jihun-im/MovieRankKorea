package com.banana.movierankinkorea.daumapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jihun on 2017-07-09.
 */

public class TrailerVideo {
    @SerializedName("content")
    public String trailerThumbnailUrl;
    @SerializedName("link")
    public String trailerLinkUrl;
}
