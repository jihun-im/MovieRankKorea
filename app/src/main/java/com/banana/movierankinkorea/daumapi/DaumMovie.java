package com.banana.movierankinkorea.daumapi;

import java.util.List;

/**
 * Created by Jihun on 2017-07-08.
 */

public class DaumMovie {
    private Channel channel;
    private String movieTitle;

    public String getReleaseDate() {
        return this.channel.items.get(0).openInfoList.get(0).releaseDate;
    }
    public String getGrade() {
        return this.channel.items.get(0).gradesList.get(0).gradeOrCount;
    }
    public String getGraderCount() {
        return this.channel.items.get(0).gradesList.get(1).gradeOrCount;
    }

    public String getStory() {
        return this.channel.items.get(0).storyList.get(0).story;
    }

    public void setMovieTitle(String title) {
        movieTitle = title;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getTitle() {
        return movieTitle;
    }

    public List<TrailerVideo> getTrailerList() {
        return this.channel.items.get(0).trailerVideoList;
    }

    public String getPosterUrl() {
        return this.channel.items.get(0).thumbnailList.get(0).posterUrl;
    }
}
