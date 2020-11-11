package com.noelrmrz.pokedex.utilities;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.noelrmrz.pokedex.pojo.AbilityLink;
import com.noelrmrz.pokedex.pojo.FlavorTextEntry;
import com.noelrmrz.pokedex.pojo.Genera;

public class BindingAdapters {

    private static final String LANGUAGE = "EN";
    private static final String VERSION_NAME = "omega-ruby";  //"ultra-sun-ultra-moon";

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
        GlideClient.downloadProfileImage(url, imageView);
    }

    @BindingAdapter("setStaticImageUrl")
    public static void setStaticImageUrl(ImageView imageView, String moveDamageClass){
        GlideClient.loadStatusDamageClassIcon(imageView, moveDamageClass);
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

    @BindingAdapter("setPokemonDescription")
    public static void setPokemonDescription(TextView textView, FlavorTextEntry[] textEntries) {
        for (FlavorTextEntry entry : textEntries) {
            if (entry.getVersionGroup().getName().equalsIgnoreCase(VERSION_NAME)
                    && entry.getLanguage().getLanguage().equalsIgnoreCase(LANGUAGE)) {
                textView.setText(entry.getFlavorText().replaceAll("(\n)", " "));
                return;
            }
        }
    }

    @BindingAdapter("setPokemonGenus")
    public static void setPokemonGenus(TextView textView, Genera[] generaList) {
        for (Genera genera : generaList) {
            if (genera.getLanguage().getLanguage().equalsIgnoreCase(LANGUAGE)) {
                textView.setText(genera.getGenus());
                return;
            }
        }
    }

    @BindingAdapter("setAbilities")
    public static void setAbilities(TextView textview, AbilityLink abilityLink) {
        if (abilityLink == null) {
            return;
        }
        else {
            textview.setVisibility(View.VISIBLE);
            textview.setText(abilityLink.getAbility().getName());
        }
    }
}
