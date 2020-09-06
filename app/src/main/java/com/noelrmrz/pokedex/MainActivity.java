package com.noelrmrz.pokedex;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.TransitionInflater;

import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.ui.main.MainFragment;
import com.noelrmrz.pokedex.ui.main.MainFragmentDirections;
import com.noelrmrz.pokedex.ui.recyclerview.PokemonAdapter;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements PokemonAdapter.PokemonAdapterOnClickHandler {

    private MainFragment mainFragment = MainFragment.newInstance();
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Set up the backstack navigation of the Fragment notifying MainFragment as the host
            NavHostFragment host = NavHostFragment.create(R.navigation.nav_graph);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_container, host)
                    .setPrimaryNavigationFragment(host).commit();
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_container);
        NavController navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Intent handler for search queries
        handleIntent(getIntent());

    }

    @Override
    public void onClick(Pokemon pokemon, int position, View view) {
        //TODO: find out how to transition fragments.  from fragment or activity?
        // Create a new step fragment
        // Get access to each fragment
        // DetailFragment detailFragment = DetailFragment.newInstance(GsonClient.getGsonClient().toJson(pokemon));

        NavDirections action = MainFragmentDirections.actionMainFragmentToDetailFragment(GsonClient.getGsonClient().toJson(pokemon));
        Navigation.findNavController(view).navigate(action);
        // Check that the device is running lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Inflate transitions to apply
/*            Transition changeTransform = TransitionInflater.from(this)
                    .inflateTransition(R.transition.image_shared_element_transition);*/

            // Setup exit transition on first fragment
            mainFragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.image_shared_element_transition));
            mainFragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));

            // Setup exit transition on second fragment
            //detailFragment.setSharedElementEnterTransition((TransitionInflater.from(this).inflateTransition(R.transition.image_shared_element_transition)));
            //detailFragment.setEnterTransition((TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition)));

            // Find the shared element in the first fragment
            ImageView ivProfile = view.findViewById(R.id.iv_pokemon_sprite);

/*            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .addSharedElement(ivProfile, ViewCompat.getTransitionName(ivProfile))
                    .replace(R.id.container, detailFragment, detailFragment.getClass().getSimpleName())
                    .addToBackStack(null)
                    .commit();*/
        }
        else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        // Get the SearchView and set the searchable.xml configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.widget.SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Associate searchable.xml configuration with the SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        // Find the id of the SearchView EditText
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);

        // This is where you find the EditText and set its background resource
        EditText editText = (EditText) searchView.findViewById(id);
        editText.setBackgroundResource(R.drawable.searchview);
        editText.setTextColor(getResources().getColor((android.R.color.black)));
        editText.setAlpha((float) 0.60);
        // TODO: change the opacity to 0.87 for user inputs
        editText.setHintTextColor(getResources().getColor((android.R.color.black)));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    // for searching
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            RetrofitClient.getPokemonInformation(new Callback<Pokemon>() {
                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    if (response.isSuccessful()) {
                        Pokemon pokemon = response.body();
                        pokemon.setProfileUrl(convertIdToString(response.body().getId()) + ".png");
                    }
                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {

                }
            }, query);
        }

        //TODO: navigate to detailFragment
        //DetailFragment detailFragment = DetailFragment.newInstance(GsonClient.getGsonClient().toJson(pokemon));
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


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}

