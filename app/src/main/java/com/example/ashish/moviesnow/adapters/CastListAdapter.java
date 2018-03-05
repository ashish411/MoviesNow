package com.example.ashish.moviesnow.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashish.moviesnow.Model.CastDetails;
import com.example.ashish.moviesnow.R;
import com.squareup.picasso.Picasso;

import java.util.List;



public class CastListAdapter extends RecyclerView.Adapter<CastListAdapter.MyViewHolder> {
    private Context mContext;
    private List<CastDetails> mList;

    public CastListAdapter(Context mContext, List<CastDetails> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cast_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CastDetails cd = mList.get(position);
        holder.mCastText.setText(cd.getmCastRealName());
        holder.mCastMovieName.setText(cd.getmCastMovieName());
        Picasso.with(mContext).load(cd.getmCastPoster()).into(holder.mCastImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mCastText,mCastMovieName;
        private ImageView mCastImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCastText=(TextView)itemView.findViewById(R.id.castName);
            mCastImage=(ImageView) itemView.findViewById(R.id.castImage);
            mCastMovieName=(TextView)itemView.findViewById(R.id.castMovieName);
        }
    }
}
