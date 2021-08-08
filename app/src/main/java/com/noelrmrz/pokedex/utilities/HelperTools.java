package com.noelrmrz.pokedex.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;

import com.noelrmrz.pokedex.R;

public class HelperTools {

    private static final String[] typeNames = new String[] {"ice",
            "water",
            "fire",
            "fighting",
            "flying",
            "normal",
            "rock",
            "ground",
            "steel",
            "dark",
            "psychic",
            "ghost",
            "poison",
            "bug",
            "grass",
            "fairy",
            "dragon",
            "electric"};

    private static HelperTools helperTools = new HelperTools();

    private HelperTools() {

    }

    public static HelperTools getInstance() {
        return helperTools;
    }

    public static int getColor(Context context, String type) {
        if (context.getString(R.string.fire).equals(type)) {
            return R.color.fire;
        } else if (context.getString(R.string.water).equals(type)) {
            return R.color.water;
        } else if (context.getString(R.string.grass).equals(type)) {
            return R.color.grass;
        } else if (context.getString(R.string.flying).equals(type)) {
            return R.color.flying;
        } else if (context.getString(R.string.poison).equals(type)) {
            return R.color.poison;
        } else if (context.getString(R.string.electric).equals(type)) {
            return R.color.electric;
        } else if (context.getString(R.string.ground).equals(type)) {
            return R.color.ground;
        } else if (context.getString(R.string.rock).equals(type)) {
            return R.color.rock;
        } else if (context.getString(R.string.steel).equals(type)) {
            return R.color.steel;
        } else if (context.getString(R.string.fighting).equals(type)) {
            return R.color.fighting;
        } else if (context.getString(R.string.psychic).equals(type)) {
            return R.color.psychic;
        } else if (context.getString(R.string.dark).equals(type)) {
            return R.color.dark;
        } else if (context.getString(R.string.bug).equals(type)) {
            return R.color.bug;
        } else if (context.getString(R.string.ghost).equals(type)) {
            return R.color.ghost;
        } else if (context.getString(R.string.dragon).equals(type)) {
            return R.color.dragon;
        } else if (context.getString(R.string.ice).equals(type)) {
            return R.color.ice;
        } else if (context.getString(R.string.fairy).equals(type)) {
            return R.color.fairy;
        }else {
            return R.color.normal;
        }
    }

    public static int getTypeId(Context context, String type) {
        if (context.getString(R.string.fire).equals(type)) {
            return R.id.fire;
        } else if (context.getString(R.string.water).equals(type)) {
            return R.id.water;
        } else if (context.getString(R.string.grass).equals(type)) {
            return R.id.grass;
        } else if (context.getString(R.string.flying).equals(type)) {
            return R.id.flying;
        } else if (context.getString(R.string.poison).equals(type)) {
            return R.id.poison;
        } else if (context.getString(R.string.electric).equals(type)) {
            return R.id.electric;
        } else if (context.getString(R.string.ground).equals(type)) {
            return R.id.ground;
        } else if (context.getString(R.string.rock).equals(type)) {
            return R.id.rock;
        } else if (context.getString(R.string.steel).equals(type)) {
            return R.id.steel;
        } else if (context.getString(R.string.fighting).equals(type)) {
            return R.id.fighting;
        } else if (context.getString(R.string.psychic).equals(type)) {
            return R.id.psychic;
        } else if (context.getString(R.string.dark).equals(type)) {
            return R.id.dark;
        } else if (context.getString(R.string.bug).equals(type)) {
            return R.id.bug;
        } else if (context.getString(R.string.ghost).equals(type)) {
            return R.id.ghost;
        } else if (context.getString(R.string.dragon).equals(type)) {
            return R.id.dragon;
        } else if (context.getString(R.string.ice).equals(type)) {
            return R.id.ice;
        } else if (context.getString(R.string.fairy).equals(type)) {
            return R.id.fairy;
        }else {
            return R.id.normal;
        }
    }

    public static int getTextColor(int color) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            float luminance = Color.luminance(color);

            if (luminance > 0.179 ) {
                return Color.BLACK;
            } else {
                return Color.WHITE;
            }
        }
        return Color.BLACK;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String[] getTypeNames() {
        return typeNames;
    }
}
