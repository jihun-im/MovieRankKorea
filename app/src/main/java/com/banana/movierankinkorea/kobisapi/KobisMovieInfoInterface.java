package com.banana.movierankinkorea.kobisapi;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.banana.movierankinkorea.Constants.KOBIS_MOVIE_INFO_URL;

/**
 * Created by Jihun on 2017-07-08.
 */

public interface KobisMovieInfoInterface {

    //since Kobis has really bad format of JSON result. I use JsonObject in the class KobisMovie
    @GET("boxoffice/searchDailyBoxOfficeList.json")
    Call<KobisMovie> getDailyMovieRank(@Query("key") String apiKey, @Query("targetDt") String targetDate);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(KOBIS_MOVIE_INFO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
