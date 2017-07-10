package com.banana.movierankinkorea.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.banana.movierankinkorea.R;
import com.banana.movierankinkorea.daumapi.DaumMovie;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jihun on 2017-07-09.
 */

public enum FavoriteFloatingButtonAction {
    INSTANCE;
    private static final String PREF_NAME = "FavoritesStore";
    private SharedPreferences pref;
    Context context;


    public void initialize(Context context) {
        if (pref == null) {
            Log.d("aaa", "pref");
            pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        if (this.context == null) {
            Log.d("aaa", "context");
            this.context = context;
        }
    }

    public void synchronizeButton(DaumMovie daumMovie, FloatingActionButton button) {
        Log.d("aaa", "synchronizeButton start " + daumMovie.getTitle());
        if (isFavorite(daumMovie)) {
            showFavorited(button);
        } else {
            showUnFavorited(button);
        }
    }


    public void buttonClicked(DaumMovie daumMovie, View view) {
        if (isFavorite(daumMovie)) {
            deleteFavorite(daumMovie);
            showUnFavorited(view);
        } else {
            storeFavorite(daumMovie);
            showFavorited(view);
        }
    }

    public List<DaumMovie> getFavorites() throws IOException {
        Map<String, ?> allEntries = pref.getAll();
        ArrayList<DaumMovie> daumMovieList = new ArrayList<>();
        Moshi moshi = new Moshi.Builder().build();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String movieJson = pref.getString(entry.getKey(), null);

            if (!TextUtils.isEmpty(movieJson)) {
                JsonAdapter<DaumMovie> jsonAdapter = moshi.adapter(DaumMovie.class);

                DaumMovie daumMovie = jsonAdapter.fromJson(movieJson);
                daumMovieList.add(daumMovie);
            } else {
                // Do nothing;
            }
        }
        return daumMovieList;
    }

    private void storeFavorite(DaumMovie daumMovie) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<DaumMovie> jsonAdapter = moshi.adapter(DaumMovie.class);
        String daumMovieJson = jsonAdapter.toJson(daumMovie);
        pref.edit().putString(daumMovie.getTitle(), daumMovieJson).apply();
    }

    private void deleteFavorite(DaumMovie daumMovie) {
        pref.edit().remove(daumMovie.getTitle()).apply();
    }

    public boolean isFavorite(DaumMovie daumMovie) {
        return pref.contains(daumMovie.getTitle());
    }

    private void showFavorited(View view) {
        ((FloatingActionButton) view).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_white_24dp));
    }

    private void showUnFavorited(View view) {
        ((FloatingActionButton) view).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_white_24dp));
    }
}
