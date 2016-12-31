package com.example.dreader.devilreader;

import android.os.Bundle;
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
import com.example.dreader.devilreader.model.Player;
import com.example.dreader.devilreader.model.PlayerContract;
import com.example.dreader.devilreader.ui.ContractAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayerContractsFragment extends Fragment {

    private Player mPlayer;
    private ContractAdapter mAdapter;

    @BindView(R.id.contracts_recycler)
    RecyclerView contracts_recycler;


    public PlayerContractsFragment() {

    }


    @Override
    public void setArguments(Bundle args) {

        mPlayer = args.getParcelable(Player.PARAM_PLAYER_PARCEL);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(Player.PARAM_PLAYER_PARCEL, mPlayer);

        super.onSaveInstanceState(outState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_player_contracts, container, false);

        ButterKnife.bind(this, root);

        if(savedInstanceState != null) {

            mPlayer = savedInstanceState.getParcelable(Player.PARAM_PLAYER_PARCEL);
        }

        contracts_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        contracts_recycler.setItemAnimator(new DefaultItemAnimator());

        return root;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(mPlayer.getContracts() == null) {

            initContracts();

        } else {

            bindContracts();
        }
    }


    private void initContracts() {

        FirebaseUtil.queryContract(mPlayer.getNhl_id(), new FirebaseCallback() {

            @Override
            public void onContractResult(final List<PlayerContract> contracts) {

                mPlayer.setContracts(contracts);

                bindContracts();
            }
        });
    }


    private void bindContracts() {

        if(mAdapter == null) {

            List<PlayerContract> contracts = new ArrayList<>(mPlayer.getContracts()) ;

            Collections.reverse(contracts);

            mAdapter = new ContractAdapter(contracts);
            contracts_recycler.setAdapter(mAdapter);

        } else {

            //mAdapter.notifyDatasetChanged();
        }
    }
}
