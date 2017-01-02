package com.example.dreader.devilreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dreader.devilreader.model.Game;

public class GameActivity extends AppCompatActivity {

    private Game mGame;


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

    private void bindGame() {


    }
}
