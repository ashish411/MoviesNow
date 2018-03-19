package com.example.ashish.moviesnow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ashish.moviesnow.Constants.Constants;
import com.example.ashish.moviesnow.Model.CastDetails;
import com.example.ashish.moviesnow.Model.DetailInf;
import com.example.ashish.moviesnow.Model.Movie;
import com.example.ashish.moviesnow.Model.Reviews;
import com.example.ashish.moviesnow.Utility.Utility;
import com.example.ashish.moviesnow.adapters.CastListAdapter;
import com.example.ashish.moviesnow.adapters.GenreListAdapter;
import com.example.ashish.moviesnow.adapters.ReviewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import at.blogc.android.views.ExpandableTextView;

public class EntertainmentDetail extends AppCompatActivity {

    private DetailInf movieDetails;
    private ImageView moviePoster,mPlayBtn,mExpandMoreBtn;
    private TextView runtimeText,dorText;
    private ExpandableTextView overviewText;
    private ProgressBar progressBar;
    private List<CastDetails> mCastDetailsList;
    private List<Reviews> reviewsList;
    private Toolbar toolbar;
    private String videoLink = null;
    private RecyclerView genresRecyclerView, castRecyclerView, reviewsRecyclerView;
    private String[] genresArray;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ReviewAdapter reviewAdapter;
    private CastListAdapter castListAdapter;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        mContext = getApplicationContext();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        mExpandMoreBtn = findViewById(R.id.expandDesc);
        mPlayBtn = findViewById(R.id.playButton);
        progressBar = findViewById(R.id.loadingDetails);
        moviePoster = findViewById(R.id.moviePoster);
        runtimeText = findViewById(R.id.runtime);
        dorText= findViewById(R.id.dor);
        overviewText= (ExpandableTextView) findViewById(R.id.expandableText);
        genresRecyclerView = findViewById(R.id.genresRecyclerView);
        castRecyclerView = findViewById(R.id.castRecyclerView);

        overviewText.setInterpolator(new OvershootInterpolator());

        mCastDetailsList = new ArrayList<>();
        int movieId = getIntent().getIntExtra(Constants.MOVIE_ID,0);

        String movieDetailUrl = Utility.getMovieDetailUrl(movieId);
        String castDetailUrl = Utility.getCastUrl(movieId);
        String videoUrlApi = Utility.getVideosLink(movieId);
        String reviewsUrl = Utility.getReviewsLink(movieId);

        requestMovieDetails(movieDetailUrl,castDetailUrl,videoUrlApi,reviewsUrl);

        mPlayBtn.setVisibility(View.GONE);
    }

    private void requestMovieDetails(String movieDetailUrl, String castDetailUrl, final String videoUrlApi, String reviewsUrl) {
        JsonObjectRequest movieDetailReq = new JsonObjectRequest(Request.Method.GET, movieDetailUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject root) {
                try {
                    String backdropPath = root.getString("backdrop_path");
                    String movieUrl = Constants.POSTER_BASE_URL+backdropPath;
                    String runtime;
                    if (root.isNull("runtime"))
                        runtime = "N.A";
                    else
                        runtime = String.valueOf(root.getInt("runtime"));
                    String dor = root.getString("release_date");
                    final String overview = root.getString("overview");
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
                    runtimeText.setText(movieDetails.getRuntime() + " min");
                    formatDate(movieDetails.getReleaseDate());
                    collapsingToolbarLayout.setTitle(movieDetails.getTitle());
                    overviewText.setText(movieDetails.getOverview());


                    mExpandMoreBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          mExpandMoreBtn.setImageResource(overviewText.isExpanded() ?
                                  R.drawable.ic_expand_more_black_24dp :
                                  R.drawable.ic_expand_less_black_24dp);
                          overviewText.toggle();
                        }
                    });
                    try {
                        URL url = new URL(movieDetails.getMoviePoster());
                        Bitmap bitmap = new PictureLoadingTask().execute(url).get();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@NonNull Palette palette) {
                                int mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                                collapsingToolbarLayout.setContentScrimColor(mutedColor);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                    getWindow().setStatusBarColor(mutedColor);
                                }
                            }
                        });
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


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

        JsonObjectRequest videoRequest = new JsonObjectRequest(Request.Method.GET, videoUrlApi, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray resultsArray = response.getJSONArray("results");
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

        JsonObjectRequest castRequest = new JsonObjectRequest(Request.Method.GET, castDetailUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray castArray = response.getJSONArray("cast");
                    if (castArray.length()>0){
                        for (int i=0;i<5;i++){
                            JSONObject obj = castArray.getJSONObject(i);
                            String profilePath =  obj.getString("profile_path");
                            String castProfileUrl = Constants.CAST_BASE_URL+profilePath;
                            String castRealName = obj.getString("name");
                            String castMovieName = obj.getString("character");
                            mCastDetailsList.add(new CastDetails(castRealName,castMovieName,castProfileUrl));
                        }
                        castListAdapter = new CastListAdapter(mContext,mCastDetailsList);
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

        JsonObjectRequest reviewsReq = new JsonObjectRequest(Request.Method.GET, reviewsUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    reviewsList = new ArrayList<>();
                    parseJsonData(response);
                    setReviewData();
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


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(movieDetailReq);
        queue.add(videoRequest);
        queue.add(castRequest);
        queue.add(reviewsReq);
    }

    private void setReviewData() {
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.HORIZONTAL,false);
        reviewsRecyclerView.setLayoutManager(lm);
        Log.v("tag","reviews List size"+reviewsList.size());
        reviewsRecyclerView.setAdapter(reviewAdapter);
    }

    private void parseJsonData(JSONObject response) throws JSONException {
        JSONArray resultsArray = response.getJSONArray("results");
        String author = null;
        String content = null;
        String linkUrl = null;
        if (resultsArray.length()>0){
            for (int i=0;i<resultsArray.length();i++){
                JSONObject obj = resultsArray.getJSONObject(i);
                author = obj.getString("author");
                content = obj.getString("content");
                linkUrl = obj.getString("url");
            }
        }
        else {
            content = "Movie Not Yet Reviewed";
        }
        reviewsList.add(new Reviews(author,content,linkUrl));
        reviewAdapter = new ReviewAdapter(mContext,reviewsList);

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
        castRecyclerView.setAdapter(castListAdapter);
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


}
