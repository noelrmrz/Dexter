package com.noelrmrz.pokedex.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class PokedexWidgetService extends JobIntentService {

    private final String mSharedPrefFile = "com.noelrmrz.pokedex";

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, PokedexWidgetService.class, 1, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
                handleActionOpenPokemon();
            }
        }
    }

    private void handleActionOpenPokemon() {
        SharedPreferences sharedPreferences = getSharedPreferences(mSharedPrefFile, MODE_PRIVATE);
        String pokemonJsonString = sharedPreferences.getString("pokemon_json_string", "");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PokedexWidgetProvider.class));
        PokedexWidgetProvider.updateWidgetPokemon(this, appWidgetManager, appWidgetIds, pokemonJsonString);
    }

    public static void startActionOpenPokemon(Context context) {
        Intent intent = new Intent(context, PokedexWidgetService.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        enqueueWork(context, intent);
    }
}
