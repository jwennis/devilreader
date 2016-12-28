package com.example.dreader.devilreader;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreader.devilreader.data.StoryContract.StoryEntry;
import com.example.dreader.devilreader.sync.StorySyncAdapter;

public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 0;


    public NewsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.drawer_news);

        StorySyncAdapter.syncImmediately(getContext());

        return inflater.inflate(R.layout.fragment_news, container, false);
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

            Log.v("DREADER", DatabaseUtils.dumpCursorToString(data));
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        if(loader.getId() == LOADER_ID) {

            // TODO: swap cursor
        }
    }
}
