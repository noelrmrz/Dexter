package com.noelrmrz.pokedex.ui.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.databinding.RvPokemonListItemBinding;
import com.noelrmrz.pokedex.pojo.Pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Pokemon> mPokemonList = new ArrayList<>();
    private final PokemonAdapterOnClickHandler mClickHandler;
    private final int VIEW_TYPE_ITEM = 0;

    public PokemonAdapter(PokemonAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,
                                                                      int viewType) {
        int layoutIdForListItem;
        // Get the Context and ID of our layout for the list items in RecyclerView
        mContext = viewGroup.getContext();

        // Get the LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        // Inflate our layout into the view
        View view;

        if (viewType == VIEW_TYPE_ITEM) {
            RvPokemonListItemBinding itemBinding = RvPokemonListItemBinding.inflate(inflater,
                    viewGroup, shouldAttachToParentImmediately);
            return new PokemonAdapterViewHolder(itemBinding);
        } else {
            layoutIdForListItem = R.layout.rv_list_loading;
           view = inflater.inflate(layoutIdForListItem, viewGroup,
                    shouldAttachToParentImmediately);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PokemonAdapterViewHolder) {
            populateItemView((PokemonAdapterViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        // Progress bar displays
    }

    private void populateItemView(PokemonAdapterViewHolder viewHolder, int position) {
        Pokemon pokemon = mPokemonList.get(position);
        viewHolder.bind(pokemon);
    }

    @Override
    public int getItemCount() {
        if (mPokemonList == null) {
            return 0;
        }
        else {
            return mPokemonList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mPokemonList.get(position) == null) {
            return LoadingViewHolder.VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public interface PokemonAdapterOnClickHandler {
        void onClick(Pokemon pokemon, int position, View view);
    }

    public class PokemonAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private RvPokemonListItemBinding bind;

        public PokemonAdapterViewHolder(RvPokemonListItemBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
            bind.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Pokemon pokemon = mPokemonList.get(adapterPosition);
            mClickHandler.onClick(pokemon, adapterPosition, view);
        }

        /**
         * We will use this function to bind instance of Pokemon to the row
         */
        public void bind(Pokemon pokemon) {
            bind.setPokemon(pokemon);
            //bind.setHandlers(new Handlers());
            bind.executePendingBindings();
        }
    }

    public void setPokemonList(List<Pokemon> pokemonList) {
        Collections.sort(pokemonList);
        mPokemonList.addAll(pokemonList);
        notifyDataSetChanged();
    }

    public void addToPokemonList(Pokemon pokemon) {
        mPokemonList.add(pokemon);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mPokemonList.remove(position);
        notifyItemRemoved(getItemCount());
    }
}
