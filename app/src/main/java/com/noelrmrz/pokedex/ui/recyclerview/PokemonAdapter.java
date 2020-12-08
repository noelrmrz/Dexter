package com.noelrmrz.pokedex.ui.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.databinding.RvPokemonListItemBinding;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.ui.detail.ImagePagerFragment;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A fragment for displaying a grid of images.
 */
public class PokemonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<Pokemon> mPokemonList = new ArrayList<>();
    private final RequestManager requestManager;
    private final int VIEW_TYPE_ITEM = 0;
    private Context mContext;
    public static int count;
    private final ViewHolderListener viewHolderListener;
    private static final String BASE_PROFILE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/";
    /**
     * A listener that is attached to all ViewHolders to handle image loading events and clicks.
     */
    private interface ViewHolderListener {

        void onLoadCompleted(ImageView view, int adapterPosition);

        void onItemClicked(View view, int adapterPosition, Pokemon pokemon);
    }

    /**
     * Constructs a new grid adapter for the given {@link Fragment}.
     */
    public PokemonAdapter(Fragment fragment) {
        this.requestManager = Glide.with(fragment);
        this.viewHolderListener = new ViewHolderListenerImpl(fragment);
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
            return new PokemonViewHolder(itemBinding, requestManager, viewHolderListener);
        } else {
            layoutIdForListItem = R.layout.rv_list_loading;
            view = inflater.inflate(layoutIdForListItem, viewGroup,
                    shouldAttachToParentImmediately);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PokemonViewHolder) {
            populateItemView((PokemonViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        // Progress bar displays
    }

    private void populateItemView(PokemonViewHolder viewHolder, int position) {
        Pokemon pokemon = mPokemonList.get(position);
        viewHolder.onBind(pokemon);
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


    /**
     * Default {@link ViewHolderListener} implementation.
     */
    private static class ViewHolderListenerImpl implements ViewHolderListener {

        private Fragment fragment;
        private AtomicBoolean enterTransitionStarted;

        ViewHolderListenerImpl(Fragment fragment) {
            this.fragment = fragment;
            this.enterTransitionStarted = new AtomicBoolean();
        }

        @Override
        public void onLoadCompleted(ImageView view, int position) {
            // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
            if (MainViewModel.position != position) {
                return;
            }
            if (enterTransitionStarted.getAndSet(true)) {
                return;
            }
            fragment.startPostponedEnterTransition();
        }

        /**
         * Handles a view click by setting the current position to the given {@code position} and
         * starting a {@link  ImagePagerFragment} which displays the image at the position.
         *
         * @param view the clicked {@link ImageView} (the shared element view will be re-mapped at the
         * GridFragment's SharedElementCallback)
         * @param position the selected view position
         */
        @Override
        public void onItemClicked(View view, int position, Pokemon pokemon) {
            // Update the position.
            MainViewModel.position = position;

            // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
            // instead of fading out with the rest to prevent an overlapping animation of fade and move).
            ((TransitionSet) fragment.getExitTransition()).excludeTarget(view, true);

            ImageView transitioningView = view.findViewById(R.id.iv_pokemon_sprite);
            fragment.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true) // Optimize for shared element transition
                    .addSharedElement(transitioningView, transitioningView.getTransitionName())
                    .replace(R.id.fragment_container,
                            ImagePagerFragment.newInstance(), ImagePagerFragment.class
                            .getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * ViewHolder for the grid's images.
     */
    static class PokemonViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private RvPokemonListItemBinding bind;
        private final ImageView image;
        private final RequestManager requestManager;
        private final ViewHolderListener viewHolderListener;

        PokemonViewHolder(RvPokemonListItemBinding bind, RequestManager requestManager,
                          ViewHolderListener viewHolderListener) {
            super(bind.getRoot());
            this.bind = bind;
            this.image = bind.getRoot().findViewById(R.id.iv_pokemon_sprite);
            this.requestManager = requestManager;
            this.viewHolderListener = viewHolderListener;
            bind.getRoot().findViewById(R.id.card_view).setOnClickListener(this);
        }

        /**
         * Binds this view holder to the given adapter position.
         *
         * The binding will load the image into the image view, as well as set its transition name for
         * later.
         */
        void onBind(Pokemon pokemon) {
            bind.setPokemon(pokemon);
            bind.executePendingBindings();
            int adapterPosition = getAdapterPosition();
            setImage(adapterPosition);
            // Set the string value of the image resource as the unique transition name for the view.
            image.setTransitionName(String.valueOf(pokemon.getId()));
        }

        void setImage(final int adapterPosition) {
            // Load the image with Glide to prevent OOM error when the image drawables are very large.
            requestManager
                    .load(BASE_PROFILE_URL.concat(bind.getPokemon().getId() + ".png"))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            viewHolderListener.onLoadCompleted(image, adapterPosition);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                                target, DataSource dataSource, boolean isFirstResource) {
                            viewHolderListener.onLoadCompleted(image, adapterPosition);
                            return false;
                        }
                    })
                    .into(image);
        }

        @Override
        public void onClick(View view) {
            // Let the listener start the ImagePagerFragment.
            viewHolderListener.onItemClicked(view, getAdapterPosition(), bind.getPokemon());
        }
    }

    public void setPokemonList(List<Pokemon> pokemonList) {
        Collections.sort(pokemonList);
        mPokemonList.addAll(pokemonList);
        count = getItemCount();
        notifyDataSetChanged();
    }

    public void addToPokemonList(Pokemon pokemon) {
        mPokemonList.add(pokemon);
        count = getItemCount();
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mPokemonList.remove(position);
        count = getItemCount();
        notifyItemRemoved(getItemCount());
    }

    public static String getPokemon(int position) {
        return GsonClient.getGsonClient().toJson(mPokemonList.get(position));
    }
}
