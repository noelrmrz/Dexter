package com.noelrmrz.pokedex.utilities;

public class GsonClient {
    private static final Gson gson = new Gson();

    public static Gson getGsonClient() {
        return gson;
    }
}
