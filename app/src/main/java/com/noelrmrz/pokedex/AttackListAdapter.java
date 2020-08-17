package com.noelrmrz.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.sargunvohra.lib.pokekotlin.model.Move;

public class AttackListAdapter extends RecyclerView.Adapter<AttackListAdapter.AttackListAdapterViewHolder> {

    private Context mContext;
    private Move[] mMoveList;

    public AttackListAdapter.AttackListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Get the Context and ID of our layout for the list items in RecyclerView
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_attack_list_item;

        // Get the LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        // Inflate our layout into the view
        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);
        return new AttackListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttackListAdapterViewHolder viewHolder,
                                 int position) {
    }

    @Override
    public int getItemCount() {
        if (mMoveList == null) {
            return 0;
        }
        else {
            return mMoveList.length;
        }
    }

    public class AttackListAdapterViewHolder extends RecyclerView.ViewHolder {

        public AttackListAdapterViewHolder(@NonNull View view) {
            super(view);
        }
    }
}
