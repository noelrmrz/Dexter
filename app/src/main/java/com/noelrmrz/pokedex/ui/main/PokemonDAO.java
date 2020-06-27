package com.noelrmrz.pokedex.ui.main;

import android.graphics.Movie;

import androidx.lifecycle.LiveData;

import com.noelrmrz.pokedex.POJO.Pokemon;

import java.util.List;

@Dao
interface PokemonDAO {

    @Query("SELECT * FROM favorite_pokemon")
    LiveData<List<Pokemon>> loadFavoritePokemon();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoritePokemon(Pokemon pokemon);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoritePokemon(Pokemon pokemon);

    @Delete
    void deleteFavoritePokemon(Pokemon pokemon);

    @Query("SELECT * FROM favorite_pokemon WHERE id = :id")
    LiveData<Pokemon> loadPokemonById(int id);
}
