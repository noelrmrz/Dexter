package com.noelrmrz.pokedex.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.PokemonJsonList;
import com.noelrmrz.pokedex.pojo.PokemonLink;
import com.noelrmrz.pokedex.pojo.PokemonSpecies;
import com.noelrmrz.pokedex.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainViewModel extends ViewModel {

    public static int position;
    private final int LIMIT = 10;
    private int offset = 0;

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Pokemon>> favoritePokemon;
    private MutableLiveData<PokemonJsonList> pokemonJsonListMutableLiveData;
    private MutableLiveData<Pokemon> pokemonMutableLiveData;
    private List<Pokemon> allPokemonList;

    public MainViewModel() {
        pokemonJsonListMutableLiveData = new MutableLiveData<>();
        pokemonMutableLiveData = new MutableLiveData<>();
        allPokemonList = new ArrayList<>();
    }

    public MainViewModel (Application application) {
        pokemonJsonListMutableLiveData = new MutableLiveData<>();
        pokemonMutableLiveData = new MutableLiveData<>();
        allPokemonList = new ArrayList<>();

        PokemonDatabase pokemonDatabase = PokemonDatabase.getInstance(application.getApplicationContext());
        favoritePokemon = pokemonDatabase.pokemonDAO().loadFavoritePokemon();
    }

    public MutableLiveData<PokemonJsonList> getPokemonJsonListMutableLiveData() {
        return pokemonJsonListMutableLiveData;
    }

    public MutableLiveData<Pokemon> getPokemonMutableLiveData() {
        return pokemonMutableLiveData;
    }

    public List<Pokemon> getAllPokemonList() {
        return allPokemonList;
    }
    public List<Pokemon> getSubsetPokemonList(int fromIndex, int toIndex) {
        return allPokemonList.subList(fromIndex, toIndex);
    }

    public void MakeAPICall() {
        RetrofitClient.getPokemonList(new Callback<PokemonJsonList>() {
            @Override
            public void onResponse(Call<PokemonJsonList> call, Response<PokemonJsonList> response) {
                for(PokemonLink pokemonLink : response.body().getResults()) {
                    getPokemonInformation(pokemonLink.getName());
                }
                pokemonJsonListMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<PokemonJsonList> call, Throwable t) {
                pokemonJsonListMutableLiveData.postValue(null);
            }
        }, LIMIT, offset);

        // Call to update the offset to avoid redundant API requests
        updateOffset();
    }

    private void getPokemonInformation(String nameOrId) {
        RetrofitClient.getPokemonInformation(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                getDetailedPokemonInformation(response.body());
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Timber.d("Error fetching the data");
                pokemonMutableLiveData.postValue(null);
            }
        }, nameOrId);
    }

    private void getDetailedPokemonInformation(Pokemon pokemon) {
        RetrofitClient.getSpeciesInformation(new Callback<PokemonSpecies>() {
            @Override
            public void onResponse(Call<PokemonSpecies> call, Response<PokemonSpecies> response) {
                pokemon.setPokemonSpecies(response.body());
                allPokemonList.add(pokemon);
                pokemonMutableLiveData.postValue(pokemon);
            }

            @Override
            public void onFailure(Call<PokemonSpecies> call, Throwable t) {
                Timber.d("Error fetching the data");
            }
        }, pokemon.getName());
    }

    public LiveData<List<Pokemon>> getFavoritePokemon() {
        return favoritePokemon;
    }

    private void updateOffset() {
        offset += LIMIT;
    }

    public int getOffset() {
        return offset;
    }

    public int getLIMIT() {
        return LIMIT;
    }
}