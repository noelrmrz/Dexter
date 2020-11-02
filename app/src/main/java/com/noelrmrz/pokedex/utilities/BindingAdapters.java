package com.noelrmrz.pokedex.utilities;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.noelrmrz.pokedex.pojo.FlavorTextEntry;

public class BindingAdapters {

    private static final String LANGUAGE = "EN";
    private static final String VERSION_NAME = "ultra-sun-ultra-moon";

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
        PicassoClient.downloadProfileImage(url, imageView);
    }

    @BindingAdapter("setStaticImageUrl")
    public static void setStaticImageUrl(ImageView imageView, String moveDamageClass){
        PicassoClient.loadStatusDamageClassIcon(imageView, moveDamageClass);
    }

    @BindingAdapter("setMoveDescription")
    public static void setMoveDescription(TextView textView, FlavorTextEntry[] flavorTextEntries) {
        for (FlavorTextEntry flavorTextEntry: flavorTextEntries) {
            if (flavorTextEntry.getLanguage().getLanguage().equalsIgnoreCase(LANGUAGE)
                    && flavorTextEntry.getVersionGroup()
                    .getName()
                    .equalsIgnoreCase(VERSION_NAME)) {
                // Remove 'newline' entries in text
                textView.setText(flavorTextEntry.getFlavorText().replaceAll("(\n)", " "));
            }
        }
    }

    @BindingAdapter("setBackgroundColor")
    public static void setBackgroundColor(View view, String someString) {
        // Change the background color depending on the primary type
        Drawable drawable = view.getBackground().mutate();
        PorterDuffColorFilter filter = new PorterDuffColorFilter(view.getResources()
                .getColor(HelperTools.getColor(view.getContext(), someString)),
                PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(filter);
        drawable.invalidateSelf();
    }

}
