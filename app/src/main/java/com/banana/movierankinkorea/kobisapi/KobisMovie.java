package com.banana.movierankinkorea.kobisapi;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jihun on 2017-07-08.
 */

public class KobisMovie {

    //since Kobis has really bad format of JSON result. I use JsonObject

    @SerializedName("boxOfficeResult")
    public JsonObject kobisMovie;

    @Override
    public String toString() {
        return "KobisMovie: " + kobisMovie.toString();
    }
}
