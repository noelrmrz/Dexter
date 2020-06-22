package com.noelrmrz.pokedex.utilities;

import android.util.Log;
import android.widget.ImageView;

import com.noelrmrz.pokedex.R;

public class PicassoClient {

    private static final String TAG = PicassoClient.class.getSimpleName();

    // TODO change URL
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w500/";

    public static void downloadImage(String url, ImageView imageView) {
        if(url != null && url.length()>0)
        {
            String completeUrl = BASE_URL.concat(url);
            Picasso.get().load(completeUrl).into(imageView);
        }
        else {
            Log.v(TAG, imageView.getContext().getString(R.string.picassoErrorMessage));
        }
    }
}
