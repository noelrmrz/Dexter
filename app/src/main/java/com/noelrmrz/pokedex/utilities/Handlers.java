package com.noelrmrz.pokedex.utilities;

import android.util.Log;
import android.view.View;

import com.noelrmrz.pokedex.pojo.Pokemon;

public class Handlers {

    public Handlers() {}

    public void onClickPokemon(View view, Pokemon pokemon) {
        Log.v("The following test ", pokemon.getName());
    }
}
