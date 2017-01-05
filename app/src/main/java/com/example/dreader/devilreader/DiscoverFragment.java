package com.example.dreader.devilreader;


import android.content.Intent;
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
import com.example.dreader.devilreader.model.Game;
import com.example.dreader.devilreader.ui.StoryAdapter;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DiscoverFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String PARAM_LAST_GAME = "PARAM_LAST_GAME";
    private static final String PARAM_NEXT_GAME = "PARAM_NEXT_GAME";
    private static final String PARAM_PLAYOFF_OUTLOOK = "PARAM_PLAYOFF_OUTLOOK";

    private static final int LOADER_ID = 3;

    private StoryAdapter mAdapter;
    private Game mLastGame;
    private Game mNextGame;
    private byte[] mPlayoffOutlook;

    private ViewGroup layout_root;

    @BindString(R.string.typeface_arvo_normal)
    String TYPEFACE_ARVO_NORMAL;

    @BindString(R.string.typeface_arvo_bold)
    String TYPEFACE_ARVO_BOLD;

    @BindView(R.id.discover_last_game)
    ViewGroup last_game;

    @BindView(R.id.discover_last_game_label)
    TextView last_game_label;

    @BindView(R.id.discover_last_game_icon)
    ImageView last_game_icon;

    @BindView(R.id.discover_last_game_score)
    TextView last_game_score;

    @BindView(R.id.discover_last_game_status)
    TextView last_game_status;

    @BindView(R.id.discover_next_game_label)
    TextView next_game_label;

    @BindView(R.id.discover_next_game_icon)
    ImageView next_game_icon;

    @BindView(R.id.discover_next_game_date)
    TextView next_game_date;

    @BindView(R.id.discover_next_game_time)
    TextView next_game_time;

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

        Util.setTitle(getActivity(), R.string.app_name);

        Typeface TypefaceArvoNormal =
                Typeface.createFromAsset(getContext().getAssets(), TYPEFACE_ARVO_NORMAL);

        Typeface TypefaceArvoBold =
                Typeface.createFromAsset(getContext().getAssets(), TYPEFACE_ARVO_BOLD);

        last_game_label.setTypeface(TypefaceArvoBold);
        next_game_label.setTypeface(TypefaceArvoBold);
        subtitle_headlines.setTypeface(TypefaceArvoBold);
        subtitle_outlook.setTypeface(TypefaceArvoBold);

        last_game_score.setTypeface(TypefaceArvoNormal);
        last_game_status.setTypeface(TypefaceArvoNormal);
        next_game_date.setTypeface(TypefaceArvoNormal);
        next_game_time.setTypeface(TypefaceArvoNormal);

        if(savedInstanceState != null) {

            mLastGame = savedInstanceState.getParcelable(PARAM_LAST_GAME);
            mNextGame = savedInstanceState.getParcelable(PARAM_NEXT_GAME);
            bindGamePreview();

            mPlayoffOutlook = savedInstanceState.getByteArray(PARAM_PLAYOFF_OUTLOOK);
            bindPlayoffOutlook();

        } else {

            initGamePreview();
            initPlayoffOutlook();
        }

        getActivity().getSupportLoaderManager()
                .initLoader(LOADER_ID, null, this).forceLoad();

        return layout_root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(PARAM_LAST_GAME, mLastGame);
        outState.putParcelable(PARAM_NEXT_GAME, mNextGame);
        outState.putByteArray(PARAM_PLAYOFF_OUTLOOK, mPlayoffOutlook);

        super.onSaveInstanceState(outState);
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








    private void initGamePreview() {

        FirebaseUtil.queryGame(FirebaseUtil.ORDER_BY, "datestring", new FirebaseCallback() {

            @Override
            public void onGameResult(List<Game> games) {

                for(Game game : games) {

                    if(mLastGame == null || !game.isPending()) {

                        mLastGame = game;

                    } else {

                        mNextGame = game;

                        break;
                    }
                }

                bindGamePreview();
            }
        });
    }


    private void bindGamePreview() {

        last_game_icon.setImageResource(Util.getTeamIcon(mLastGame.getOpponent()));
        last_game_score.setText(mLastGame.getResultLabel());

        String statusText = "FINAL";

        if(!mLastGame.getStatus().equals("F")) {

            statusText += "/" + mLastGame.getStatus();
        }

        last_game_status.setText(statusText);

        last_game.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent gameIntent = new Intent(getContext(), GameActivity.class);
                gameIntent.putExtra(Game.PARAM_GAME_PARCEL, mLastGame);

                startActivity(gameIntent);
            }
        });


        next_game_icon.setImageResource(Util.getTeamIcon(mNextGame.getOpponent()));
        next_game_date.setText(mNextGame.getDate());
        next_game_time.setText(mNextGame.getPuckdrop());
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
