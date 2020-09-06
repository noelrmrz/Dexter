package com.noelrmrz.pokedex.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.RemoteViews;

import com.noelrmrz.pokedex.MainActivity;
import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.PicassoClient;

public class PokedexWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String pokemonJsonString) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.whos_that_pokemon_widget);

        // Handle
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(() -> {

            // Use Picasso to load the image into the ImageView
            PicassoClient.whosThatPokemon(views, R.id.iv_whos_that_pokemon, appWidgetIds,
                    GsonClient.getGsonClient().fromJson(pokemonJsonString, Pokemon.class).getProfileUrl());

            // Set the black outline ontop of the pokemon imagea
            views.setInt(R.id.iv_whos_that_pokemon,"setColorFilter",
                    context.getResources().getColor(android.R.color.black));
        });

        // Create an intent to open the app when the user clicks on the ImageView
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, pokemonJsonString);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.iv_whos_that_pokemon, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PokedexWidgetService.startActionOpenPokemon(context);
    }

    public static void updateWidgetPokemon(Context context,
                                          AppWidgetManager appWidgetManager, int[] appWidgetIds, String pokemonJsonString) {
            updateAppWidget(context, appWidgetManager, appWidgetIds, pokemonJsonString);
    }
}
