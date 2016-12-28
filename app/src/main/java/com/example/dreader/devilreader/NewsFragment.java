package com.example.dreader.devilreader;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreader.devilreader.data.StoryContract.StoryEntry;
import com.example.dreader.devilreader.sync.StorySyncAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 0;

    private StoryAdapter mAdapter;
    private ViewGroup layout_root;

    @BindView(R.id.news_recycler)
    RecyclerView recycler;


    public NewsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout_root = (ViewGroup) inflater.inflate(R.layout.fragment_news, container, false);

        bindNews();

        return layout_root;
    }

    private void bindNews() {

        ButterKnife.bind(this, layout_root);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setItemAnimator(new DefaultItemAnimator());

        getActivity().setTitle(R.string.drawer_news);

        StorySyncAdapter.syncImmediately(getContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager()
                .initLoader(LOADER_ID, null, this).forceLoad();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(id == LOADER_ID) {

            String[] projection = new String[] { };
            String selection = "";
            String[] selectionArgs = new String[] { };
            String sortOrder = String.format("%1$s DESC", StoryEntry.COL_PUBDATE);

            return new CursorLoader(getContext(), StoryEntry.CONTENT_URI,
                    projection, selection, selectionArgs, sortOrder);
        }

        return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(loader.getId() == LOADER_ID) {

            //Log.v("DREADER", DatabaseUtils.dumpCursorToString(data));

            if(mAdapter == null) {

                mAdapter = new StoryAdapter(data, NewsFragment.class.getSimpleName());
                recycler.setAdapter(mAdapter);

            } else {

                mAdapter.swapCursor(data);
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        if(loader.getId() == LOADER_ID) {

            // TODO: swap cursor
        }
    }
}
