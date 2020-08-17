package com.noelrmrz.pokedex.utilities;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;

public class PokemonApiClient {
    private final static PokeApi pokeApi = new PokeApiClient();

    public PokemonApiClient() {

    }

    public static PokeApi getPokeApi() {
        return pokeApi;
    }

}
