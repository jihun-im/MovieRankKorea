package com.banana.movierankinkorea.daumapi;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.banana.movierankinkorea.Constants.DAUM_MOVIE_API_URL;

/**
 * Created by Jihun on 2017-07-08.
 */

public interface DaumMovieApiInterface {

    @GET("contents/movie")
    Call<DaumMovie> getMovieInfo(@Query("apikey") String apiKey, @Query("output") String outputType, @Query("q") String query);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(DAUM_MOVIE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
