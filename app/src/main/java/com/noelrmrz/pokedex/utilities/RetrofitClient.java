package com.noelrmrz.pokedex.utilities;

public class RetrofitClient {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static void getRecipeListObjects(Callback<JsonArray> callback) {
        Retrofit retrofit = retrofitBuilder();

        RecipeService service = retrofit.create(RecipeService.class);
        Call<JsonArray> recipes = service.getRecipes();
        recipes.enqueue(callback);
    }

    private static Retrofit retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
