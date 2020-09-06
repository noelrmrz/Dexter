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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.utilities.PicassoClient;

import java.util.ArrayList;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonAdapterViewHolder> {

    private Context mContext;
    private List<Pokemon> mPokemonList = new ArrayList<>();
    private final PokemonAdapterOnClickHandler mClickHandler;

    public PokemonAdapter(PokemonAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public PokemonAdapter.PokemonAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,
                                                                      int viewType) {
        // Get the Context and ID of our layout for the list items in RecyclerView
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_pokemon_list_item;

        // Get the LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        // Inflate our layout into the view
        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);
        return new PokemonAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonAdapterViewHolder viewHolder,
                                 int position) {

        String name = capitalizeName(mPokemonList.get(position).getName());
        viewHolder.pokemonName.setText(name);
        viewHolder.pokemonId.setText(String.valueOf(convertIdToString(mPokemonList.get(position).getId())));
        viewHolder.pokemonPrimaryType.setText(mPokemonList.get(position).getTypeList()[0].getType().getName());

        // Change the background color depending on the primary type
        Drawable drawable = viewHolder.constraintLayout.getBackground().mutate();
        PorterDuffColorFilter filter = new PorterDuffColorFilter(mContext.getResources()
                .getColor(getColor(mPokemonList.get(position).getTypeList()[0]
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

        //TODO Change the URL of the image
        if ((mPokemonList.get(position).getId() != -1)) {
            PicassoClient.downloadImage(mPokemonList.get(position).getProfileUrl(),
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

    public interface PokemonAdapterOnClickHandler {
        void onClick(Pokemon pokemon, int position, View view);
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
        mPokemonList = pokemonList;
        notifyDataSetChanged();
    }

    public void addToPokemonList(Pokemon pokemon) {
        // TODO: sort list
        mPokemonList.add(pokemon);
        notifyDataSetChanged();
    }

    public int getColor(String type) {
        if (mContext.getString(R.string.fire).equals(type)) {
            return R.color.fire;
        } else if (mContext.getString(R.string.water).equals(type)) {
            return R.color.water;
        } else if (mContext.getString(R.string.grass).equals(type)) {
            return R.color.grass;
        } else if (mContext.getString(R.string.flying).equals(type)) {
            return R.color.flying;
        } else if (mContext.getString(R.string.poison).equals(type)) {
            return R.color.poison;
        } else if (mContext.getString(R.string.electric).equals(type)) {
            return R.color.electric;
        } else if (mContext.getString(R.string.ground).equals(type)) {
            return R.color.ground;
        } else if (mContext.getString(R.string.rock).equals(type)) {
            return R.color.rock;
        } else if (mContext.getString(R.string.steel).equals(type)) {
            return R.color.steel;
        } else if (mContext.getString(R.string.fighting).equals(type)) {
            return R.color.fighting;
        } else if (mContext.getString(R.string.psychic).equals(type)) {
            return R.color.psychic;
        } else if (mContext.getString(R.string.dark).equals(type)) {
            return R.color.dark;
        } else if (mContext.getString(R.string.bug).equals(type)) {
            return R.color.bug;
        } else if (mContext.getString(R.string.ghost).equals(type)) {
            return R.color.ghost;
        } else if (mContext.getString(R.string.dragon).equals(type)) {
            return R.color.dragon;
        } else if (mContext.getString(R.string.ice).equals(type)) {
            return R.color.ice;
        } else if (mContext.getString(R.string.fairy).equals(type)) {
            return R.color.fairy;
        }else {
            return R.color.normal;
        }
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
}
