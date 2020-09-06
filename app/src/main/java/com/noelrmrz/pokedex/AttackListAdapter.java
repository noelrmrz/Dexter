package com.noelrmrz.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.POJO.FlavorTextEntry;
import com.noelrmrz.pokedex.POJO.Move;

import java.util.ArrayList;
import java.util.List;

public class AttackListAdapter extends RecyclerView.Adapter<AttackListAdapter.AttackListAdapterViewHolder> {

    private Context mContext;
    private List<Move> mMoveList = new ArrayList<>();

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
        viewHolder.attackName.setText(mMoveList.get(position).getName());
        viewHolder.attackType.setText(mMoveList.get(position).getType().getName());
        viewHolder.attackPP.setText(String.valueOf(mMoveList.get(position).getPp()));
        viewHolder.attackDamage.setText(String.valueOf(mMoveList.get(position).getPower()));
        viewHolder.attackAccuracy.setText(String.valueOf(mMoveList.get(position).getAccuracy()));
        viewHolder.attackDescription.setText(getLanguage("EN", mMoveList.get(position).getFlavorTextEntries()));
        //viewHolder.attackFlavor.setImageResource();
    }

    @Override
    public int getItemCount() {
        if (mMoveList == null) {
            return 0;
        }
        else {
            return mMoveList.size();
        }
    }

    public class AttackListAdapterViewHolder extends RecyclerView.ViewHolder {

        private ImageView attackFlavor;
        private TextView attackName;
        private TextView attackPP;
        private TextView attackDescription;
        private TextView attackAccuracy;
        private TextView attackDamage;
        private TextView attackType;

        public AttackListAdapterViewHolder(@NonNull View view) {
            super(view);

            attackFlavor = view.findViewById(R.id.iv_attack_flavor);
            attackName = view.findViewById(R.id.tv_attack_name);
            attackPP = view.findViewById(R.id.tv_attack_pp);
            attackDescription = view.findViewById(R.id.tv_attack_description);
            attackAccuracy = view.findViewById(R.id.tv_attack_accuracy);
            attackDamage = view.findViewById(R.id.tv_attack_damage);
            attackType = view.findViewById(R.id.tv_attack_type);
        }
    }

    public void setMoveList(List<Move> moveList) {
        mMoveList.addAll(moveList);
        notifyDataSetChanged();
    }

    private String getLanguage(String language, FlavorTextEntry[] flavorTextEntries) {
        for (FlavorTextEntry flavorTextEntry: flavorTextEntries) {
            if (flavorTextEntry.getLanguage().getLanguage().equalsIgnoreCase(language))
                return flavorTextEntry.getFlavorText();
        }
        return language;
    }
}
