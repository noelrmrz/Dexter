package com.noelrmrz.pokedex;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Fade;

import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.ui.DetailsTransition;
import com.noelrmrz.pokedex.ui.detail.DetailFragment;
import com.noelrmrz.pokedex.ui.main.MainFragment;
import com.noelrmrz.pokedex.ui.recyclerview.PokemonAdapter;
import com.noelrmrz.pokedex.utilities.GsonClient;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements PokemonAdapter.PokemonAdapterOnClickHandler {

    private MainFragment mainFragment = MainFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow();
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    public void onClick(Pokemon pokemon, int position) {
        //TODO: find out how to transition fragments.  from fragment or activity?
        // Create a new step fragment
        // Get access to each fragment
        DetailFragment detailFragment = DetailFragment.newInstance(GsonClient.getGsonClient().toJson(pokemon));

        // Check that the device is running lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Inflate transitions to apply
/*            Transition changeTransform = TransitionInflater.from(this)
                    .inflateTransition(R.transition.image_shared_element_transition);*/

            // Setup exit transition on first fragment
/*            mainFragment.setSharedElementEnterTransition(changeTransform);
            mainFragment.setExitTransition(changeTransform);*/

            // Setup exit transition on second fragment
            detailFragment.setSharedElementEnterTransition(new DetailsTransition());
            detailFragment.setEnterTransition(new Fade());
            mainFragment.setExitTransition(new Fade());
            detailFragment.setSharedElementReturnTransition(new DetailsTransition());

            // Find the shared element in the first fragment
            ImageView ivProfile = findViewById(R.id.iv_pokemon_sprite);

            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .addSharedElement(ivProfile, "iv_profile_large")
                    .replace(R.id.container, detailFragment, detailFragment.getClass().getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
        else {

        }
    }
}