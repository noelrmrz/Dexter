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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.ui.recyclerview.PokemonAdapter;
import com.noelrmrz.pokedex.viewmodel.MainViewModel;

import java.util.List;
import java.util.Map;

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokemonAdapter gridAdapter;
    private boolean isLoading = false;
    private MainViewModel mainViewModel;

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
                        // Bottom of the list, load more data.
                        loadData();
                    }
                }
            }
        });

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.getPokemonMutableLiveData().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Pokemon pokemon) {
                if (mainViewModel.getOffset() == mainViewModel.getAllPokemonList().size()) {
                    isLoading = false;
                    // Remove null entry item
                    gridAdapter.remove(gridAdapter.getItemCount() - 1);
                    gridAdapter.setPokemonList(
                            mainViewModel.getSubsetPokemonList(
                                    mainViewModel.getOffset() - mainViewModel.getLIMIT(),
                                    mainViewModel.getAllPokemonList().size()));
                }
            }
        });
        loadData();

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

    private void loadData() {
        isLoading = true;
        // Insert null object, this is the empty loading indicator
        gridAdapter.addToPokemonList(null);
        gridAdapter.notifyItemInserted(gridAdapter.getItemCount() - 1);
        // Call the ViewModel to retrieve the data
        mainViewModel.MakeAPICall();
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
}