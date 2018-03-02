package com.example.ashish.moviesnow;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ashish.moviesnow.Constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class EntertainmentDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_detail);

        int movieId = getIntent().getIntExtra(Constants.MOVIE_ID,0);
        String movieDetailUrl = getMovieDetailUrl(movieId);
        requestMovieDetails(movieDetailUrl);
    }

    private void requestMovieDetails(String movieDetailUrl) {
        StringRequest sr = new StringRequest(Request.Method.GET, movieDetailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    String baseUrl = "https://image.tmdb.org/t/p/w500";
                    String backdropPath = root.getString("backdrop_path");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);
    }

    private String getMovieDetailUrl(int movieId) {
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
}
