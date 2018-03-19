package com.example.ashish.moviesnow.Model;

/**
 * Created by Ashish on 12-03-2018.
 */

public class Reviews {
    private String mAuther;
    private String mContent;
    private String mUrlLink;

    public Reviews(String mAuther, String mContent, String mUrlLink) {
        this.mAuther = mAuther;
        this.mContent = mContent;
        this.mUrlLink = mUrlLink;
    }

    public String getmAuther() {
        return mAuther;
    }

    public String getmContent() {
        return mContent;
    }

    public String getmUrlLink() {
        return mUrlLink;
    }
}
