package com.noelrmrz.pokedex;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.POJO.PokemonSpecies;
import com.noelrmrz.pokedex.settings.SettingsActivity;
import com.noelrmrz.pokedex.ui.detail.DetailFragmentDirections;
import com.noelrmrz.pokedex.ui.main.MainFragment;
import com.noelrmrz.pokedex.ui.main.MainFragmentDirections;
import com.noelrmrz.pokedex.ui.recyclerview.PokemonAdapter;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.RetrofitClient;
import com.noelrmrz.pokedex.widget.PokedexWidgetService;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements
        PokemonAdapter.PokemonAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private MainFragment mainFragment = MainFragment.newInstance();
    private AppBarConfiguration appBarConfiguration;
    private InterstitialAd mInterstitialAd;
    private final String mSharedPrefFile = "com.noelrmrz.pokedex";
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAdMob();
        updateWidgetService();
        setupSharedPreferences();
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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
    }

    private void setupAdMob() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // Set the AdUnitId for Google's test ad.  Replace with own AppId when publishing.
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        // Load the ad
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    public void onClick(Pokemon pokemon, int position, View view) {
        // DetailFragment detailFragment = DetailFragment.newInstance(GsonClient.getGsonClient().toJson(pokemon));

        navigateToDetailFragment(pokemon);
        //NavDirections action = MainFragmentDirections.actionMainFragmentToDetailFragment(GsonClient.getGsonClient().toJson(pokemon));
        //Navigation.findNavController(view).navigate(action);
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
        editText.setHintTextColor(getResources().getColor((android.R.color.black)));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Start Settings Activity if the id matches
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent
                        (this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            handleSearchIntent(intent);
        } else if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            handleWidgetIntent(intent);
        } else {
        }
    }

    private void handleSearchIntent(Intent intent) {
        String query = intent.getStringExtra(SearchManager.QUERY);

        // Google Analytics to measure what users search for
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, query);
        // or use this
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SEARCH, params);

        //use the query to search your data somehow
        RetrofitClient.getPokemonInformation(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful()) {
                    Pokemon pokemon = response.body();
                    RetrofitClient.getSpeciesInformation(new Callback<PokemonSpecies>() {
                        @Override
                        public void onResponse(Call<PokemonSpecies> call, Response<PokemonSpecies> response) {
                            pokemon.setPokemonSpecies(response.body());
                            navigateToDetailFragment(pokemon);
                        }

                        @Override
                        public void onFailure(Call<PokemonSpecies> call, Throwable t) {
                            Timber.d(t);
                        }
                    }, query);
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Timber.d(t);
            }
        }, query);
    }

    private void handleWidgetIntent(Intent intent) {
        // If the ad is loaded then display it before transitioning to the detail fragment
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Timber.d("The interstitial wasn't loaded yet.");
        }

        // Check for extras in the Intent
        Pokemon savedPokemon = GsonClient.getGsonClient().fromJson(
                intent.getStringExtra(Intent.EXTRA_TEXT),
                Pokemon.class);
        navigateToDetailFragment(savedPokemon);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_container);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void navigateToDetailFragment(Pokemon pokemon) {
        // Navigate to DetailFragment
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_container);
        NavDirections action = MainFragmentDirections
                .actionMainFragmentToDetailFragment(GsonClient.getGsonClient().toJson(pokemon));
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_container);

        try {
            navHostFragment.getNavController().navigate(action);
        } catch (IllegalArgumentException e) {
            Timber.d(e);
            // User case when navigating from the DetailFragment to itself
            action = DetailFragmentDirections
                    .actionDetailFragmentSelf(GsonClient.getGsonClient().toJson(pokemon));
            navHostFragment.getNavController().navigate(action);
        }
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_key))) {
            // change and reload the views
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_container, MainFragment.newInstance())
                    .commitAllowingStateLoss(); // should not do this
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
    }

    public void updateWidgetService() {
        SharedPreferences.Editor editor = getSharedPreferences(mSharedPrefFile,
                Context.MODE_PRIVATE).edit();

        RetrofitClient.getPokemonInformation(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (response.isSuccessful()) {
                    Pokemon pokemon = response.body();

                    RetrofitClient.getSpeciesInformation(new Callback<PokemonSpecies>() {
                        @Override
                        public void onResponse(Call<PokemonSpecies> call, Response<PokemonSpecies> response) {
                            if (response.isSuccessful()) {
                                pokemon.setPokemonSpecies(response.body());
                                editor.putString("pokemon_json_string", GsonClient.getGsonClient().toJson(pokemon));
                                editor.apply();

                                PokedexWidgetService.startActionOpenPokemon(getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<PokemonSpecies> call, Throwable t) {
                            Timber.d(t);
                        }
                    }, pokemon.getName());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Timber.d(t);
            }
        }, String.valueOf(getRandomPokemonId()));
    }

    private int getRandomPokemonId() {
        return new Random().nextInt(720) + 1;
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Timber.d(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

