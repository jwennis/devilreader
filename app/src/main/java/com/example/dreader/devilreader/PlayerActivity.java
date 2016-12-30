package com.example.dreader.devilreader;

import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dreader.devilreader.model.Player;
import com.example.dreader.devilreader.ui.CircleTransform;

import java.lang.reflect.Field;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivity extends AppCompatActivity {

    private Player mPlayer;

    @BindString(R.string.player_backdrop_root)
    String BACKDROP_ROOT;

    @BindString(R.string.player_mugshot_root)
    String MUGSHOT_ROOT;

    @BindString(R.string.typeface_arvo_bold)
    String TYPEFACE_ARVO_BOLD;

    @BindView(R.id.player_detail_backdrop)
    ImageView player_backdrop;

    @BindView(R.id.player_detail_mugshot)
    ImageView player_mugshot;

    @BindView(R.id.player_detail_name)
    TextView player_name;

    @BindView(R.id.player_detail_status)
    TextView player_status;

    @BindView(R.id.player_detail_age)
    TextView player_age;

    @BindView(R.id.player_detail_draft_year)
    TextView draft_year;

    @BindView(R.id.player_detail_draft_pos)
    TextView draft_position;

    @BindView(R.id.player_detail_position)
    TextView player_position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        if(savedInstanceState != null) {

            mPlayer = savedInstanceState.getParcelable(Player.PARAM_PLAYER_PARCEL);

        } else {

            mPlayer = getIntent().getExtras().getParcelable(Player.PARAM_PLAYER_PARCEL);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        bindPlayer();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(Player.PARAM_PLAYER_PARCEL, mPlayer);

        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {

            super.onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void bindPlayer() {

        mPlayer.print();

        ButterKnife.bind(this);

        Glide.with(this)
                .load(BACKDROP_ROOT + mPlayer.getNhl_id() + "@2x.jpg")
                //.placeholder(R.drawable.img_player_backdrop_placeholder)
                .centerCrop()
                .into(player_backdrop);

        Glide.with(this)
                .load(MUGSHOT_ROOT + mPlayer.getNhl_id() + "@2x.jpg")
                .transform(new CircleTransform(this))
                .into(player_mugshot);

        Typeface TypefaceArvoBold = Typeface.createFromAsset(getAssets(), TYPEFACE_ARVO_BOLD);

        player_name.setText(mPlayer.getName());
        player_name.setTypeface(TypefaceArvoBold);

        String status = mPlayer.getStatus();

        if(status != null) {

            player_status.setText(status);
            player_status.setVisibility(View.VISIBLE);
        }

        player_age.setText("Age: " + mPlayer.getAge() + " (" + mPlayer.getDobString() + ")");

        if(mPlayer.getIs_drafted()) {

            draft_year.setText("Drafted " + mPlayer.getDraft_year() + " by " + mPlayer.getDraft_team());

            draft_position.setText(Util.formatNumSuffix((int) mPlayer.getDraft_round()) + " Round "
                    + Util.formatNumSuffix((int) mPlayer.getDraft_position()) + " Overall");

            draft_position.setVisibility(View.VISIBLE);

        } else {

            draft_year.setText("Undrafted");
        }

        String pos = mPlayer.getPosition();
        StringBuilder position = new StringBuilder();

        switch(pos) {

            case "G": {

                position.append("GOALTENDER | Catches ");

                break;
            }

            case "C": { position.append("CENTER | Shoots "); break; }
            case "LW": { position.append("LEFT WING | Shoots "); break; }
            case "RW": { position.append("RIGHT WING | Shoots "); break; }
            case "D": { position.append("DEFENSE | Shoots "); break; }
        }

        position.append(mPlayer.getHand().equals("L") ? "LEFT" : "RIGHT");

        player_position.setText(position.toString());
    }
}
