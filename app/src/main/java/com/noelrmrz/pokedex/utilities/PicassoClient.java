package com.noelrmrz.pokedex.utilities;

import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.fragment.app.Fragment;

import com.noelrmrz.pokedex.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

import static androidx.core.app.ActivityCompat.startPostponedEnterTransition;

public class PicassoClient {

    private static final String PHYSICAL = "physical";
    private static final String SPECIAL = "special";
    private static final String ERROR = "Could not download the requested image";
    private static final String BASE_SPRITE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
    private static final String BASE_PROFILE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/";
    //private static final String BASE_URL = "https://assets.pokemon.com//assets/cms2/img/pokedex/detail/";

    public static void downloadSpriteImage(String id, ImageView imageView) {
        if(id != null && id.length() > 0)
        {
            String completeUrl = BASE_SPRITE_URL.concat(id + ".png");
            Picasso.get().load(completeUrl).into(imageView);
        }
        else {
            Timber.d(ERROR);
            imageView.setImageDrawable(null);
        }
    }

    public static void downloadProfileImage(String id, ImageView imageView) {
        if(id != null && id.length() > 0)
        {
            String completeUrl = BASE_PROFILE_URL.concat(id + ".png");
            Picasso.get().load(completeUrl).into(imageView);
        }
        else {
            Timber.d(ERROR);
        }
    }

    public static void loadStatusDamageClassIcon(ImageView imageView, String moveDamageClass) {
        switch (moveDamageClass) {
            case PHYSICAL:
                Picasso.get().load(R.drawable.physical).into(imageView);
                break;
            case SPECIAL:
                Picasso.get().load(R.drawable.special).into(imageView);
                break;
            default:
                Picasso.get().load(R.drawable.status).into(imageView);
        }
    }

    public static void whosThatPokemon(RemoteViews remoteViews, int viewId, int[] appWidgetIds,
                                       String id) {
        if (id != null && id.length() > 0)
        {
            String completeUrl = BASE_PROFILE_URL.concat(id + ".png");
            Picasso.get().load(completeUrl).into(remoteViews, viewId, appWidgetIds);
        }
        else {
            Timber.d(ERROR);
        }
    }

    public static void postponedDownloadImage(String url, ImageView imageView, Activity activity) {
        if(url != null && url.length()>0)
        {
            String completeUrl = BASE_PROFILE_URL.concat(url);
            Picasso.get().load(completeUrl).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    startPostponedEnterTransition(activity);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        else {
            Timber.d(ERROR);
        }
    }

    // Temporarily prevent the shared element transition until the resource has loaded
    private static void scheduleStartPostponedTransition(final View sharedElement,
                                                         Fragment fragment) {
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
