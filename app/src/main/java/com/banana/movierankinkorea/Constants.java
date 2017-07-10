package com.banana.movierankinkorea;


import android.media.audiofx.BassBoost;
import android.provider.ContactsContract;
import android.provider.Settings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Jihun on 2017-07-08.
 */

public class Constants {

    final static public int NUMBER_OF_COLS_IN_GRID = 2;
    final static public String KOBIS_MOVIE_INFO_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/";
    final static public String DAUM_MOVIE_API_URL = "https://apis.daum.net/";

    //155716

    public static String getYesterday() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String date = df.format(cal.getTime());
        return date;
    }
}
