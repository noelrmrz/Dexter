package com.noelrmrz.pokedex.utilities;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static androidx.core.app.ActivityCompat.startPostponedEnterTransition;

public class PicassoClient {

    private static final String TAG = PicassoClient.class.getSimpleName();

    // TODO change URL
    private static final String BASE_URL = "https://assets.pokemon.com//assets/cms2/img/pokedex/detail/";

    public static void downloadImage(String url, ImageView imageView) {
        if(url != null && url.length()>0)
        {
            String completeUrl = BASE_URL.concat(url);
            Picasso.get().load(completeUrl).into(imageView);
        }
        else {
            // TODO change hardcoded string
            Log.v(TAG, "imageView.getContext().getString(R.string.picassoErrorMessage)");
        }
    }

    public static void postponedDownloadImage(String url, ImageView imageView, Fragment fragment) {
        if(url != null && url.length()>0)
        {
            String completeUrl = BASE_URL.concat(url);
            Picasso.get().load(completeUrl).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    scheduleStartPostponedTransition(imageView, fragment);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        else {
            // TODO change hardcoded string
            Log.v(TAG, "imageView.getContext().getString(R.string.picassoErrorMessage)");
        }
    }

    // Temporarily prevent the shared element transition until the resource has loaded
    private static void scheduleStartPostponedTransition(final View sharedElement, Fragment fragment) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition(fragment.getActivity());
                        return true;
                    }
                });
    }
}
