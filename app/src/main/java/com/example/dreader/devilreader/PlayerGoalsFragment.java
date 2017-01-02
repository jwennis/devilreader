package com.example.dreader.devilreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.dreader.devilreader.model.Player;

public class PlayerGoalsFragment extends Fragment {

    private Player mPlayer;

    @BindView(R.id.goals_recycler)
    RecyclerView goals_recycler;


    public PlayerGoalsFragment() {

    }


    @Override
    public void setArguments(Bundle args) {

        mPlayer = args.getParcelable(Player.PARAM_PLAYER_PARCEL);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_player_goals, container, false);

        ButterKnife.bind(this, root);

        goals_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        goals_recycler.setItemAnimator(new DefaultItemAnimator());

        if(savedInstanceState != null) {

            mPlayer = savedInstanceState.getParcelable(Player.PARAM_PLAYER_PARCEL);
        }

        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(Player.PARAM_PLAYER_PARCEL, mPlayer);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

//        if(mGoals == null) {
//
//            initContracts();
//
//        } else {
//
//            bindContracts();
//        }
    }


    private void initGoals() {

//        FirebaseUtil.queryGoals(mPlayer.getNhl_id(), new FirebaseCallback() {
//
//            @Override
//            public void onGoalResult(final List<Goal> goals) {
//
//                //...
//
//                bindGoals();
//            }
//        });
    }


    private void bindGoals() {

//        if(mAdapter == null) {
//
//            mAdapter = new GoalAdapter(mGoals);
//            goals_recycler.setAdapter(mAdapter);
//
//        } else {
//
//            //mAdapter.notifyDatasetChanged();
//        }
    }

}
