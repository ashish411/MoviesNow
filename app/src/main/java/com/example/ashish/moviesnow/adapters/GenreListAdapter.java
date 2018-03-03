package com.example.ashish.moviesnow.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashish.moviesnow.R;

/**
 * Created by ashis on 3/3/2018.
 */

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.MyViewHolder> {

    private Context context;
    private String[] genreList;

    public GenreListAdapter(Context context, String[] genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.genre_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String genreName = genreList[position];
        holder.genreText.setText(genreName);
        holder.genreText.post(new Runnable() {
            @Override
            public void run() {
                String n = genreName.replace(" ","\n");
                holder.genreText.setText(n);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreList.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView genreText;

        public MyViewHolder(View itemView) {
            super(itemView);
            genreText = (TextView)itemView.findViewById(R.id.genres);
        }
    }
}
