package com.noelrmrz.pokedex.utilities;

public interface PokemonService {

    // TODO change URL
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<JsonArray> getPokemon();

}
