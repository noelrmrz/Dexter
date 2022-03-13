package com.noelrmrz.pokedex.utilities;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.databinding.BindingAdapter;
import androidx.palette.graphics.Palette;

import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.pojo.AbilityLink;
import com.noelrmrz.pokedex.pojo.FlavorTextEntry;
import com.noelrmrz.pokedex.pojo.Genera;

public class BindingAdapters {

    private static final String LANGUAGE = "EN";
    private static final String VERSION_NAME = "omega-ruby";  //"ultra-sun-ultra-moon";
    private static final int ONE = 1;

    public BindingAdapters() {}

    @BindingAdapter("hideIfZero")
    public static void hideIfZero(View view, int number) {
        if (number > ONE)
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
                    .equalsIgnoreCase("ultra-sun-ultra-moon")) {
                // Remove 'newline' entries in text
                textView.setText(flavorTextEntry.getFlavorText().replaceAll("(\n)", " "));
            }
        }
    }

    @BindingAdapter("setBackgroundColor")
    public static void setBackgroundColor(View view, String type) {
        // Change the background color depending on the primary type
        GradientDrawable typeBackground = (GradientDrawable) view.getBackground();
        int color = view.getResources()
                .getColor(HelperTools.getColor(view.getContext(), type));
        Palette.Swatch swatch = HelperTools.getSwatch(color);

        if (swatch != null) {
            typeBackground.setColor(swatch.getRgb());
        }
    }

    @BindingAdapter("setTextColor")
    public static void setTextColor(TextView textView, String type) {
        int color = textView.getResources()
                .getColor(HelperTools.getColor(textView.getContext(), type));
        Palette.Swatch swatch = HelperTools.getSwatch(color);

        if (swatch != null) {
            textView.setTextColor(swatch.getTitleTextColor());
        }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @BindingAdapter("setPokemonGenus")
    public static void setPokemonGenus(TextView textView, Genera[] generaList) {
        for (Genera genera : generaList) {
            if (genera.getLanguage().getLanguage().equalsIgnoreCase(LANGUAGE)) {
                Spanned styledText = Html.fromHtml(textView.getContext().getString(R.string.tv_species,
                        genera.getGenus()), Html.FROM_HTML_MODE_LEGACY);
                textView.setText(styledText);
                return;
            }
        }
    }

    @BindingAdapter("setAbilities")
    public static void setAbilities(Button button, AbilityLink abilityLink) {
        if (abilityLink != null) {
            // Get the Ability name and make everything after the first character lowercase
            button.setVisibility(View.VISIBLE);
            button.setText(abilityLink.getAbility().getName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @BindingAdapter("setWeight")
    public static void setWeight(TextView textView, int weight) {
        Spanned styledText = Html.fromHtml(textView.getContext().getString(R.string.tv_weight,
                weight), Html.FROM_HTML_MODE_LEGACY);
        textView.setText(styledText);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @BindingAdapter("setHeight")
    public static void setHeight(TextView textView, int height) {
        Spanned styledText = Html.fromHtml(textView.getContext().getString(R.string.tv_height,
                height), Html.FROM_HTML_MODE_LEGACY);
        textView.setText(styledText);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @BindingAdapter("setHabitat")
    public static void setHabitat(TextView textView, String habitat) {
        Spanned styledText = Html.fromHtml(textView.getContext().getString(R.string.tv_habitat,
                habitat), Html.FROM_HTML_MODE_LEGACY);
        textView.setText(styledText);
    }
}
