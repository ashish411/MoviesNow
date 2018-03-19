package com.example.ashish.moviesnow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Ashish on 12-03-2018.
 */

public class PictureLoadingTask extends AsyncTask<URL,Void,Bitmap> {

    @Override
    protected Bitmap doInBackground(URL... urls) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
            if (bitmap!=null)
                return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
