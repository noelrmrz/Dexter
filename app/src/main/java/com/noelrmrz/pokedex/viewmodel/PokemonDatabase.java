package com.noelrmrz.pokedex.viewmodel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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

/*    public static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'favorite_pokemon' ('id' INTEGER, "
                    + " 'name"")");
        }
    }*/

    public abstract PokemonDAO pokemonDAO();
}
