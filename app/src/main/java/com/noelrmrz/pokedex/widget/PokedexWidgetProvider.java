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
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.utilities.GlideClient;
import com.noelrmrz.pokedex.utilities.GsonClient;

public class PokedexWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, String pokemonJsonString) {
        for (int widgetId : appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.whos_that_pokemon_widget);

            // Handle
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(() -> {

                // Use Picasso to load the image into the ImageView
                GlideClient.whosThatPokemon(views, R.id.iv_whos_that_pokemon,
                        appWidgetIds, String.valueOf(GsonClient.getGsonClient()
                                .fromJson(pokemonJsonString, Pokemon.class).getId()), context);

                // Set the black outline ontop of the pokemon image
                views.setInt(R.id.iv_whos_that_pokemon, "setColorFilter",
                        context.getResources().getColor(android.R.color.black));
            });

            // Create an intent to open the app when the user clicks on the ImageView
            // PengindIntent.FLAG_UPDATE_CURRENT to update the extra text parameter
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, pokemonJsonString);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.iv_whos_that_pokemon, pendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(widgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PokedexWidgetService.startActionOpenPokemon(context);
    }

    public static void updateWidgetPokemon(Context context,
                                          AppWidgetManager appWidgetManager, int[] appWidgetIds,
                                           String pokemonJsonString) {
            updateAppWidget(context, appWidgetManager, appWidgetIds, pokemonJsonString);
    }
}
