package com.noelrmrz.pokedex.utilities;

import android.graphics.Bitmap;

import com.noelrmrz.pokedex.POJO.Ability;
import com.noelrmrz.pokedex.POJO.Move;
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
    Call<Pokemon> getPokemonInformationByNameOrId(@Path("pokemonNameOrId") String nameOrId);

    @GET("pokemon-species/{pokemonNameOrId}")
    Call<PokemonSpecies> getSpeciesInformationByNameOrId(@Path("pokemonNameOrId") String nameOrId);

    @GET("ability/{abilityNameOrId}")
    Call<Ability> getAbilityInformation(@Path("abilityNameOrId") String nameOrId);

    @GET("type/{typeId}")
    Call<Type> getTypeInformation(@Path("typeId") String id);

    @GET("move/{nameOrId}")
    Call<Move> getMoveInformation(@Path("nameOrId") String nameOrId);

    @GET("https://assets.pokemon.com//assets/cms2/img/pokedex/detail/{url}")
    Call<Bitmap> getBitmap(@Path("url") String url);


}
