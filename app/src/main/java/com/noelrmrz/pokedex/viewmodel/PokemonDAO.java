package com.noelrmrz.pokedex.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.noelrmrz.pokedex.POJO.Pokemon;

import java.util.List;

@Dao
public interface PokemonDAO {

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
