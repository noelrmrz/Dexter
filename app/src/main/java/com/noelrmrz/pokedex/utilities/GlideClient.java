package com.noelrmrz.pokedex.utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.Target;
import com.noelrmrz.pokedex.R;

import timber.log.Timber;

public class GlideClient {

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
            Glide.with(imageView.getContext()).load(completeUrl).into(imageView);
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
            //Glide.with(imageView.getContext()).load(completeUrl).into(imageView);
            Glide.with(imageView.getContext().getApplicationContext())
                    .load(completeUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            //FragmentManager.findFragment(imageView).getParentFragment().startPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //FragmentManager.findFragment(imageView).getParentFragment().startPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(imageView);
        }
        else {
            Timber.d(ERROR);
        }
    }

    public static void loadStatusDamageClassIcon(ImageView imageView, String moveDamageClass) {
        switch (moveDamageClass) {
            case PHYSICAL:
                Glide.with(imageView.getContext()).load(R.drawable.physical).into(imageView);
                break;
            case SPECIAL:
                Glide.with(imageView.getContext()).load(R.drawable.special).into(imageView);
                break;
            default:
                Glide.with(imageView.getContext()).load(R.drawable.status).into(imageView);
        }
    }

    public static void whosThatPokemon(RemoteViews remoteViews, int viewId, int[] appWidgetIds,
                                       String id, Context context) {
        AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, viewId,
                remoteViews, appWidgetIds);

        if (id != null && id.length() > 0)
        {
            String completeUrl = BASE_PROFILE_URL.concat(id + ".png");
            Glide
                    .with(context.getApplicationContext())
                    .asBitmap()
                    .load(completeUrl)
                    .into(appWidgetTarget);
        }
        else {
            Timber.d(ERROR);
        }
    }

    // Temporarily prevent the shared element transition until the resource has loaded
/*    private static void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }*/
}
