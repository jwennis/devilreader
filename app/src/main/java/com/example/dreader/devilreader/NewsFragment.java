package com.example.dreader.devilreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreader.devilreader.sync.StorySyncAdapter;

public class NewsFragment extends Fragment {

    public NewsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.drawer_news);

        StorySyncAdapter.syncImmediately(getContext());

        return inflater.inflate(R.layout.fragment_news, container, false);
    }
}
