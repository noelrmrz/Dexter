package com.noelrmrz.pokedex.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.databinding.FragmentDetailBinding;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.ui.recyclerview.TabsAdapter;
import com.noelrmrz.pokedex.utilities.AppExecutors;
import com.noelrmrz.pokedex.utilities.GlideClient;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.ViewAnimation;
import com.noelrmrz.pokedex.viewmodel.PokemonDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    Pokemon savedPokemon;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private TabsAdapter mTabsAdapter;
    private PokemonDatabase pokemonDatabase;
    private FragmentDetailBinding bind;
    private final String DATA = "Data";
    private final String MOVES = "Moves";
    private final String STATS = "Stats";

    private Boolean isRotate = false;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(String param1) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    /*
    Called when the fragment is being created or recreated
    Use onCreate for any standard setup that does not require the activity to be fully created
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pokemonDatabase = pokemonDatabase.getInstance(getContext());

        if (getArguments() != null) {
            // Get the arguments
            String args = DetailFragmentArgs.fromBundle(getArguments()).getPokemonJsonString();
            savedPokemon = GsonClient.getGsonClient().fromJson(args, Pokemon.class);
            mTabsAdapter = new TabsAdapter(this, args);
        }

        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);
    }

    /*
    Called when the fragment should create its view object heirarchy
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        bind = FragmentDetailBinding.inflate(inflater, container, false);
        bind.setPokemon(savedPokemon);
        return bind.getRoot();
    }

    /*
    Triggered after onCreateView
    only called if the view from onCreateView is non-null
    View setup should occur here
    */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_container);
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) getActivity(), navController, appBarConfiguration);

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
                        if (removeOrAdd) {
                            pokemonDatabase.pokemonDAO().insertFavoritePokemon(savedPokemon);
                        } else {
                            pokemonDatabase.pokemonDAO().deleteFavoritePokemon(savedPokemon);
                        }
                    }
                });
            }
        });

        // Setup the views
        ImageView imageView = view.findViewById(R.id.iv_fragment_detail);

        GlideClient.downloadProfileImage(String.valueOf(savedPokemon.getId()), imageView);

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