package com.noelrmrz.pokedex.ui.recyclerview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.POJO.FlavorTextEntry;
import com.noelrmrz.pokedex.POJO.Move;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.utilities.HelperTools;

import java.util.ArrayList;
import java.util.List;

public class AttackListAdapter extends RecyclerView.Adapter<AttackListAdapter.AttackListAdapterViewHolder> {

    private Context mContext;
    private List<Move> mMoveList = new ArrayList<>();
    private final String PHYSICAL = "physical";
    private final String SPECIAL = "special";
    private final String LANGUAGE = "EN";
    private final String VERSION_NAME = "ultra-sun-ultra-moon";

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
        viewHolder.attackDescription.setText(getLanguage(LANGUAGE,
                mMoveList.get(position).getFlavorTextEntries()));

        // Change the background color depending on the primary type
        Drawable drawable = viewHolder.attackType.getBackground().mutate();
        PorterDuffColorFilter filter = new PorterDuffColorFilter(mContext.getResources()
                .getColor(HelperTools.getColor(mContext, mMoveList.get(position).getType().getName())),
                PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(filter);
        drawable.invalidateSelf();


        switch (mMoveList.get(position).getMoveDamageClass().getName()) {
            case PHYSICAL:
                viewHolder.attackFlavor.setImageDrawable(mContext
                        .getResources().getDrawable(R.drawable.physical));
                break;
            case SPECIAL:
                viewHolder.attackFlavor.setImageDrawable(mContext
                        .getResources().getDrawable(R.drawable.special));
                break;
            default:
                viewHolder.attackFlavor.setImageDrawable(mContext
                        .getResources().getDrawable(R.drawable.status));
        }
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
            if (flavorTextEntry.getLanguage().getLanguage().equalsIgnoreCase(language)
                    && flavorTextEntry.getVersionGroup()
                    .getName()
                    .equalsIgnoreCase(VERSION_NAME)) {
                // Remove 'newline' entries in text
                return flavorTextEntry.getFlavorText().replaceAll("(\n)", " ");
            }
        }
        return language;
    }
}
