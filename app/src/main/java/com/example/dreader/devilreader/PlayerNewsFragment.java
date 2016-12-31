package com.example.dreader.devilreader;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreader.devilreader.data.StoryContract;
import com.example.dreader.devilreader.data.StoryContract.StoryEntry;
import com.example.dreader.devilreader.firebase.FirebaseCallback;
import com.example.dreader.devilreader.firebase.FirebaseUtil;
import com.example.dreader.devilreader.model.Player;
import com.example.dreader.devilreader.model.Story;
import com.example.dreader.devilreader.ui.StoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayerNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String PARAM_STORY_KEYS = "PARAM_STORY_KEYS";

    private static final int LOADER_ID = 1;

    private Player mPlayer;
    //private List<Story> mItems;
    private List<String> mTags;
    private StoryAdapter mAdapter;

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
        }

        return layout_root;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(mTags == null) {

            if(savedInstanceState != null
                    && savedInstanceState.containsKey(PARAM_STORY_KEYS)) {

                mTags = savedInstanceState.getStringArrayList(PARAM_STORY_KEYS);

            } else {

                initTags();

                return;
            }
        }

        initLoader();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(Player.PARAM_PLAYER_PARCEL, mPlayer);
        outState.putStringArrayList(PARAM_STORY_KEYS, (ArrayList) mTags);

        super.onSaveInstanceState(outState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(id == LOADER_ID) {

            StringBuilder selectionQs = new StringBuilder(" IN(");

            for(int i = 0; i < mTags.size(); i++) {

                selectionQs.append(i > 0 ? ",?" : "?");
            }

            selectionQs.append(")");

            String[] projection = new String[] { };
            String selection = StoryEntry.COL_ID + selectionQs.toString();
            String[] selectionArgs = mTags.toArray(new String[ mTags.size() ]);
            String sortOrder = StoryEntry.COL_PUBDATE + " DESC";

            return new CursorLoader(getContext(), StoryEntry.CONTENT_URI,
                    projection, selection, selectionArgs, sortOrder);
        }

        return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(loader.getId() == LOADER_ID) {

            if(mAdapter == null) {

                mAdapter = new StoryAdapter(data, PlayerNewsFragment.class.getSimpleName());
                news_recycler.setAdapter(mAdapter);

            } else {

                mAdapter.swapCursor(data);
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        if(loader.getId() == LOADER_ID) {

            mAdapter.swapCursor(null);
        }
    }


    private void initTags() {

        String playerId = Long.toString(mPlayer.getNhl_id());

        FirebaseUtil.queryStory(FirebaseUtil.TAG_PLAYER, playerId, new FirebaseCallback() {

            @Override
            public void onTagResult(List<String> tags) {

                mTags = tags;

                initLoader();
            }
        });
    }


    private void initLoader() {

        getActivity().getSupportLoaderManager()
                .initLoader(LOADER_ID, null, this).forceLoad();
    }
}
