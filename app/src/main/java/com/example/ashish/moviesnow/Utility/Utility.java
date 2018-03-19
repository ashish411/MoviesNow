package com.example.ashish.moviesnow.Utility;

import android.net.Uri;

import com.example.ashish.moviesnow.BuildConfig;

/**
 * Created by Ashish on 12-03-2018.
 */

public class Utility {


    public static String getCastUrl(int movieId) {
        // https://api.themoviedb.org/3/movie/284054/credits?api_key=c686b5d39204b19a48fb9a27f5457a41
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(String.valueOf(movieId))
                .appendPath("credits")
                .appendQueryParameter("api_key", BuildConfig.MY_MOVIE_DB_API_KEY);
        return builder.build().toString();
    }

    public static String getMovieDetailUrl(int movieId) {
        if (movieId != 0) {
            //https://api.themoviedb.org/3/movie/335777?api_key=c686b5d39204b19a48fb9a27f5457a41&language=en-US
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(String.valueOf(movieId))
                    .appendQueryParameter("api_key", BuildConfig.MY_MOVIE_DB_API_KEY)
                    .appendQueryParameter("language", "en-US");
            return builder.build().toString();
        }
        return null;
    }


    public static String getVideosLink(int movieId) {
        if (movieId != 0) {
            //https://api.themoviedb.org/3/movie/354912/videos?api_key=c686b5d39204b19a48fb9a27f5457a41&language=en-US
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(String.valueOf(movieId))
                    .appendPath("videos")
                    .appendQueryParameter("api_key", BuildConfig.MY_MOVIE_DB_API_KEY)
                    .appendQueryParameter("language", "en-US");
            return builder.build().toString();
        }
        return null;

    }

    public static String getReviewsLink(int movieId) {
        if (movieId != 0) {
//          https://api.themoviedb.org/3/movie/284054/reviews?api_key=c686b5d39204b19a48fb9a27f5457a41&language=en-US
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(String.valueOf(movieId))
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", BuildConfig.MY_MOVIE_DB_API_KEY)
                    .appendQueryParameter("language", "en-US");
            return builder.build().toString();
        }
        return null;

    }
}
