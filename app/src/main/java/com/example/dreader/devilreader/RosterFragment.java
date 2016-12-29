package com.example.dreader.devilreader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreader.devilreader.firebase.FirebaseCallback;
import com.example.dreader.devilreader.firebase.FirebaseUtil;
import com.example.dreader.devilreader.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RosterFragment extends Fragment {

    private List<Player> mRoster;

    private ViewGroup layout_root;

    @BindView(R.id.roster_label_container)
    View label_container;

    @BindView(R.id.roster_salary_container)
    View salary_container;

    @BindView(R.id.roster_forward_labels)
    RecyclerView forward_labels;

    @BindView(R.id.roster_forward_salaries)
    RecyclerView forward_salaries;


    public RosterFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout_root = (ViewGroup) inflater.inflate(R.layout.fragment_roster, container, false);

        if(savedInstanceState != null) {

            mRoster = (ArrayList) savedInstanceState.getParcelableArrayList(Player.PARAM_PLAYER_PARCEL);

            bindRoster();

        } else {

            initRoster();
        }

        return layout_root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(Player.PARAM_PLAYER_PARCEL, (ArrayList) mRoster);

        super.onSaveInstanceState(outState);
    }


    private void initRoster() {

        FirebaseUtil.queryPlayer(FirebaseUtil.TEAM, "NJD", new FirebaseCallback() {

            @Override
            public void onPlayerResult(final List<Player> list) {

                mProcessPlayersTask.execute(list.toArray(new Player[list.size()]));
            }
        });
    }

    private AsyncTask mProcessPlayersTask = new AsyncTask<Player, Player, Void>() {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Player... players) {

            for(Player player : players) {








            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Player... params) {

            super.onProgressUpdate(params);
        }

        @Override
        protected void onPostExecute(Void args) {

            super.onPostExecute(args);

            bindRoster();
        }
    };


    private void bindRoster() {

        ButterKnife.bind(this, layout_root);

        Util.setTitle(getActivity(), R.string.drawer_roster);

        initRecycler(forward_labels);
        initRecycler(forward_salaries);

        label_container.setVisibility(View.VISIBLE);
        salary_container.setVisibility(View.VISIBLE);
    }

    private void initRecycler(RecyclerView recycler) {

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setItemAnimator(new DefaultItemAnimator());
    }
}
