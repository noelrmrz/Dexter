package com.noelrmrz.pokedex.ui.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.databinding.RvAttackListItemBinding;
import com.noelrmrz.pokedex.pojo.Move;

import java.util.ArrayList;
import java.util.List;

public class AttackListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Move> mMoveList = new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Get the Context and ID of our layout for the list items in RecyclerView
        mContext = viewGroup.getContext();
        int layoutIdForListItem;

        // Get the LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        // Inflate our layout into the view
        View view;

        if (viewType == VIEW_TYPE_ITEM) {
            RvAttackListItemBinding itemBinding = RvAttackListItemBinding.inflate(inflater,
                    viewGroup, shouldAttachToParentImmediately);
            return new AttackListAdapterViewHolder(itemBinding);
        } else {
            layoutIdForListItem = R.layout.rv_list_loading;
            view = inflater.inflate(layoutIdForListItem, viewGroup,
                    shouldAttachToParentImmediately);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int position) {
        if (viewHolder instanceof AttackListAdapterViewHolder) {
            populateItemView((AttackListAdapterViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        // Progress bar displays
    }

    private void populateItemView(AttackListAdapterViewHolder viewHolder, int position) {
        Move move = mMoveList.get(position);
        viewHolder.bind(move);
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

    @Override
    public int getItemViewType(int position) {
        if (mMoveList.get(position) == null) {
            return LoadingViewHolder.VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public class AttackListAdapterViewHolder extends RecyclerView.ViewHolder {

        private RvAttackListItemBinding bind;

        public AttackListAdapterViewHolder(RvAttackListItemBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
        }

        /**
         * We will use this function to bind instance of Move to the row
         */
        public void bind(Move move) {
            bind.setMove(move);
            bind.executePendingBindings();
        }
    }

    public void addToMoveList(Move move) {
        mMoveList.add(move);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mMoveList.remove(position);
        notifyItemRemoved(getItemCount());
    }

    public void setMoveList(List<Move> moveList) {
        mMoveList.addAll(moveList);
        notifyDataSetChanged();
    }
}
