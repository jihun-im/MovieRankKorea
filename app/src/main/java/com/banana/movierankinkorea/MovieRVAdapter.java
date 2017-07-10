package com.banana.movierankinkorea;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.banana.movierankinkorea.activities.MovieDetailActivity;
import com.banana.movierankinkorea.daumapi.DaumMovie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jihun on 2017-07-08.
 */

public class MovieRVAdapter extends RecyclerView.Adapter<MovieRVAdapter.MovieViewHolder> {
    private static MovieRVAdapter mMovieRVAdapter;
    private List<DaumMovie> mDaumMovieList;
    private Context appContext;
    private int lastClickedPosition;

    public MovieRVAdapter() {
        mMovieRVAdapter = this;
    }

    public static MovieRVAdapter getInstance() {
        return mMovieRVAdapter;
    }
    public List<DaumMovie> getDaumMovieList() {
        return mDaumMovieList;
    }

    public void setMovieList(List<DaumMovie> mDaumMovieList) {
        this.mDaumMovieList = mDaumMovieList;
        notifyDataSetChanged();
    }

    public int getLastClickedPosition() {
        return lastClickedPosition;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_image_view)
        ImageView movieImageView;
        @BindView(R.id.movie_titlebackground_view)
        View titleBackgroundView;
        @BindView(R.id.movie_titme_text_view)
        TextView movieTitleTextView;
        DaumMovie daumMovie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            lastClickedPosition = getAdapterPosition();
            appContext.startActivity(new Intent(appContext, MovieDetailActivity.class));
        }
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        appContext = parent.getContext();
        View rootView = LayoutInflater.from(appContext).inflate(R.layout.movie_grid_item, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(rootView);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        holder.itemView.setOnClickListener(holder);
        holder.daumMovie = mDaumMovieList.get(position);
        holder.movieTitleTextView.setText(1+position + ". " + holder.daumMovie.getTitle());
        Glide.with(appContext).load(holder.daumMovie.getPosterUrl()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new BitmapImageViewTarget(holder.movieImageView) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        super.onResourceReady(bitmap, anim);

                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
//                                if (palette != null) {
//                                    //int viewColor = palette.getVibrantColor(ContextCompat.getColor(appContext, R.color.black_translucent_60));
//                                    //holder.titleBackgroundView.setBackgroundColor(viewColor);
//                                    Palette.Swatch swatch = palette.getVibrantSwatch();
//                                    if(swatch !=null) {
//                                        holder.titleBackgroundView.setBackgroundColor(palette.getVibrantSwatch().getRgb());
//                                        holder.movieTitleTextView.setTextColor(palette.getMutedSwatch().getRgb());
//                                    }
//                                }
                                holder.titleBackgroundView.setBackgroundColor(palette.getVibrantColor(ContextCompat.getColor(appContext, R.color.black_translucent_60)));

                            }
                        });
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (mDaumMovieList != null) {
            return mDaumMovieList.size();
        } else {
            return 0;
        }
    }


}
