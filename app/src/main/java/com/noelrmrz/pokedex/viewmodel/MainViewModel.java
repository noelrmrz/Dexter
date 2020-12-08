package com.noelrmrz.pokedex.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.ui.recyclerview.PokemonAdapter;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Pokemon>> favoritePokemon;
    private Pokemon pokemon;
    private LiveData<List<Pokemon>> pokemonList;
    public static int position;

    public MainViewModel(@NonNull Application application) {
        super(application);
        PokemonDatabase pokemonDatabase = PokemonDatabase.getInstance(application);
        favoritePokemon = pokemonDatabase.pokemonDAO().loadFavoritePokemon();
    }

    public LiveData<List<Pokemon>> getPokemonList() {
        if (pokemonList == null) {
            pokemonList = new MutableLiveData<List<Pokemon>>();
            loadUsers();
        }
        return pokemonList;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

    public LiveData<List<Pokemon>> getFavoritePokemon() {
        return favoritePokemon;
    }

    public String getPokemon(int position) {
        return PokemonAdapter.getPokemon(position);
    }

}