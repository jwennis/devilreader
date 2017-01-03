package com.example.dreader.devilreader;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dreader.devilreader.model.Game;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class GameActivity extends AppCompatActivity {

    private Game mGame;

    @BindString(R.string.typeface_arvo_bold)
    String TYPEFACE_ARVO_BOLD;

    @BindView(R.id.game_date)
    TextView date;

    @BindView(R.id.game_arena)
    TextView arena;

    @BindView(R.id.game_city)
    TextView city;

    @BindView(R.id.game_scoring_heading)
    ViewGroup scoring_heading;

    @BindView(R.id.game_scoring_away)
    ViewGroup scoring_away;

    @BindView(R.id.game_scoring_home)
    ViewGroup scoring_home;

    @BindView(R.id.game_scoring_away_icon)
    ImageView scoring_away_icon;

    @BindView(R.id.game_scoring_home_icon)
    ImageView scoring_home_icon;

    @BindView(R.id.game_label_recap)
    TextView label_recap;

    @BindView(R.id.game_recap_backdrop)
    ImageView recap_backdrop;

    @BindView(R.id.game_recap_play)
    ImageView recap_play;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        if(savedInstanceState != null) {

            mGame = savedInstanceState.getParcelable(Game.PARAM_GAME_PARCEL);

        } else {

            mGame = getIntent().getExtras().getParcelable(Game.PARAM_GAME_PARCEL);
        }

        bindGame();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(Game.PARAM_GAME_PARCEL, mGame);

        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {

            onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    private void bindGame() {

        ButterKnife.bind(this);

        Typeface TypefaceArvoBold = Typeface.createFromAsset(getAssets(), TYPEFACE_ARVO_BOLD);

        date.setText(mGame.getLongDate());
        date.setTypeface(TypefaceArvoBold);

        if(mGame.isHome()) {

            arena.setText(Util.getTeamArena("NJD"));
            city.setText(Util.getTeamCity("NJD"));

            scoring_away_icon.setImageResource(Util.getTeamIcon(mGame.getOpponent()));
            scoring_home_icon.setImageResource(Util.getTeamIcon("NJD"));

        } else  {

            arena.setText(Util.getTeamArena(mGame.getOpponent()));
            city.setText(Util.getTeamCity(mGame.getOpponent()));

            scoring_away_icon.setImageResource(Util.getTeamIcon("NJD"));
            scoring_home_icon.setImageResource(Util.getTeamIcon(mGame.getOpponent()));
        }

        // Scoring summary

        List<String> goalsAway = mGame.getGoalsAway();
        List<String> goalsHome = mGame.getGoalsHome();

        for(int i = 0; i < goalsHome.size(); i++) {

            TextView heading = (TextView) scoring_heading.getChildAt(i + 1);
            TextView away = (TextView) scoring_away.getChildAt(i + 1);
            TextView home = (TextView) scoring_home.getChildAt(i + 1);

            if(i != 3) {

                heading.setText(Util.formatNumSuffix(i + 1));
                away.setText(goalsAway.get(i));
                home.setText(goalsHome.get(i));

            } else if(goalsHome.size() == 5) {

                heading.setText(mGame.getStatus());
                away.setText(goalsAway.get(i));
                home.setText(goalsHome.get(i));

            } else {

                heading.setVisibility(View.GONE);
                away.setVisibility(View.GONE);
                home.setVisibility(View.GONE);

                ((TextView) scoring_away.getChildAt(i + 2)).setText(goalsAway.get(i));
                ((TextView) scoring_home.getChildAt(i + 2)).setText(goalsHome.get(i));
            }
        }

        // Recap

        label_recap.setTypeface(TypefaceArvoBold);

        Glide.with(this)
                .load(mGame.getRecapImage())
                .into(recap_backdrop);

        recap_play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent recapIntent = new Intent(Intent.ACTION_VIEW);
                recapIntent.setDataAndType(Uri.parse(mGame.getRecapVideo()),"video/mp4");

                startActivityForResult(recapIntent, 0);
            }
        });

    }
}
