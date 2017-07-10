package com.banana.movierankinkorea.controller;

import com.banana.movierankinkorea.daumapi.DaumMovie;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Jihun on 2017-07-10.
 */

public class PopularComparator implements Comparator<DaumMovie> {
    private ArrayList<String> nameOrderBuffer;

    private PopularComparator() {
        nameOrderBuffer = new ArrayList<String>();
    }
    private static class SingletonHelper {
        private static final PopularComparator INSTANCE = new PopularComparator();
    }
    public static PopularComparator getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public ArrayList<String> getNameOrderBuffer() {
        return nameOrderBuffer;
    }

    @Override
    public int compare(DaumMovie d1, DaumMovie d2) {
        if (nameOrderBuffer.indexOf(d1.getTitle()) < nameOrderBuffer.indexOf(d2.getTitle())){
            return -1;
        } else {
            return 0;
        }
    }
}
