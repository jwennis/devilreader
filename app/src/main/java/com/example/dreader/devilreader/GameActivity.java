package com.example.dreader.devilreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dreader.devilreader.model.Game;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {

    private Game mGame;

    @BindView(R.id.game_date)
    TextView date;

    @BindView(R.id.game_arena)
    TextView arena;

    @BindView(R.id.game_city)
    TextView city;


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

        date.setText(mGame.getLongDate());

        if(mGame.isHome()) {

            arena.setText(Util.getTeamArena("NJD"));
            city.setText(Util.getTeamCity("NJD"));

        } else  {

            arena.setText(Util.getTeamArena(mGame.getOpponent()));
            city.setText(Util.getTeamCity(mGame.getOpponent()));
        }






    }
}
