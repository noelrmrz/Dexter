package com.noelrmrz.pokedex.utilities;

public class RetrofitClient {

    // TODO change URL
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static void getRecipeListObjects(Callback<JsonArray> callback) {
        Retrofit retrofit = retrofitBuilder();

        PokemonService service = retrofit.create(PokemonService.class);
        Call<JsonArray> monsters = service.getPokemon();
        monsters.enqueue(callback);
    }

    private static Retrofit retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
