package com.noelrmrz.pokedex.utilities;

import com.noelrmrz.pokedex.POJO.Ability;
import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.POJO.PokemonJsonList;
import com.noelrmrz.pokedex.POJO.PokemonSpecies;
import com.noelrmrz.pokedex.POJO.Type;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokemonService {

    // TODO change URL
    @GET("pokemon")
    Call<PokemonJsonList> getPokemonList(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{pokemonNameOrId}")
    Call<Pokemon> getPokemonInformationByNameOrId(@Path("pokemonNameOrId") String NameOrId);

    @GET("pokemon-species/{pokemonNameOrId}")
    Call<PokemonSpecies> getSpeciesInformationByNameOrId(@Path("pokemonNameOrId") String NameOrId);

    @GET("ability/{abilityId}")
    Call<Ability> getAbilityInformation(@Path("abilityId") String id);

    @GET("type/{typeId}")
    Call<Type> getTypeInformation(@Path("typeId") String id);
}
