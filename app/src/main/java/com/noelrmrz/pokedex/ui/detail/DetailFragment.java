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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.ui.recyclerview.TabsAdapter;
import com.noelrmrz.pokedex.viewmodel.PokemonDatabase;
import com.noelrmrz.pokedex.utilities.AppExecutors;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.PicassoClient;
import com.noelrmrz.pokedex.utilities.ViewAnimation;

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
    private ViewPager2 viewPager;
    private PokemonDatabase pokemonDatabase;

    private AppBarConfiguration appBarConfiguration;
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

/*        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }*/
    }

    /*
    Called when the fragment should create its view object heirarchy
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    /*
    Triggered after onCreateView
    only called if the view from onCreateView is non-null
    View setup should occur here
    */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


/*        Toolbar toolbar = (Toolbar) view.findViewById(R.id.detail_toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("TITLE");*/

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_container);
        NavController navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) getActivity(), navController, appBarConfiguration);

/*        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = view.findViewById(R.id.detail_toolbar);
        CollapsingToolbarLayout layout = view.findViewById(R.id.collapsing_toolbar);

        NavigationUI.setupWithNavController(layout, toolbar, navController, appBarConfiguration);*/

        // Setup the ViewPager
        viewPager = view.findViewById(R.id.tab_pager);
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
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName("Charmander");
        }*/
        //PicassoClient.postponedDownloadImage(savedPokemon.getProfileUrl(), imageView, this.getActivity());

        PicassoClient.downloadProfileImage(String.valueOf(savedPokemon.getId()), imageView);

        // Setup the TabLayout
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Data");
                                break;
                            case 1:
                                tab.setText("Moves");
                                break;
                            default:
                                tab.setText("Stats");
                                break;
                        }
                    }
                }).attach();
    }
}