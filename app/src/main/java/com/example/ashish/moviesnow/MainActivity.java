package com.example.ashish.moviesnow;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ashish.moviesnow.Model.Movie;
import com.example.ashish.moviesnow.adapters.MovieListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMovieListsView;
    private MovieListAdapter movieListAdapter,seriesAdapter;
    private List<Movie> movieList,seriesList;
    private BottomNavigationView movieNavBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList = new ArrayList<>();
        seriesList = new ArrayList<>();
        mMovieListsView = (RecyclerView)findViewById(R.id.movieListRecyclerView);
        mMovieListsView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        mMovieListsView.setLayoutManager(layoutManager);

        movieNavBar = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        Toast.makeText(getApplicationContext(),"Long press the movie to view its name",Toast.LENGTH_LONG).show();

        final String queryUrl = getMovieUrl();
        requestMovies(queryUrl);

        movieNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.moviesItem:
                        seriesList.clear();
                        if (movieList.size() > 0)
                            movieList.clear();
                        movieListAdapter.notifyDataSetChanged();
                        requestMovies(queryUrl);
                        break;
                    case R.id.tvItem:
                        movieList.clear();
                        if (seriesList.size()>0)
                            seriesList.clear();
                        movieListAdapter.notifyDataSetChanged();
                        String seriesUrl = getSeriesUrl();
                        requestSeries(seriesUrl);
                        break;
                    case R.id.dummyItem:
                        Toast.makeText(getApplicationContext(),"Dummy bar",Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });

    }

    private void requestSeries(String seriesUrl) {
        StringRequest sr = new StringRequest(Request.Method.GET, seriesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getSeriesList(response);

                seriesAdapter = new MovieListAdapter(getApplicationContext(), seriesList);
                mMovieListsView.setAdapter(seriesAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);
    }

    private void requestMovies(String mUrl){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getMovieList(response);

                movieListAdapter = new MovieListAdapter(getApplicationContext(), movieList);
                mMovieListsView.setAdapter(movieListAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }


    private void getSeriesList(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            if (resultsArray.length() != 0){
                for (int i=0;i<resultsArray.length();i++){
                    JSONObject obj = resultsArray.getJSONObject(i);
                    int movieId = obj.getInt("id");
                    String seriesName = obj.getString("original_name");
                    String seriesPoster = obj.getString("poster_path");
                    String baseUrl = "https://image.tmdb.org/t/p/w500";
                    StringBuilder seriesUrlBuilder = new StringBuilder();
                    seriesUrlBuilder.append(baseUrl).append(seriesPoster);
                    seriesList.add(new Movie(movieId,seriesName,seriesUrlBuilder.toString()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getMovieList(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray resultArray = jsonObject.getJSONArray("results");
            if (resultArray.length()!=0){
                for (int i=0;i<resultArray.length();i++){
                    JSONObject obj = resultArray.getJSONObject(i);
                    int movieId = obj.getInt("id");
                    String movieName = obj.getString("title");
                    String moviePoster = obj.getString("poster_path");
                    String movieBaseUrl = "https://image.tmdb.org/t/p/w500";
                    StringBuilder movieUrlBuilder = new StringBuilder();
                    movieUrlBuilder.append(movieBaseUrl).append(moviePoster);
                    movieList.add(new Movie(movieId,movieName,movieUrlBuilder.toString()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getSeriesUrl(){

        //https://api.themoviedb.org/3/tv/popular?api_key=c686b5d39204b19a48fb9a27f5457a41&language=en-US&page=1
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("tv")
                .appendPath("popular")
                .appendQueryParameter("api_key",BuildConfig.MY_MOVIE_DB_API_KEY)
                .appendQueryParameter("language","en-US");
        return builder.build().toString();
    }

    private String getMovieUrl() {

//        for (int i=1;i<=10;i++)
//            list.add(new Movie("Deadpool 2",
//                "http://image10.bizrate-images.com/resize?sq=60&uid=2216744464"));
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("now_playing")
                .appendQueryParameter("api_key",BuildConfig.MY_MOVIE_DB_API_KEY);
        String queryUrl = builder.build().toString();
        return queryUrl;
    }

}
