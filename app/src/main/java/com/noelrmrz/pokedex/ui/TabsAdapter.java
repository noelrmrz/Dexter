package com.noelrmrz.pokedex.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabsAdapter extends FragmentStateAdapter {

    public TabsAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        // Return new fragment instance in createFragment(int)
        switch (position) {
/*            case 1:
                fragment = InformationFragment.newInstance();
                break;
            case 2:
                fragment = AttackListFragment.newInstance("Param1", "Param2");
                break;*/
            default:
                fragment = new Fragment();
        }
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
