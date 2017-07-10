package com.banana.movierankinkorea.daumapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jihun on 2017-07-09.
 */

public class Grade {
    //In ther mother list, first index indicates grade, and, second index indicates count
    @SerializedName("content")
    public String gradeOrCount;
}
