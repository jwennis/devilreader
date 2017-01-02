package com.example.dreader.devilreader;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreader.devilreader.firebase.FirebaseCallback;
import com.example.dreader.devilreader.firebase.FirebaseUtil;
import com.example.dreader.devilreader.model.Game;
import com.example.dreader.devilreader.ui.GameAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ScheduleFragment extends Fragment {

    private List<Game> mSchedule;
    private GameAdapter mAdapter;

    @BindView(R.id.schedule_recycler)
    RecyclerView schedule_recycler;


    public ScheduleFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Util.setTitle(getActivity(), R.string.drawer_schedule);

        View layout_root = inflater.inflate(R.layout.fragment_schedule, container, false);

        ButterKnife.bind(this, layout_root);

        schedule_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        schedule_recycler.setItemAnimator(new DefaultItemAnimator());

        return layout_root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {

            mSchedule = savedInstanceState.getParcelableArrayList(Game.PARAM_GAME_PARCEL);

            bindSchedule();

        } else {

            initSchedule();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putStringArrayList(Game.PARAM_GAME_PARCEL, (ArrayList) mSchedule);

        super.onSaveInstanceState(outState);
    }


    private void initSchedule() {

        FirebaseUtil.queryGame(FirebaseUtil.ORDER_BY, "datestring", new FirebaseCallback() {

            @Override
            public void onGameResult(List<Game> list) {

                mSchedule = list;

                bindSchedule();
            }
        });
    }


    private void bindSchedule() {

        if(mAdapter == null) {

            mAdapter = new GameAdapter(mSchedule);
            schedule_recycler.setAdapter(mAdapter);

            // Scroll to first pending game

            int index = 0;

            for(Game game : mSchedule) {

                if(!game.isPending()) {

                    index++;

                } else {

                    schedule_recycler.scrollToPosition(index);

                    break;
                }
            }
        }
    }
}
