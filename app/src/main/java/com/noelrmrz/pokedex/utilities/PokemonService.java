package com.noelrmrz.pokedex.utilities;

import com.noelrmrz.pokedex.pojo.Ability;
import com.noelrmrz.pokedex.pojo.EvolutionChainLink;
import com.noelrmrz.pokedex.pojo.Move;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.PokemonJsonList;
import com.noelrmrz.pokedex.pojo.PokemonSpecies;
import com.noelrmrz.pokedex.pojo.Type;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokemonService {

    // TODO change URL
    @GET("pokemon")
    Call<PokemonJsonList> getPokemonList(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{pokemonNameOrId}")
    Call<Pokemon> getPokemonInformationByNameOrId(@Path("pokemonNameOrId") String nameOrId);

    @GET("pokemon-species/{pokemonNameOrId}")
    Call<PokemonSpecies> getSpeciesInformationByNameOrId(@Path("pokemonNameOrId") String nameOrId);

    @GET("ability/{abilityNameOrId}")
    Call<Ability> getAbilityInformation(@Path("abilityNameOrId") String nameOrId);

    @GET("type/{typeNameOrId}")
    Call<Type> getTypeInformation(@Path("typeNameOrId") String nameOrId);

    @GET("move/{nameOrId}")
    Call<Move> getMoveInformation(@Path("nameOrId") String nameOrId);

    @GET("evolution-chain/{id}")
    Call<EvolutionChainLink> getPokemonEvolutionChain(@Path("id") String id);
}
