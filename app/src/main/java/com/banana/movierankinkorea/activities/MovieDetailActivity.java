package com.banana.movierankinkorea.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.banana.movierankinkorea.MovieRVAdapter;
import com.banana.movierankinkorea.R;
import com.banana.movierankinkorea.controller.FavoriteFloatingButtonAction;
import com.banana.movierankinkorea.daumapi.DaumMovie;
import com.banana.movierankinkorea.daumapi.TrailerVideo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jihun on 2017-07-09.
 */

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.movie_poster)
    ImageView poster;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.movie_name)
    TextView title;
    @BindView(R.id.movie_year)
    TextView releaseDate;
    @BindView(R.id.movie_rating)
    TextView rating;
    @BindView(R.id.movie_description)
    TextView overview;
    @BindView(R.id.trailers_label)
    TextView label;
    @BindView(R.id.trailer_layout)
    LinearLayout trailerLayout;
    @BindView(R.id.trailers_container)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.reviews_label)
    TextView reviews;
    @BindView(R.id.reviews)
    LinearLayout reviewsContainer;
    @BindView(R.id.favorite)
    FloatingActionButton favorite;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    private List<DaumMovie> mDaumMovieList;
    private MovieRVAdapter mMovieRVAdapter;
    Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("aaa", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setToolbar();
    }

    @Override
    protected void onResume() {
        Log.d("aaa", "onResume");
        mMovieRVAdapter = MovieRVAdapter.getInstance();
        mDaumMovieList = mMovieRVAdapter.getDaumMovieList();
        setContents(mDaumMovieList.get(mMovieRVAdapter.getLastClickedPosition()));
        super.onResume();
    }

    private void setContents(DaumMovie daumMovie) {
        Glide.with(this).load(daumMovie.getPosterUrl()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(poster);
        title.setText(daumMovie.getTitle());
        releaseDate.setText(getString(R.string.release_date) + ": " + daumMovie.getReleaseDate());
        ratingBar.setStepSize(0.1f);
        ratingBar.setRating((float) (Float.parseFloat(daumMovie.getGrade())/2f));
        rating.setText(String.format(getString(R.string.grading), String.valueOf(daumMovie.getGrade())) + " " + String.format(getString(R.string.grader_count), String.valueOf(daumMovie.getGraderCount())));
        overview.setText(daumMovie.getStory());
        showTrailers(daumMovie.getTrailerList());
        FavoriteFloatingButtonAction.INSTANCE.synchronizeButton(daumMovie, favorite);
//        movieDetailsPresenter.showReviews(movie);
    }

    private void showTrailers(List<TrailerVideo> trailerVideoList) {

        if (trailerVideoList.isEmpty()) {
            label.setVisibility(View.GONE);
            this.trailerLayout.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);

        } else {
            label.setVisibility(View.VISIBLE);
            this.trailerLayout.setVisibility(View.VISIBLE);
            horizontalScrollView.setVisibility(View.VISIBLE);

            this.trailerLayout.removeAllViews();
            LayoutInflater inflater = this.getLayoutInflater();
            Picasso picasso = Picasso.with(this);
            for (TrailerVideo trailer : trailerVideoList) {
                View thumbContainer = inflater.inflate(R.layout.video, this.trailerLayout, false);
                ImageView thumbView = ButterKnife.findById(thumbContainer, R.id.video_thumb);
                thumbView.setTag(trailer.trailerLinkUrl);
                thumbView.requestLayout();
                thumbView.setOnClickListener(this);
                picasso
                        .load(trailer.trailerThumbnailUrl)
                        .resizeDimen(R.dimen.video_width, R.dimen.video_height)
                        .centerCrop()
                        .placeholder(R.color.colorPrimary)
                        .into(thumbView);
                this.trailerLayout.addView(thumbContainer);
            }
        }
    }

    private void setToolbar() {
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary));
        collapsingToolbar.setTitle(getString(R.string.movie_details));
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null) {
            this.setSupportActionBar(toolbar);

            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else {
            // Don't inflate. Tablet is in landscape mode.
        }
    }

    @Override
    public void onClick(View view) {
        //thumbnail imageview is clicked
        String videoUrl = (String) view.getTag();
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }

    public void onFavoritClicked(View view) {
        FavoriteFloatingButtonAction.INSTANCE.buttonClicked(mDaumMovieList.get(mMovieRVAdapter.getLastClickedPosition()), view);
    }



}
