package com.banana.movierankinkorea.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.banana.movierankinkorea.Constants;
import com.banana.movierankinkorea.MovieRVAdapter;
import com.banana.movierankinkorea.R;
import com.banana.movierankinkorea.controller.FavoriteFloatingButtonAction;
import com.banana.movierankinkorea.controller.GradeComparater;
import com.banana.movierankinkorea.controller.PopularComparator;
import com.banana.movierankinkorea.daumapi.DaumMovie;
import com.banana.movierankinkorea.daumapi.DaumMovieApiInterface;
import com.banana.movierankinkorea.kobisapi.KobisMovie;
import com.banana.movierankinkorea.kobisapi.KobisMovieInfoInterface;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.grid_recyclerview)
    public RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    final private List<DaumMovie> mMovieList = new ArrayList<>();
    private MovieRVAdapter mMovieRVAdapter;
    private int dialogSelectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FavoriteFloatingButtonAction.INSTANCE.initialize(this);
        mRecyclerViewLayoutManager = new GridLayoutManager(this, Constants.NUMBER_OF_COLS_IN_GRID);
        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieRVAdapter = new MovieRVAdapter();
        mMovieRVAdapter.setMovieList(mMovieList);
        mRecyclerView.setAdapter(mMovieRVAdapter);

        requestMovieRank();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /*
        It requests movie rank information to Kobis api and gets movieTitles.
     */
    private void requestMovieRank() {
        KobisMovieInfoInterface kobisRetrofitInterface = KobisMovieInfoInterface.retrofit.create(KobisMovieInfoInterface.class);

        Call<KobisMovie> kobisCall = kobisRetrofitInterface.getDailyMovieRank(getResources().getString(R.string.kobis_api_key), Constants.getYesterday());
        kobisCall.enqueue(new Callback<KobisMovie>() {
            @Override
            public void onResponse(Call<KobisMovie> call, Response<KobisMovie> response) {
                final JsonArray dailyBoxOfficeList = response.body().kobisMovie.getAsJsonArray("dailyBoxOfficeList");
                String movieTitle;
                for (int i = 0; i < dailyBoxOfficeList.size(); i++) {
//                for (int i = 0; i < 2; i++) {
                    movieTitle = dailyBoxOfficeList.get(i).getAsJsonObject().get("movieNm").toString().replace("\"", "");
                    PopularComparator.getInstance().getNameOrderBuffer().add(movieTitle);
                    DaumMovieApiInterface daumRetrofitInterface = DaumMovieApiInterface.retrofit.create(DaumMovieApiInterface.class);
                    Call<DaumMovie> daumCall = daumRetrofitInterface.getMovieInfo(getResources().getString(R.string.daum_api_key), "json", movieTitle);
                    final String finalMovieTitle = movieTitle;
                    daumCall.enqueue(new Callback<DaumMovie>() {
                        @Override
                        public void onResponse(Call<DaumMovie> call, Response<DaumMovie> response) {
                            DaumMovie daumMovie = response.body();
                            daumMovie.setMovieTitle(finalMovieTitle);
                            if (daumMovie != null) {
                                mMovieList.add(daumMovie);
                                Collections.sort(mMovieList, PopularComparator.getInstance());
                                mMovieRVAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<DaumMovie> call, Throwable t) {
                            Log.d("ccc", "onFailure : " + t.toString());
                        }
                    });
                }
                mMovieRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<KobisMovie> call, Throwable t) {
            }
        });
    }


    /*
        Option item and NavigationItem
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            showDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (dialogSelectedIndex == 2) {
            showFavoriteList();
        }
        super.onResume();
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.dialog_title);

        //list of items
        String[] items = getResources().getStringArray(R.array.sort_list);
        builder.setSingleChoiceItems(items, dialogSelectedIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) { // Popular Sorting
                            dialogSelectedIndex = 0;
                            Collections.sort(mMovieList, PopularComparator.getInstance());
                            mMovieRVAdapter.setMovieList(mMovieList);
                            mMovieRVAdapter.notifyDataSetChanged();
                        } else if (which == 1) { // Grade Sorting
                            dialogSelectedIndex = 1;
                            Collections.sort(mMovieList, new GradeComparater());
                            mMovieRVAdapter.setMovieList(mMovieList);
                            mMovieRVAdapter.notifyDataSetChanged();
                            //Highest Rated
                        } else if (which == 2) {
                            //Favorites
                            dialogSelectedIndex = 2;
                            showFavoriteList();
                        }
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showFavoriteList() {
        try {
            List<DaumMovie> favoritDaumMovieList = FavoriteFloatingButtonAction.INSTANCE.getFavorites();
            mMovieRVAdapter.setMovieList(favoritDaumMovieList);
            mMovieRVAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            try {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.banana.movierankinkorea");
                sendIntent.setType("text/html");
                startActivity(sendIntent);
            } catch (Exception e) {
                Toast.makeText(this, R.string.cannot_share_application, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_github) {
            Intent sendIntent = new Intent();
            sendIntent.setData(Uri.parse("https://github.com/jihunim/MovieRankKorea"));
            sendIntent.setAction(Intent.ACTION_VIEW);
            startActivity(sendIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
