package com.example.ashish.moviesnow.Model;


public class CastDetails {
    private String mCastRealName;
    private String mCastMovieName;
    private String mCastPoster;

    public CastDetails(String mCastRealName, String mCastMovieName, String mCastPoster) {
        this.mCastRealName = mCastRealName;
        this.mCastMovieName = mCastMovieName;
        this.mCastPoster = mCastPoster;
    }

    public String getmCastRealName() {
        return mCastRealName;
    }

    public String getmCastMovieName() {
        return mCastMovieName;
    }

    public String getmCastPoster() {
        return mCastPoster;
    }
}
