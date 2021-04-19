package com.noelrmrz.pokedex.ui.main;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.PokemonJsonList;
import com.noelrmrz.pokedex.pojo.PokemonLink;
import com.noelrmrz.pokedex.pojo.PokemonSpecies;
import com.noelrmrz.pokedex.ui.recyclerview.PokemonAdapter;
import com.noelrmrz.pokedex.utilities.RetrofitClient;
import com.noelrmrz.pokedex.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokemonAdapter gridAdapter;
    private final int LIMIT = 15;
    private int offset = 0;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_main, container, false);
        gridAdapter = new PokemonAdapter(this);
        recyclerView.setAdapter(gridAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == gridAdapter.getItemCount() - 1) {
                        // Bottom of the list
                        loadMore();
                    }
                }
            }
        });

        loadMore();

        prepareTransitions();
        postponeEnterTransition();

        scrollToPosition();

        return recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Scrolls the recycler view to show the last viewed item in the grid. This is important when
     * navigating back from the grid.
     */
    private void scrollToPosition() {
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left,
                                       int top,
                                       int right,
                                       int bottom,
                                       int oldLeft,
                                       int oldTop,
                                       int oldRight,
                                       int oldBottom) {
                recyclerView.removeOnLayoutChangeListener(this);
                final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                View viewAtPosition = layoutManager.findViewByPosition(MainViewModel.position);
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)) {
                    recyclerView.scrollToPosition(MainViewModel.position);
                }
            }
        });
    }

    private void loadMore() {
        isLoading = true;
        loadPokemonData();
    }

    /**
     * Prepares the shared element transition to the pager fragment, as well as the other transitions
     * that affect the flow.
     */
    private void prepareTransitions() {
        setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.exit_transition));


        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        // Locate the ViewHolder for the clicked position.
                        RecyclerView.ViewHolder selectedViewHolder = recyclerView
                                .findViewHolderForAdapterPosition(MainViewModel.position);
                        if (selectedViewHolder == null) {
                            return;
                        }
                        // Map the first shared element name to the child ImageView.
                        sharedElements
                                .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.iv_pokemon_sprite));
                    }
                });
    }

    private void loadPokemonData() {
        ArrayList<Pokemon> pokemonListToLoad = new ArrayList<>();

        // Insert null object
        gridAdapter.addToPokemonList(null);
        gridAdapter.notifyItemInserted(gridAdapter.getItemCount() - 1);

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
                                                    gridAdapter.remove(gridAdapter.getItemCount() - 1);
                                                    // Add all pokemon to the adapters list
                                                    gridAdapter.setPokemonList(pokemonListToLoad);
                                                    // Update the offset
                                                    offset = offset + LIMIT;
                                                }
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