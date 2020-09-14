package com.noelrmrz.pokedex.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private PokemonAdapter mPokemonAdapter;
    private boolean isLoading = false;
    private final int LIMIT = 20;
    private int offset = 0;

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

        // Setup the RecyclerView
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_pokemon_list);
        mRecyclerView.setAdapter(mPokemonAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == mPokemonAdapter.getItemCount() - 1) {
                        // Bottom of the list
                        loadMore();
                    }
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // Initial load
        loadMore();
    }

    private void loadMore() {
        isLoading = true;
        loadPokemonData(PreferenceManager.getDefaultSharedPreferences(getContext()).
                getString(getString(R.string.pref_sort_key),
                        getString(R.string.default_value)), isLoading);

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

    private void loadPokemonData(String preference, boolean scrollEnd) {
        ArrayList<Pokemon> pokemonListToLoad = new ArrayList<>();
        if (preference.equals(getString(R.string.favorites_value))) {
            setupViewModel();
        } else {

            mPokemonAdapter.addToPokemonList(null);
            mPokemonAdapter.notifyItemInserted(mPokemonAdapter.getItemCount() - 1);

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

                                        RetrofitClient.getSpeciesInformation(new Callback<PokemonSpecies>() {
                                            @Override
                                            public void onResponse(Call<PokemonSpecies> call, Response<PokemonSpecies> response) {

                                                if (response.isSuccessful()) {
                                                    pokemon.setPokemonSpecies(response.body());
                                                    pokemonListToLoad.add(pokemon);

                                                    // All pokemon have been loaded
                                                    if (pokemonListToLoad.size() == LIMIT) {
                                                        isLoading = false;
                                                        // Remove null entry item
                                                        mPokemonAdapter.remove(mPokemonAdapter.getItemCount() - 1);
                                                        // Add all pokemon to the adapters list
                                                        mPokemonAdapter.setPokemonList(pokemonListToLoad);
                                                        // Update the offset
                                                        offset = offset + LIMIT;
                                                    }
                                                    //mPokemonAdapter.addToPokemonList(pokemon);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<PokemonSpecies> call, Throwable t) {
                                                Timber.d(t);
                                            }
                                        }, pokemonLink.getName());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Pokemon> call, Throwable t) {
                                    Timber.d(t);
                                }
                            }, pokemonLink.getName());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PokemonJsonList> call, Throwable t) {
                    Timber.d(t);
                }
            }, LIMIT, offset);
        }
    }

    private void setupViewModel() {
        mViewModel = new ViewModelProvider(this,
                new MainViewModelFactory(this.getActivity().getApplication())).get(MainViewModel.class);
        mViewModel.getFavoritePokemon().observe(getViewLifecycleOwner(), new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(@Nullable List<Pokemon> pokemonList) {
                if (pokemonList == null) {
                    Toast.makeText(getContext(), "no favorites", Toast.LENGTH_SHORT);
                }
                else {
                    for (int x = 0; x < pokemonList.size(); x++) {
                        pokemonList.set(x, GsonClient.getGsonClient()
                                .fromJson(pokemonList.get(x).getJsonString(), Pokemon.class));
                    }
                    mPokemonAdapter.setPokemonList(pokemonList);
                }
            }
        });
    }
}