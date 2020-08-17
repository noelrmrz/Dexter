package com.noelrmrz.pokedex.ui.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
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

        changeBackgroundColor(mPokemonList.get(position).getTypeList()[0].getType().getName());
        viewHolder.pokemonName.setText(capitalizeName(mPokemonList.get(position).getName()));
        viewHolder.pokemonId.setText(String.valueOf(convertIdToString(mPokemonList.get(position).getId())));
        viewHolder.pokemonPrimaryType.setText(mPokemonList.get(position).getTypeList()[0].getType().getName());

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
        void onClick(Pokemon pokemon, int position);
    }

    public class PokemonAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView pokemonName;
        public TextView pokemonId;
        private TextView pokemonPrimaryType;
        private TextView pokemonSecondaryType;
        public ImageView pokemonImage;

        public PokemonAdapterViewHolder(View view) {
            super(view);
            pokemonName = view.findViewById(R.id.tv_pokemon_name);
            pokemonId = view.findViewById(R.id.tv_pokemon_id);
            pokemonImage = view.findViewById(R.id.iv_pokemon_sprite);
            pokemonPrimaryType = view.findViewById(R.id.tv_pokemon_primary_type);
            pokemonSecondaryType = view.findViewById(R.id.tv_pokemon_secondary_type);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Pokemon pokemon = mPokemonList.get(adapterPosition);
            mClickHandler.onClick(pokemon, adapterPosition);
        }
    }

    public void setPokemonList(List<Pokemon> pokemonList) {
        mPokemonList = pokemonList;
        notifyDataSetChanged();
    }

    public void addToPokemonList(Pokemon pokemon) {
        // Specify the index to keep the list in sequential order
        mPokemonList.add(pokemon);
        notifyDataSetChanged();
    }

    public void changeBackgroundColor(String typeColor) {
        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(mContext, R.drawable.rectangle)).mutate();
        DrawableCompat.setTint(drawable, getColor(typeColor));
        //ColorFilter filter = new LightingColorFilter(Color.DKGRAY, Color.BLACK);
        //drawable.setColorFilter(filter);
    }

    public int getColor(String type) {
        switch (type) {
            case "fire":
                return R.color.fire;
            case "water":
                return R.color.water;
            case "grass":
                return R.color.grass;
            case "flying":
                return R.color.flying;
            default:
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
