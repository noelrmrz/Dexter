package com.noelrmrz.pokedex.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.noelrmrz.pokedex.POJO.AbilityLink;
import com.noelrmrz.pokedex.POJO.Genera;
import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.utilities.GsonClient;

public class InformationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    static Pokemon savedPokemon;

    public InformationFragment() {

    }

    public static InformationFragment newInstance(String param1) {
        InformationFragment informationFragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        informationFragment.setArguments(args);
        return informationFragment;
    }

    /*
    Called when the fragment is being created or recreated
    Use onCreate for any standard setup that does not require the activity to be fully created
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            savedPokemon = GsonClient.getGsonClient().fromJson(mParam1, Pokemon.class);
        }

        // TODO: Retrofit call to get species information



/*        RetrofitClient.getAbilityList(new Callback<Ability>() {
            @Override
            public void onResponse(Call<Ability> call, Response<Ability> response) {
                savedPokemon.getAbilityList()[0].setAbility(response.body());
            }

            @Override
            public void onFailure(Call<Ability> call, Throwable t) {

            }
        }, "blaze");*/
    }

    /*
    Called when the fragment should create its view object heirarchy
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    /*
    Triggered after onCreateView
    only called if the view from onCreateView is non-null
    View setup should occur here
    */
    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        TextView description = view.findViewById(R.id.tv_description);
        TextView weight = view.findViewById(R.id.tv_weight);
        TextView height = view.findViewById(R.id.tv_height);

        TextView primaryAbility = view.findViewById(R.id.tv_ability_primary_name);
        TextView secondaryAbility = view.findViewById(R.id.tv_ability_secondary_name);
        TextView hiddenAbility = view.findViewById(R.id.tv_ability_hidden_name);
        TextView primaryAbilityType = view.findViewById(R.id.tv_ability_primary_type);
        TextView secondaryAbilityType = view.findViewById(R.id.tv_ability_secondary_type);
        TextView hiddenAbilityType = view.findViewById(R.id.tv_ability_hidden_type);

        for (AbilityLink ability: savedPokemon.getAbilityList()) {
            switch (ability.getSlot()) {
                case 1:
                    primaryAbility.setText(ability.getAbility().getName());
                    break;
                case 2:
                    secondaryAbility.setVisibility(View.VISIBLE);
                    secondaryAbilityType.setVisibility(View.VISIBLE);
                    secondaryAbility.setText(ability.getAbility().getName());
                    break;
                case 3:
                    hiddenAbility.setVisibility(View.VISIBLE);
                    hiddenAbilityType.setVisibility(View.VISIBLE);
                    hiddenAbility.setText(ability.getAbility().getName());
                    break;
            }
        }

        TextView species = view.findViewById(R.id.tv_species);

        // TODO: fix the zero index here
        species.setText(getLanguage("EN", savedPokemon.getPokemonSpecies().getGenera()));
        description.setText(savedPokemon.getPokemonSpecies().getFlavorTextEntries()[0].getFlavorText());
        weight.setText(String.valueOf(savedPokemon.getWeight()) + "lbs");
        height.setText((String.valueOf(savedPokemon.getHeight())));
    }

    private String getLanguage(String language, Genera[] generaList) {
        for (Genera genera: generaList) {
            if (genera.getLanguage().getLanguage().equalsIgnoreCase(language))
                return genera.getGenus();
        }
        return language;
    }
}
