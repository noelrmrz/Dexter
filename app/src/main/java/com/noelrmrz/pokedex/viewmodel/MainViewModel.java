package com.noelrmrz.pokedex.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.noelrmrz.pokedex.pojo.Ability;
import com.noelrmrz.pokedex.pojo.EvolutionChainLink;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.PokemonJsonList;
import com.noelrmrz.pokedex.pojo.PokemonLink;
import com.noelrmrz.pokedex.pojo.PokemonSpecies;
import com.noelrmrz.pokedex.pojo.Type;
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
    private final MutableLiveData<PokemonJsonList> pokemonJsonListMutableLiveData;
    private final MutableLiveData<Pokemon> pokemonMutableLiveData;
    private MutableLiveData<Type> typeMutableLiveData;
    private MutableLiveData<Ability> abilityMutableLiveData;
    private MutableLiveData<EvolutionChainLink> evolutionChainLinkMutableLiveData;
    private static List<Pokemon> allPokemonList = new ArrayList<>();
    private static final List<Type> allTypes = new ArrayList<>();

    public MainViewModel() {
        pokemonJsonListMutableLiveData = new MutableLiveData<>();
        pokemonMutableLiveData = new MutableLiveData<>();
        typeMutableLiveData = new MutableLiveData<>();
        evolutionChainLinkMutableLiveData = new MutableLiveData<>();
        abilityMutableLiveData = new MutableLiveData<>();
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

    public MutableLiveData<Type> getTypeMutableLiveData() {
        return typeMutableLiveData;
    }

    public MutableLiveData<EvolutionChainLink> getEvolutionChainLinkMutableLiveData() {
        return evolutionChainLinkMutableLiveData;
    }

    public MutableLiveData<Ability> getAbilityMutableLiveData() {
        return abilityMutableLiveData;
    }

    public MutableLiveData<Pokemon> getPokemonMutableLiveData() {
        return pokemonMutableLiveData;
    }

    public List<Pokemon> getAllPokemonList() {
        return allPokemonList;
    }

    public List<Type> getAllTypes() {
        return allTypes;
    }

    public Type getType(String name) {
        for (Type type : allTypes) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public List<Pokemon> getSubsetPokemonList(int fromIndex, int toIndex) {
        return allPokemonList.subList(fromIndex, toIndex);
    }

    public void MakeAPICall() {
        RetrofitClient.getPokemonList(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PokemonJsonList> call,
                                   @NonNull Response<PokemonJsonList> response) {
                assert response.body() != null;
                for (PokemonLink pokemonLink : response.body().getResults()) {
                    getPokemonInformation(pokemonLink.getName());
                }
                pokemonJsonListMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<PokemonJsonList> call, @NonNull Throwable t) {
                pokemonJsonListMutableLiveData.postValue(null);
            }
        }, LIMIT, offset);

        // Call to update the offset to avoid redundant API requests
        updateOffset();
    }

    private void getPokemonInformation(String nameOrId) {
        RetrofitClient.getPokemonInformation(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Pokemon> call, @NonNull Response<Pokemon> response) {
                assert response.body() != null;
                getDetailedPokemonInformation(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Pokemon> call, @NonNull Throwable t) {
                Timber.d("Error fetching the data");
                pokemonMutableLiveData.postValue(null);
            }
        }, nameOrId);
    }

    private void getDetailedPokemonInformation(Pokemon pokemon) {
        RetrofitClient.getSpeciesInformation(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PokemonSpecies> call,
                                   @NonNull Response<PokemonSpecies> response) {
                pokemon.setPokemonSpecies(response.body());
                allPokemonList.add(pokemon);
                pokemonMutableLiveData.postValue(pokemon);
            }

            @Override
            public void onFailure(@NonNull Call<PokemonSpecies> call, @NonNull Throwable t) {
                Timber.d("Error fetching the data");
            }
        }, pokemon.getName());
    }

    public void getPokemonTypeData(String name) {
        RetrofitClient.getTypeInformation(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Type> call, @NonNull Response<Type> response) {
                // Todo store in Database
                allTypes.add(response.body());
                typeMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Type> call, @NonNull Throwable t) {

            }
        }, name);
    }

    public void getPokemonEvolutionData(String id) {
        RetrofitClient.getPokemonEvolutionChain(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EvolutionChainLink> call,
                                   @NonNull Response<EvolutionChainLink> response) {
                evolutionChainLinkMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<EvolutionChainLink> call, @NonNull Throwable t) {

            }
        }, id);
    }

    public void getAbilityData(String nameOrId) {
        RetrofitClient.getAbilityList(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Ability> call, @NonNull Response<Ability> response) {
                abilityMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Ability> call, Throwable t) {

            }
        }, nameOrId);
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