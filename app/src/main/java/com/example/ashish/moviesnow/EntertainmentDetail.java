package com.example.ashish.moviesnow;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ashish.moviesnow.Constants.Constants;
import com.example.ashish.moviesnow.Model.CastDetails;
import com.example.ashish.moviesnow.Model.DetailInf;
import com.example.ashish.moviesnow.adapters.CastListAdapter;
import com.example.ashish.moviesnow.adapters.GenreListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntertainmentDetail extends AppCompatActivity {

    private DetailInf movieDetails;
    private ImageView moviePoster,mPlayBtn;
    private TextView runtimeText,dorText,overviewText,titleText;
    private ProgressBar progressBar;

    private List<CastDetails> mCastDetailsList;

    private String videoLink = null;
    private RecyclerView genresRecyclerView, castRecyclerView;
    private String[] genresArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_detail);

        mPlayBtn = findViewById(R.id.playButton);
        progressBar = findViewById(R.id.loadingDetails);
        moviePoster = findViewById(R.id.moviePoster);
        runtimeText = findViewById(R.id.runtime);
        dorText= findViewById(R.id.dor);
        overviewText= findViewById(R.id.movieOverview);
        titleText = findViewById(R.id.movieTitle);
        genresRecyclerView = findViewById(R.id.genresRecyclerView);
        castRecyclerView = findViewById(R.id.castRecyclerView);

        mCastDetailsList = new ArrayList<>();
        int movieId = getIntent().getIntExtra(Constants.MOVIE_ID,0);
        String movieDetailUrl = getMovieDetailUrl(movieId);
        String castDetailUrl = getCastUrl(movieId);
        String videoUrlApi = getVideosLink(movieId);
        requestMovieDetails(movieDetailUrl,castDetailUrl,videoUrlApi);

        mPlayBtn.setVisibility(View.GONE);
    }

    private void requestMovieDetails(String movieDetailUrl, String castDetailUrl, final String videoUrlApi) {
        StringRequest movieDetailReq = new StringRequest(Request.Method.GET, movieDetailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    String backdropPath = root.getString("backdrop_path");
                    String movieUrl = Constants.POSTER_BASE_URL+backdropPath;
                    int runtime = root.getInt("runtime");
                    String dor = root.getString("release_date");
                    String overview = root.getString("overview");
                    StringBuilder builder = new StringBuilder();

                    JSONArray genreArray = root.getJSONArray("genres");
                    genresArray = new String[genreArray.length()];
                    if (genreArray.length()>0){
                        for (int i=0;i<genreArray.length();i++){
                            JSONObject obj = genreArray.getJSONObject(i);
                            genresArray[i] = obj.getString("name");
                        }
                    }

                    String title = root.getString("title");
                    movieDetails = new DetailInf(movieUrl,runtime,dor,overview,genresArray,title);

                    Picasso.with(getApplicationContext()).load(movieDetails.getMoviePoster())
                            .into(moviePoster);
                    runtimeText.setText(String.valueOf(movieDetails.getRuntime()) + " min");
                    formatDate(movieDetails.getReleaseDate());
                    overviewText.setText(movieDetails.getOverview());
                    titleText.setText(movieDetails.getTitle());
                    ResizableCustomView.doResizeTextView(overviewText,2,
                            "View More",true);
                    getGenresItems();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        StringRequest castRequest = new StringRequest(Request.Method.GET, castDetailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray castArray = root.getJSONArray("cast");
                    if (castArray.length()>0){
                        for (int i=0;i<5;i++){
                            JSONObject obj = castArray.getJSONObject(i);
                            String profilePath =  obj.getString("profile_path");
                            String castProfileUrl = Constants.CAST_BASE_URL+profilePath;
                            String castRealName = obj.getString("name");
                            String castMovieName = obj.getString("character");
                            mCastDetailsList.add(new CastDetails(castRealName,castMovieName,castProfileUrl));
                        }
                        getMovieCast();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });


        StringRequest videoRequest = new StringRequest(Request.Method.GET, videoUrlApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray resultsArray = root.getJSONArray("results");
                    if (resultsArray.length()>0){
                        StringBuilder builder = new StringBuilder(Constants.VIDEO_LINK_BASE_URL);
                        builder.append(resultsArray.getJSONObject(0).getString("key"));
                        videoLink = builder.toString();
                    }
                    mPlayBtn.setVisibility(View.VISIBLE);

                    moviePoster.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openYoutubeLink(getApplicationContext(),videoLink);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(movieDetailReq);
        queue.add(videoRequest);
        queue.add(castRequest);

    }

    private void openYoutubeLink(Context context, String videoLink) {
        Log.i("TAG",videoLink);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink));
       context.startActivity(appIntent);
    }

    private void getMovieCast() {
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false);
        castRecyclerView.setLayoutManager(lm);
        CastListAdapter adapter = new CastListAdapter(getApplicationContext(),mCastDetailsList);
        castRecyclerView.setAdapter(adapter);
    }

    private void formatDate(String releaseDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = (Date)format.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd YYYY");
        String formattedDate = formatter.format(date);
        dorText.setText(formattedDate);
    }

    private void getGenresItems() {
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false);
        genresRecyclerView.setLayoutManager(lm);

        GenreListAdapter adapter = new GenreListAdapter(getApplicationContext(),genresArray);
        genresRecyclerView.setAdapter(adapter);

    }

    private String getCastUrl(int movieId) {
        // https://api.themoviedb.org/3/movie/284054/credits?api_key=c686b5d39204b19a48fb9a27f5457a41
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(String.valueOf(movieId))
                .appendPath("credits")
                .appendQueryParameter("api_key",BuildConfig.MY_MOVIE_DB_API_KEY);
        return builder.build().toString();
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


    private String getVideosLink(int movieId) {
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


}
