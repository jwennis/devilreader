package com.example.dreader.devilreader;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreader.devilreader.firebase.FirebaseCallback;
import com.example.dreader.devilreader.firebase.FirebaseUtil;
import com.example.dreader.devilreader.model.Story;

public class NewsFragment extends Fragment {

    public NewsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.drawer_news);

        FirebaseUtil.queryStory(FirebaseUtil.ORDER_BY, "pubdate", new FirebaseCallback() {

            @Override
            public void onResult(List<Story> list) {

                for(Story item : list) {

                    item.print();
                }
            }
        });

        return inflater.inflate(R.layout.fragment_news, container, false);
    }
}
