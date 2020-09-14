package com.noelrmrz.pokedex.ui.recyclerview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.utilities.HelperTools;
import com.noelrmrz.pokedex.utilities.PicassoClient;

import java.util.ArrayList;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Pokemon> mPokemonList = new ArrayList<>();
    private final PokemonAdapterOnClickHandler mClickHandler;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

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
            layoutIdForListItem = R.layout.rv_pokemon_list_item;
            view = inflater.inflate(layoutIdForListItem, viewGroup,
                    shouldAttachToParentImmediately);
            return new PokemonAdapterViewHolder(view);
        } else {
            layoutIdForListItem = R.layout.rv_pokemon_list_loading;
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
        String name = capitalizeName(mPokemonList.get(position).getName());
        viewHolder.pokemonName.setText(name);
        viewHolder.pokemonId.setText(String.valueOf(convertIdToString(mPokemonList.get(position).getId())));
        viewHolder.pokemonPrimaryType.setText(mPokemonList.get(position).getTypeList()[0].getType().getName());

        // Change the background color depending on the primary type
        Drawable drawable = viewHolder.constraintLayout.getBackground().mutate();
        PorterDuffColorFilter filter = new PorterDuffColorFilter(mContext.getResources()
                .getColor(HelperTools.getColor(mContext, mPokemonList.get(position).getTypeList()[0]
                        .getType().getName())), PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(filter);
        drawable.invalidateSelf();

        // Check for a secondary type
        // Make the view invisible if there is no secondary type
        if (mPokemonList.get(position).getTypeList().length > 1) {
            viewHolder.pokemonSecondaryType.setText(mPokemonList.get(position).getTypeList()[1].getType().getName());
        } else {
            viewHolder.pokemonSecondaryType.setVisibility(View.INVISIBLE);
        }

        if ((mPokemonList.get(position).getId() != -1)) {
            PicassoClient.downloadProfileImage(String.valueOf(mPokemonList.get(position).getId()),
                    viewHolder.pokemonImage);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.pokemonImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_launcher_foreground));
            }
        }

        ViewCompat.setTransitionName(viewHolder.pokemonImage, name);
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
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public interface PokemonAdapterOnClickHandler {
        void onClick(Pokemon pokemon, int position, View view);
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.indeterminate_bar);
        }
    }

    public class PokemonAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView pokemonName;
        private TextView pokemonId;
        private TextView pokemonPrimaryType;
        private TextView pokemonSecondaryType;
        private ImageView pokemonImage;
        private ConstraintLayout constraintLayout;

        public PokemonAdapterViewHolder(View view) {
            super(view);
            pokemonName = view.findViewById(R.id.tv_pokemon_name);
            pokemonId = view.findViewById(R.id.tv_pokemon_id);
            pokemonImage = view.findViewById(R.id.iv_pokemon_sprite);
            pokemonPrimaryType = view.findViewById(R.id.tv_pokemon_primary_type);
            pokemonSecondaryType = view.findViewById(R.id.tv_pokemon_secondary_type);
            constraintLayout = view.findViewById(R.id.inner_layout);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Pokemon pokemon = mPokemonList.get(adapterPosition);
            mClickHandler.onClick(pokemon, adapterPosition, view);
        }
    }

    public void setPokemonList(List<Pokemon> pokemonList) {
        mPokemonList.addAll(pokemonList);
        notifyDataSetChanged();
    }

    public void addToPokemonList(Pokemon pokemon) {
        // TODO: sort list
        mPokemonList.add(pokemon);
        notifyDataSetChanged();
    }

    public String capitalizeName(String name) {
        return name.substring(0,1).toUpperCase() + name.substring(1);
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

    public void remove(int position) {
        mPokemonList.remove(position);
        notifyItemRemoved(getItemCount());
    }
}
