package com.noelrmrz.pokedex.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.ui.TabsAdapter;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    Pokemon savedPokemon;
    private static final String BASE_URL = "https://assets.pokemon.com//assets/cms2/img/pokedex/detail/";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TabsAdapter mTabsAdapter;
    private ViewPager2 viewPager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private AppBarConfiguration appBarConfiguration;

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
    // TODO: Rename and change types and number of parameters
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

        if (getArguments() != null) {
            // Get the arguments
            String args = DetailFragmentArgs.fromBundle(getArguments()).getPokemonJsonString();
            savedPokemon = GsonClient.getGsonClient().fromJson(args, Pokemon.class);
            mTabsAdapter = new TabsAdapter(this, args);
        }

        Intent intentThatStartedThisActivity = getActivity().getIntent();

        // Check for extras in the Intent
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                savedPokemon = GsonClient.getGsonClient().fromJson(
                        intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT),
                Pokemon.class);
            }
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
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_container);
        NavController navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) getActivity(), navController, appBarConfiguration);

//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        view.findViewById(R.id.favorite_fab);

        // Setup the views
        ImageView imageView = view.findViewById(R.id.iv_fragment_detail);
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName("Charmander");
        }*/
        //PicassoClient.postponedDownloadImage(savedPokemon.getProfileUrl(), imageView, this.getActivity());

        String completeUrl = BASE_URL.concat(savedPokemon.getProfileUrl());
        Picasso.get().load(completeUrl).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.v("ppicaasso", "success");
                //startPostponedEnterTransition();
            }

            @Override
            public void onError(Exception e) {

            }
        });

        // Setup the TabLayout
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("Tab " + (position + 1))).attach();
    }
}