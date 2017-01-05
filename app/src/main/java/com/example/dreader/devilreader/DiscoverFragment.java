package com.example.dreader.devilreader;


import android.database.Cursor;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dreader.devilreader.data.StoryContract;
import com.example.dreader.devilreader.firebase.FirebaseCallback;
import com.example.dreader.devilreader.firebase.FirebaseUtil;
import com.example.dreader.devilreader.ui.StoryAdapter;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DiscoverFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String PARAM_PLAYOFF_OUTLOOK = "PARAM_PLAYOFF_OUTLOOK";

    private static final int LOADER_ID = 3;

    private StoryAdapter mAdapter;
    private byte[] mPlayoffOutlook;

    private ViewGroup layout_root;

    @BindString(R.string.typeface_arvo_bold)
    String TYPEFACE_ARVO_BOLD;

    @BindView(R.id.discover_subtitle_headlines)
    TextView subtitle_headlines;

    @BindView(R.id.discover_headlines_recycler)
    RecyclerView headlines_recycler;

    @BindView(R.id.discover_subtitle_outlook)
    TextView subtitle_outlook;

    @BindView(R.id.discover_playoff_outlook)
    ImageView playoff_outlook;

    public DiscoverFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout_root = (ViewGroup) inflater.inflate(R.layout.fragment_discover, container, false);

        ButterKnife.bind(this, layout_root);

        Typeface TypefaceArvoBold =
                Typeface.createFromAsset(getContext().getAssets(), TYPEFACE_ARVO_BOLD);

        subtitle_headlines.setTypeface(TypefaceArvoBold);
        subtitle_outlook.setTypeface(TypefaceArvoBold);

        if(savedInstanceState != null) {

            mPlayoffOutlook = savedInstanceState.getByteArray(PARAM_PLAYOFF_OUTLOOK);

            bindPlayoffOutlook();

        } else {

            initPlayoffOutlook();
        }

        getActivity().getSupportLoaderManager()
                .initLoader(LOADER_ID, null, this).forceLoad();

        return layout_root;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == LOADER_ID) {

            String[] projection = new String[]{};
            String selection = "";
            String[] selectionArgs = new String[]{};
            String sortOrder = String.format("%1$s DESC LIMIT 3", StoryContract.StoryEntry.COL_PUBDATE);

            return new CursorLoader(getContext(), StoryContract.StoryEntry.CONTENT_URI,
                    projection, selection, selectionArgs, sortOrder);
        }

        return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(loader.getId() == LOADER_ID) {

            if (mAdapter == null) {

                mAdapter = new StoryAdapter(data, DiscoverFragment.class.getSimpleName());

                headlines_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                headlines_recycler.setItemAnimator(new DefaultItemAnimator());
                headlines_recycler.setAdapter(mAdapter);

            } else {

                mAdapter.swapCursor(data);
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);
    }


    private void initPlayoffOutlook() {

        FirebaseUtil.queryStorage("playoff_outlook.png", new FirebaseCallback() {

            @Override
            public void onByteArrayResult(byte[] bytes) {

                mPlayoffOutlook = bytes;

                bindPlayoffOutlook();
            }
        });
    }


    private void bindPlayoffOutlook() {

        Glide.with(this)
                .load(mPlayoffOutlook)
                .asBitmap()
                .into(playoff_outlook);
    }

}
