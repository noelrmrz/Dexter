package com.noelrmrz.pokedex.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.noelrmrz.pokedex.POJO.Pokemon;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Pokemon>> favoritePokemon;

    public MainViewModel(@NonNull Application application) {
        super(application);

        PokemonDatabase pokemonDatabase = PokemonDatabase.getInstance(this.getApplication());
        favoritePokemon = pokemonDatabase.pokemonDAO().loadFavoritePokemon();
    }

    public LiveData<List<Pokemon>> getFavoritePokemon() {
        return favoritePokemon;
    }
}