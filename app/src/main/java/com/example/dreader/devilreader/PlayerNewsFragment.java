package com.example.dreader.devilreader;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreader.devilreader.model.Player;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayerNewsFragment extends Fragment {

    private Player mPlayer;

    @BindView(R.id.player_news_recycler)
    RecyclerView news_recycler;


    public PlayerNewsFragment() { }


    @Override
    public void setArguments(Bundle args) {

        mPlayer = args.getParcelable(Player.PARAM_PLAYER_PARCEL);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout_root = inflater.inflate(R.layout.fragment_player_news, container, false);

        ButterKnife.bind(this, layout_root);

        news_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        news_recycler.setItemAnimator(new DefaultItemAnimator());

        if(savedInstanceState != null) {

            mPlayer = savedInstanceState.getParcelable(Player.PARAM_PLAYER_PARCEL);

            loadNews();

        } else {

            initNews();
        }

        return layout_root;
    }


    private void initNews() {


    }


    private void loadNews() {


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(Player.PARAM_PLAYER_PARCEL, mPlayer);

        super.onSaveInstanceState(outState);
    }




}