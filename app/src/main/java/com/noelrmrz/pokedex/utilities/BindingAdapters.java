package com.noelrmrz.pokedex.utilities;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    public BindingAdapters() {}

    @BindingAdapter("hideIfZero")
    public static void hideIfZero(View view, int number) {
        if (number > 1)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }

    @BindingAdapter("setImageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        if (url == null) {
            imageView.setImageDrawable(null);
        } else {
            PicassoClient.downloadProfileImage(url, imageView);
        }
    }
}
