package com.noelrmrz.pokedex.ui.main;

import android.content.Context;

import com.noelrmrz.pokedex.POJO.Pokemon;

@Database(entities = {Pokemon.class}, version = 1, exportSchema = false)
public abstract class PokemonDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorite_pokemon";
    private static PokemonDatabase sInstance;

    public static PokemonDatabase getInstance(Context context) {
        // Create a new databaseInstance
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        PokemonDatabase.class, PokemonDatabase.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract PokemonDAO pokemonDAO;
}
