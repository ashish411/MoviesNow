package com.example.ashish.moviesnow;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ashish.moviesnow.Constants.Constants;
import com.example.ashish.moviesnow.Model.DetailInf;
import com.example.ashish.moviesnow.adapters.GenreListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EntertainmentDetail extends AppCompatActivity {

    private DetailInf movieDetails;
    private ImageView moviePoster;
    private TextView runtimeText,dorText,overviewText,titleText;
    private ProgressBar progressBar;


    private RecyclerView genresRecyclerView;
    private String[] genresArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_detail);

        progressBar = (ProgressBar) findViewById(R.id.loadingDetails);
        moviePoster = (ImageView)findViewById(R.id.moviePoster);
        runtimeText = (TextView)findViewById(R.id.runtime);
        dorText= (TextView)findViewById(R.id.dor);
        overviewText=(TextView)findViewById(R.id.movieOverview);
        titleText = (TextView)findViewById(R.id.movieTitle);
        genresRecyclerView = (RecyclerView)findViewById(R.id.genresRecyclerView);


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
                    String movieUrl = baseUrl+backdropPath;
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
                finally {
                    progressBar.setVisibility(View.GONE);
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
