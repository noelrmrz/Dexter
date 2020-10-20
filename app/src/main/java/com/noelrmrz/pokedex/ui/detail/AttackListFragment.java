package com.noelrmrz.pokedex.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noelrmrz.pokedex.ui.recyclerview.AttackListAdapter;
import com.noelrmrz.pokedex.POJO.Move;
import com.noelrmrz.pokedex.POJO.MoveLink;
import com.noelrmrz.pokedex.POJO.Pokemon;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.utilities.DividerItemDecoration;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttackListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttackListFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private Pokemon savedPokemon;
    private AttackListAdapter attackListAdapter;
    private static List<Move> mMoveList = new ArrayList<>();

    public AttackListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment AttackListFragment.
     */
    public static AttackListFragment newInstance(String param1) {
        AttackListFragment fragment = new AttackListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        attackListAdapter = new AttackListAdapter();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            savedPokemon = GsonClient.getGsonClient().fromJson(mParam1, Pokemon.class);

            attackListAdapter.addToMoveList(null);
            attackListAdapter.notifyItemInserted(attackListAdapter.getItemCount() - 1);

            // Get detailed information for each move
            for (MoveLink pokemonMove: savedPokemon.getMoveList()) {
                asyncCallback(pokemonMove.getMove().getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attack_list, container, false);
    }

    /*
    Triggered after onCreateView
    only called if the view from onCreateView is non-null
    View setup should occur here
    */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup the RecyclerView
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_attack_list);
        mRecyclerView.setAdapter(attackListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext()
                .getResources().getDrawable(R.drawable.divider_item_decoration)));
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void asyncCallback(String move) {
        RetrofitClient.getMoveList(new Callback<Move>() {
            @Override
            public void onResponse(Call<Move> call, Response<Move> response) {
                mMoveList.add(response.body());

                // Once all the moved have been retrieved proceed to add the collection to the adapter
                if (mMoveList.size() == savedPokemon.getMoveList().length) {
                    // Remove null entry item
                    attackListAdapter.remove(attackListAdapter.getItemCount() - 1);
                    // Add all moves to the adapters list
                    attackListAdapter.setMoveList(mMoveList);
                }
            }

            @Override
            public void onFailure(Call<Move> call, Throwable t) {

            }
        }, move);
    }
}