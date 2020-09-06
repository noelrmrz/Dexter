package com.noelrmrz.pokedex.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.noelrmrz.pokedex.ui.recyclerview.StatsFragment;

public class TabsAdapter extends FragmentStateAdapter {
    private String pokemonJSON;
    public TabsAdapter(Fragment fragment, String pokemonJSON) {
        super(fragment);
        this.pokemonJSON = pokemonJSON;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        // Return new fragment instance in createFragment(int)
        switch (position) {
            case 0:
                fragment = InformationFragment.newInstance(pokemonJSON);
                break;
            case 1:
                fragment = AttackListFragment.newInstance(pokemonJSON);
                break;
            default:
                fragment = StatsFragment.newInstance(pokemonJSON);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
