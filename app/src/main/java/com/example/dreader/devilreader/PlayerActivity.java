package com.example.dreader.devilreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dreader.devilreader.model.Player;

public class PlayerActivity extends AppCompatActivity {

    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        if(savedInstanceState != null) {

            mPlayer = savedInstanceState.getParcelable(Player.PARAM_PLAYER_PARCEL);

        } else {

            mPlayer = getIntent().getExtras().getParcelable(Player.PARAM_PLAYER_PARCEL);
        }

        mPlayer.print();
    }
}
