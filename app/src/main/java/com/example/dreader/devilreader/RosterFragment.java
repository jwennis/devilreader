package com.example.dreader.devilreader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import android.os.AsyncTask;
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

import com.example.dreader.devilreader.firebase.FirebaseCallback;
import com.example.dreader.devilreader.firebase.FirebaseUtil;
import com.example.dreader.devilreader.model.Player;
import com.example.dreader.devilreader.model.PlayerContract;
import com.example.dreader.devilreader.ui.RosterLabelAdapter;
import com.example.dreader.devilreader.ui.RosterSalaryAdapter;


public class RosterFragment extends Fragment {

    private static final String PARAM_ROSTER_FORWARDS = "PARAM_ROSTER_FORWARDS";
    private static final String PARAM_ROSTER_DEFENSEMEN = "PARAM_ROSTER_DEFENSEMEN";
    private static final String PARAM_ROSTER_GOALTENDERS = "PARAM_ROSTER_GOALTENDERS";
    private static final String PARAM_ROSTER_INJURED = "PARAM_ROSTER_INJURED";
    private static final String PARAM_ROSTER_NONROSTER = "PARAM_ROSTER_NONROSTER";

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

    @BindView(R.id.roster_defense_labels)
    RecyclerView defense_labels;

    @BindView(R.id.roster_defense_salaries)
    RecyclerView defense_salaries;

    @BindView(R.id.roster_goaltender_labels)
    RecyclerView goaltender_labels;

    @BindView(R.id.roster_goaltender_salaries)
    RecyclerView goaltender_salaries;

    @BindView(R.id.roster_injured_labels)
    RecyclerView injured_labels;

    @BindView(R.id.roster_injured_salaries)
    RecyclerView injured_salaries;

    @BindView(R.id.roster_nonroster_labels)
    RecyclerView nonroster_labels;

    @BindView(R.id.roster_nonroster_salaries)
    RecyclerView nonroster_salaries;


    public RosterFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Util.setTitle(getActivity(), R.string.drawer_roster);

        layout_root = (ViewGroup) inflater.inflate(R.layout.fragment_roster, container, false);

        ButterKnife.bind(this, layout_root);

        if(savedInstanceState != null) {

            mForwards = (ArrayList) savedInstanceState.getParcelableArrayList(PARAM_ROSTER_FORWARDS);
            mDefensemen = (ArrayList) savedInstanceState.getParcelableArrayList(PARAM_ROSTER_DEFENSEMEN);
            mGoaltenders = (ArrayList) savedInstanceState.getParcelableArrayList(PARAM_ROSTER_GOALTENDERS);
            mInjured = (ArrayList) savedInstanceState.getParcelableArrayList(PARAM_ROSTER_INJURED);
            mNonroster = (ArrayList) savedInstanceState.getParcelableArrayList(PARAM_ROSTER_NONROSTER);

            bindRoster();

        } else {

            mForwards = new ArrayList<>();
            mDefensemen = new ArrayList<>();
            mGoaltenders = new ArrayList<>();
            mInjured = new ArrayList<>();
            mNonroster = new ArrayList<>();

            initRoster();
        }

        return layout_root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(PARAM_ROSTER_FORWARDS, (ArrayList) mForwards);
        outState.putParcelableArrayList(PARAM_ROSTER_DEFENSEMEN, (ArrayList) mDefensemen);
        outState.putParcelableArrayList(PARAM_ROSTER_GOALTENDERS, (ArrayList) mGoaltenders);
        outState.putParcelableArrayList(PARAM_ROSTER_INJURED, (ArrayList) mInjured);
        outState.putParcelableArrayList(PARAM_ROSTER_NONROSTER, (ArrayList) mNonroster);

        super.onSaveInstanceState(outState);
    }


    private void initRoster() {

        FirebaseUtil.queryPlayer(FirebaseUtil.TEAM, Util.TEAM_ABBR_NJD, new FirebaseCallback() {

            @Override
            public void onPlayerResult(final List<Player> roster) {

                mRoster = roster;

                sortRoster();

                fetchContract(0);
            }
        });
    }


    private void fetchContract(final int index) {

        FirebaseUtil.queryContract(mRoster.get(index).getNhl_id(), new FirebaseCallback() {

            @Override
            public void onContractResult(final List<PlayerContract> contracts) {

                mRoster.get(index).setContracts(contracts);

                if(index < mRoster.size() - 1) {

                    fetchContract(index + 1);

                } else {

                    bindSalaries();
                }
            }
        });
    }


    private void sortRoster() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                Collections.sort(mRoster, new Comparator<Player>() {

                    @Override
                    public int compare(Player p1, Player p2) {

                        return (int) (p2.getCap_hit() - p1.getCap_hit());
                    }
                });

                for(Player player : mRoster) {

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

                return null;
            }

            @Override
            protected void onPostExecute(Void voidArg) {

                bindLabels();
            }

        }.execute();
    }


    private void bindRoster() {

        bindLabels();
        bindSalaries();
    }


    private void bindLabels() {

        setAdapter(forward_labels, new RosterLabelAdapter(mForwards));
        setAdapter(defense_labels, new RosterLabelAdapter(mDefensemen));
        setAdapter(goaltender_labels, new RosterLabelAdapter(mGoaltenders));
        setAdapter(injured_labels, new RosterLabelAdapter(mInjured));
        setAdapter(nonroster_labels, new RosterLabelAdapter(mNonroster));

        label_container.setVisibility(View.VISIBLE);
    }


    private void bindSalaries() {

        setAdapter(forward_salaries, new RosterSalaryAdapter(mForwards));
        setAdapter(defense_salaries, new RosterSalaryAdapter(mDefensemen));
        setAdapter(goaltender_salaries, new RosterSalaryAdapter(mGoaltenders));
        setAdapter(injured_salaries, new RosterSalaryAdapter(mInjured));
        setAdapter(nonroster_salaries, new RosterSalaryAdapter(mNonroster));

        salary_container.setVisibility(View.VISIBLE);
    }


    private void setAdapter(RecyclerView recycler, RecyclerView.Adapter adapter) {

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }
}
