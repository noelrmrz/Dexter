package com.noelrmrz.pokedex.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.POJO.PokemonJsonList;
import com.noelrmrz.pokedex.POJO.PokemonLink;
import com.noelrmrz.pokedex.POJO.PokemonSpecies;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.ui.recyclerview.PokemonAdapter;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.RetrofitClient;
import com.noelrmrz.pokedex.widget.PokedexWidgetService;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private PokemonAdapter mPokemonAdapter;
    private final String mSharedPrefFile = "com.noelrmrz.pokedex";
    // TODO pass in as argument from mainactivity
    private Context mContext;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    /*
    Called when the fragment should create its view object heirarchy
    */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    /*
    Triggered after onCreateView
    only called if the view from onCreateView is non-null
    View setup should occur here
    */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = view.getContext();
        updateWidgetService();
        // Setup the RecyclerView
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_pokemon_list);
        mRecyclerView.setAdapter(mPokemonAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
/*        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext()
                .getDrawable(R.drawable.divider_item_decoration)));*/
        mRecyclerView.setLayoutManager(layoutManager);

        // Retrofit callbacks
        RetrofitClient.getPokemonList(new Callback<PokemonJsonList>() {
            @Override
            public void onResponse(Call<PokemonJsonList> call, Response<PokemonJsonList> response) {
                if (response.isSuccessful()) {
                    List<PokemonLink> results = response.body().getResults();

                    for (PokemonLink pokemonLink: results) {
                        RetrofitClient.getPokemonInformation(new Callback<Pokemon>() {
                            @Override
                            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                                if (response.isSuccessful()) {
                                    Pokemon pokemon = response.body();
                                    pokemon.setProfileUrl(convertIdToString(response.body().getId()) + ".png");

                                    RetrofitClient.getSpeciesInformation(new Callback<PokemonSpecies>() {
                                        @Override
                                        public void onResponse(Call<PokemonSpecies> call, Response<PokemonSpecies> response) {
                                            if (response.isSuccessful()) {
                                                pokemon.setPokemonSpecies(response.body());
                                                mPokemonAdapter.addToPokemonList(pokemon);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<PokemonSpecies> call, Throwable t) {

                                        }
                                    }, pokemonLink.getName());
                                }
                            }

                            @Override
                            public void onFailure(Call<Pokemon> call, Throwable t) {

                            }
                        }, pokemonLink.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<PokemonJsonList> call, Throwable t) {

            }
        }, 50, 0);
    }

    /*
    Called when the fragment is being created or recreated
    Use onCreate for any standard setup that does not require the activity to be fully created
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPokemonAdapter = new PokemonAdapter((PokemonAdapter.PokemonAdapterOnClickHandler) getActivity());

        /*setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                RecyclerView.ViewHolder selectedViewHolder = mRecyclerView.findViewHolderForAdapterPosition();
                if (selectedViewHolder == null || selectedViewHolder.itemView == null) {
                    return;
                }

                // Map the shared element name to the ImageView
                sharedElements.put(names.get(0),
                        selectedViewHolder.itemView.findViewById(R.id.iv_fragment_detail);
            }
        });*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO: Use the PokeAPI here to retrieve the list of pokemon and load any other resources
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this,
                new MainViewModelFactory(this.getActivity().getApplication())).get(MainViewModel.class);
        mViewModel.getFavoritePokemon().observe(getViewLifecycleOwner(), new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(@Nullable List<Pokemon> pokemonList) {
                if (pokemonList == null) {
                    Toast.makeText(getContext(), "no favorites", Toast.LENGTH_SHORT);
                }
                else {
                    // TODO: set pokemonadapter
                    //mPokemonAdapter.setPokemonList(PokemonApiClient.getPokeApi().getPokemon(0).);
                }
            }
        });
    }

    public String convertIdToString(int id) {
        if ((id / 10) < 1) {
            return "00" + id;
        }
        else if ((id/ 10) < 10)
            return "0" + id;
        else
            return String.valueOf(id);
    }

    public void updateWidgetService() {

        SharedPreferences.Editor editor = mContext.getSharedPreferences(mSharedPrefFile, Context.MODE_PRIVATE).edit();

        RetrofitClient.getPokemonInformation(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful()) {
                    Pokemon pokemon = response.body();
                    pokemon.setProfileUrl(convertIdToString(response.body().getId()) + ".png");

                    editor.putString("pokemon_json_string", GsonClient.getGsonClient().toJson(pokemon));
                    editor.apply();
                    PokedexWidgetService.startActionOpenPokemon(mContext);

/*                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, PokedexWidgetProvider.class));

                    PicassoClient.whosThatPokemon(new RemoteViews(mContext.getPackageName(), R.layout.whos_that_pokemon_widget)
                            , R.id.iv_whos_that_pokemon, appWidgetIds, pokemon.getProfileUrl());*/
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {

            }
        }, String.valueOf(new Random().nextInt(500) + 1));
    }
}