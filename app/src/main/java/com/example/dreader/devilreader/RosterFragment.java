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
import com.example.dreader.devilreader.model.PlayerContract;
import com.example.dreader.devilreader.ui.RosterLabelAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RosterFragment extends Fragment {

    private List<Player> mRoster;
    private List<Player> mForwards;
    private List<Player> mDefensemen;
    private List<Player> mGoaltenders;
    private List<Player> mInjured;
    private List<Player> mNonroster;

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


    private AsyncTask mProcessPlayersTask = new AsyncTask<Player, Integer, Boolean>() {

        @Override
        protected Boolean doInBackground(final Player... players) {

            final List<Player> processed = new ArrayList<>();

            for(final Player player : players) {

                FirebaseUtil.queryContract(player.getNhl_id(), new FirebaseCallback() {

                    @Override
                    public void onContractResult(List<PlayerContract> list) {

                        player.setContracts(list);

                        processed.add(player);

                        if(processed.size() == players.length) {

                            sortRoster(processed);
                        }
                    }
                });
            }

            return false;
        }

        private void sortRoster(List<Player> roster) {

            Collections.sort(roster, new Comparator<Player>() {

                @Override
                public int compare(Player p1, Player p2) {

                    return p2.getCapHit() - p1.getCapHit();
                }
            });

            mForwards = new ArrayList<>();
            mDefensemen = new ArrayList<>();
            mGoaltenders = new ArrayList<>();
            mInjured = new ArrayList<>();
            mNonroster = new ArrayList<>();

            for(Player player : roster) {

                if(player.getIs_injured()) {

                    mInjured.add(player);

                } else if (!player.getIs_roster()) {

                    mNonroster.add(player);

                } else if(Pattern.matches("C|LW|RW", player.getPosition())) {

                    mForwards.add(player);

                } else if (player.getPosition().equals("D")) {

                    mDefensemen.add(player);

                } else {

                    mGoaltenders.add(player);
                }
            }

            onPostExecute(true);
        }

        @Override
        protected void onPostExecute(Boolean isSorted) {

            if(isSorted) {

                bindRoster();
            }
        }
    };


    private void bindRoster() {

        ButterKnife.bind(this, layout_root);

        Util.setTitle(getActivity(), R.string.drawer_roster);

        initRecycler(mForwards, forward_labels, forward_salaries);

        label_container.setVisibility(View.VISIBLE);
        salary_container.setVisibility(View.VISIBLE);
    }

    private void initRecycler(List<Player> players,
                              RecyclerView labelRecycler, RecyclerView salaryRecycler) {

        labelRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        labelRecycler.setItemAnimator(new DefaultItemAnimator());
        labelRecycler.setAdapter(new RosterLabelAdapter(players));

//        salaryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
//        salaryRecycler.setItemAnimator(new DefaultItemAnimator());
        //salaryRecycler.setAdapter(new RosterSalaryAdapter(getContext(), players));


    }
}
