package com.example.ashish.moviesnow.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashish.moviesnow.Model.Reviews;
import com.example.ashish.moviesnow.R;

import java.util.List;

/**
 * Created by Ashish on 12-03-2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Reviews> mReviewsList;

    public ReviewAdapter(Context mContext, List<Reviews> mReviewsList) {
        this.mContext = mContext;
        this.mReviewsList = mReviewsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_review_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Reviews currentReview = mReviewsList.get(position);
        holder.mContentText.setText(currentReview.getmContent());
        StringBuilder builder = new StringBuilder();
        if (currentReview.getmAuther() != null) {
            builder.append("Written By: ").append(currentReview.getmAuther());
            String authorText = builder.toString();
            holder.mAuthorText.setText(authorText);
        }
    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mAuthorText,mContentText;

        public MyViewHolder(View itemView) {
            super(itemView);
            mAuthorText=(TextView)itemView.findViewById(R.id.authorText);
            mContentText=(TextView)itemView.findViewById(R.id.contentText);
        }
    }
}
