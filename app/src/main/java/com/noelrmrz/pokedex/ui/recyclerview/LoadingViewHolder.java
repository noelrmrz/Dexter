package com.noelrmrz.pokedex.ui.recyclerview;

import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder {
    static final int VIEW_TYPE_LOADING = 1;

    ProgressBar progressBar;

    public LoadingViewHolder(View view) {
        super(view);
        progressBar = view.findViewById(R.id.indeterminate_bar);
    }
}
