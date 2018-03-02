package com.example.ashish.moviesnow.Model;

/**
 * Created by ashis on 2/8/2018.
 */

public class Movie {

    private int mMovieId;
    private String mMovieName;
    private String mMoviePoster;

    public Movie(int mMovieId, String mMovieName, String mMoviePoster) {
        this.mMovieId = mMovieId;
        this.mMovieName = mMovieName;
        this.mMoviePoster = mMoviePoster;
    }

    public int getmMovieId() {
        return mMovieId;
    }

    public String getmMovieName() {
        return mMovieName;
    }

    public String getmMoviePoster() {
        return mMoviePoster;
    }
}
