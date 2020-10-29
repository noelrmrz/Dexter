package com.noelrmrz.pokedex.utilities;

import com.noelrmrz.pokedex.pojo.Ability;
import com.noelrmrz.pokedex.pojo.EvolutionChainLink;
import com.noelrmrz.pokedex.pojo.Move;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.PokemonJsonList;
import com.noelrmrz.pokedex.pojo.PokemonSpecies;
import com.noelrmrz.pokedex.pojo.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = retrofitBuilder();
    private static PokemonService service = retrofit.create(PokemonService.class);
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    public static void getPokemonList(Callback<PokemonJsonList> callback, int limit, int offset) {
        Call<PokemonJsonList> pokemonList = service.getPokemonList(limit, offset);
        pokemonList.enqueue(callback);
    }

    public static void getMoveList(Callback<Move> callback, String nameOrId) {
        Call<Move> moveList = service.getMoveInformation(nameOrId);
        moveList.enqueue(callback);
    }

    public static void getAbilityList(Callback<Ability> callback, String nameOrId) {
        Call<Ability> abilityList = service.getAbilityInformation(nameOrId);
        abilityList.enqueue(callback);
    }

    public static void getTypeInformation(Callback<Type> callback, String nameOrId) {
        Call<Type> typeCall = service.getTypeInformation(nameOrId);
        typeCall.enqueue(callback);
    }

    public static void getPokemonInformation(Callback<Pokemon> callback, String nameOrId) {
        Call<Pokemon> pokemonCall = service.getPokemonInformationByNameOrId(nameOrId);
        pokemonCall.enqueue(callback);
    }

    public static void getPokemonEvolutionChain(Callback<EvolutionChainLink> callback, String id) {
        Call<EvolutionChainLink> evolutionChainCall = service.getPokemonEvolutionChain(id);
        evolutionChainCall.enqueue(callback);
    }

    public static void getSpeciesInformation(Callback<PokemonSpecies> callback, String nameOrId) {
        Call<PokemonSpecies> pokemonSpeciesCall = service.getSpeciesInformationByNameOrId(nameOrId);
        pokemonSpeciesCall.enqueue(callback);
    }

    private static Retrofit retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
