package com.example.ashish.moviesnow.Model;

/**
 * Created by ashis on 3/3/2018.
 */

public class DetailInf {
    private String moviePoster;
    private int runtime;
    private String releaseDate;
    private String overview;
    private String[] genre;
    private String title;

    public DetailInf(String moviePoster, int runtime, String releaseDate, String overview,
                     String[] genre, String title) {
        this.moviePoster = moviePoster;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.genre = genre;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String[] getGenre() {
        return genre;
    }
}
