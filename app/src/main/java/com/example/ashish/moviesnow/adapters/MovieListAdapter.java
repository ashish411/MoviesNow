package com.example.ashish.moviesnow.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ashish.moviesnow.Constants.Constants;
import com.example.ashish.moviesnow.EntertainmentDetail;
import com.example.ashish.moviesnow.Model.Movie;
import com.example.ashish.moviesnow.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ashis on 2/8/2018.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {


    private Context mContext;
    private List<Movie> mMovieList;

    public MovieListAdapter(Context context, List<Movie> movieList) {
        mContext = context;
        mMovieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_card_view,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Movie currentMovie = mMovieList.get(position);

//        holder.mMovieName.setText(currentMovie.getmMovieName());
        Picasso.with(mContext).load(currentMovie.getmMoviePoster())
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.mMoviePoster);


        holder.mMoviePoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(mContext,currentMovie.getmMovieName(),Toast.LENGTH_LONG).show();
                return true;
            }
        });

        holder.mMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movieDetailIntent = new Intent(mContext, EntertainmentDetail.class);
                movieDetailIntent.putExtra(Constants.MOVIE_ID,currentMovie.getmMovieId());
                mContext.startActivity(movieDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mMoviePoster;

        public MyViewHolder(View itemView) {
            super(itemView);
//            mMovieName = (TextView)itemView.findViewById(R.id.movieName);
            mMoviePoster = (ImageView) itemView.findViewById(R.id.moviePoster);
        }

        }
    }



