package com.example.ashish.moviesnow.Model;

/**
 * Created by ashis on 2/13/2018.
 */

public class Series {
    private String mSeriesName;
    private String mSeriesPoster;

    public Series(String mSeriesName, String mSeriesPoster) {
        this.mSeriesName = mSeriesName;
        this.mSeriesPoster = mSeriesPoster;
    }

    public String getmSeriesName() {
        return mSeriesName;
    }

    public String getmSeriesPoster() {
        return mSeriesPoster;
    }
}
