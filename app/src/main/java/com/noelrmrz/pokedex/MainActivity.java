package com.noelrmrz.pokedex;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.PokemonSpecies;
import com.noelrmrz.pokedex.settings.SettingsActivity;
import com.noelrmrz.pokedex.ui.detail.SearchFragment;
import com.noelrmrz.pokedex.ui.main.MainFragment;
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
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final String mSharedPrefFile = "com.noelrmrz.pokedex";
    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAdMob();
        setupSharedPreferences();
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        updateWidgetService();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, new MainFragment(), MainFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        // Get the SearchView and set the searchable.xml configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
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
                    .replace(R.id.nav_host_fragment_container, new MainFragment(),
                            MainFragment.class.getSimpleName())
                    .commitAllowingStateLoss(); // should not do this
        }
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
                                editor.putString("pokemon_json_string",
                                        GsonClient.getGsonClient().toJson(pokemon));
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

    private void handleWidgetIntent(Intent intent) {
        // If the ad is loaded then display it before transitioning to the detail fragment
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Timber.d("The interstitial wasn't loaded yet.");
        }

        // Check for extras in the Intent
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .addToBackStack(MainFragment.class.getSimpleName())
                .replace(R.id.fragment_container, SearchFragment.newInstance(intent.getStringExtra(Intent.EXTRA_TEXT)), SearchFragment.class.getSimpleName())
                .commit();
    }

    private void setupAdMob() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // Set the AdUnitId for Google's test ad.  Replace with own AppId when publishing.
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
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
                        public void onResponse(Call<PokemonSpecies> call,
                                               Response<PokemonSpecies> response) {
                            pokemon.setPokemonSpecies(response.body());

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .addToBackStack(MainFragment.class.getSimpleName())
                                    .replace(R.id.fragment_container, SearchFragment.newInstance(GsonClient.getGsonClient().toJson(pokemon)), SearchFragment.class.getSimpleName())
                                    .commit();
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

