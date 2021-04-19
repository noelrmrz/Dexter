package com.noelrmrz.pokedex.ui.detail;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.ui.recyclerview.TabsAdapter;
import com.noelrmrz.pokedex.utilities.AppExecutors;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.ViewAnimation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    private static final String KEY_IMAGE_RES = "com.google.samples.gridtopager.key.imageRes";
    private static final String BASE_PROFILE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/";
    private TabsAdapter mTabsAdapter;
    private final String DATA = "Data";
    private final String MOVES = "Moves";
    private final String STATS = "Stats";
    Pokemon savedPokemon;

    private Boolean isRotate = false;

    public static DetailFragment newInstance(String param1) {
        DetailFragment fragment = new DetailFragment();
        Bundle argument = new Bundle();
        argument.putString(KEY_IMAGE_RES, param1);
        fragment.setArguments(argument);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Just like we do when binding views at the grid, we set the transition name to be the string
        // value of the image res.
        view.findViewById(R.id.iv_fragment_detail).setTransitionName(String.valueOf(savedPokemon.getId()));
        //TODO: get the pokemons primary type and based on that change the background color
        CollapsingToolbarLayout imageView = view.findViewById(R.id.collapsing_toolbar);
        ColorFilter colorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.grass), PorterDuff.Mode.LIGHTEN);
        Drawable background = getResources().getDrawable(R.drawable.rectangle);
        background.setColorFilter(colorFilter);
        background.setAlpha(150);
        imageView.setBackground(background);
        //imageView.setContentScrimColor(getResources().getColor(R.color.grass));
        //imageView.setBackgroundColor(Color.argb(50, 102, 187, 106));

        // Load the image with Glide to prevent OOM error when the image drawables are very large.
        Glide.with(this)
                .load(BASE_PROFILE_URL.concat(savedPokemon.getId()  + ".png"))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable>
                            target, boolean isFirstResource) {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going in case of a failure.
                        getParentFragment().startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                            target, DataSource dataSource, boolean isFirstResource) {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going when the image is ready.
                        getParentFragment().startPostponedEnterTransition();
                        return false;
                    }
                })
                .into((ImageView) view.findViewById(R.id.iv_fragment_detail));
        return view;
    }

    /*
    Called when the fragment is being created or recreated
    Use onCreate for any standard setup that does not require the activity to be fully created
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // Get the arguments
            String args = getArguments().getString(KEY_IMAGE_RES);
            savedPokemon = GsonClient.getGsonClient().fromJson(args, Pokemon.class);
            mTabsAdapter = new TabsAdapter(this, args);
        }

    }

    /*
    Triggered after onCreateView
    only called if the view from onCreateView is non-null
    View setup should occur here
    */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Setup the ViewPager
        ViewPager2 viewPager = view.findViewById(R.id.tab_pager);
        viewPager.setAdapter(mTabsAdapter);

        // FAB
        FloatingActionButton fab = view.findViewById(R.id.favorite_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isRotate = ViewAnimation.rotateFab(view, !isRotate);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        // Negate whatever favorite is currently set to
                        Boolean removeOrAdd = !savedPokemon.getFavorite();
                        savedPokemon.setFavorite(removeOrAdd);
                        savedPokemon.setJsonString(GsonClient.getGsonClient()
                                .toJson(savedPokemon, Pokemon.class));
/*                        if (removeOrAdd) {
                            pokemonDatabase.pokemonDAO().insertFavoritePokemon(savedPokemon);
                        } else {
                            pokemonDatabase.pokemonDAO().deleteFavoritePokemon(savedPokemon);
                        }*/
                    }
                });
            }
        });

        // Setup the TabLayout
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText(DATA);
                                break;
                            case 1:
                                tab.setText(MOVES);
                                break;
                            default:
                                tab.setText(STATS);
                                break;
                        }
                    }
                }).attach();
    }
}