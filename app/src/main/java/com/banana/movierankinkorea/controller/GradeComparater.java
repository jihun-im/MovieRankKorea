package com.banana.movierankinkorea.controller;

import com.banana.movierankinkorea.daumapi.DaumMovie;

import java.util.Comparator;

/**
 * Created by Jihun on 2017-07-10.
 */

public class GradeComparater implements Comparator<DaumMovie> {
    @Override
    public int compare(DaumMovie d1, DaumMovie d2) {
        if (Float.parseFloat(d1.getGrade()) > Float.parseFloat(d2.getGrade())) {
            return -1;
        } else {
            return 0;
        }
    }
}
