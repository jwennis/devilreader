package com.example.dreader.devilreader;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.example.dreader.devilreader.model.Player;
import com.example.dreader.devilreader.ui.CircleTransform;


public class PlayerActivity extends AppCompatActivity {

    private Player mPlayer;

    @BindString(R.string.player_backdrop_root)
    String BACKDROP_ROOT;

    @BindString(R.string.player_mugshot_root)
    String MUGSHOT_ROOT;

    @BindString(R.string.typeface_arvo_normal)
    String TYPEFACE_ARVO_NORMAL;

    @BindString(R.string.typeface_arvo_bold)
    String TYPEFACE_ARVO_BOLD;

    @BindString(R.string.player_status_captain)
    String CAPTAIN;

    @BindString(R.string.player_status_assistant)
    String ASSISTANT;

    @BindString(R.string.player_label_age)
    String LABEL_AGE;

    @BindString(R.string.player_label_drafted)
    String LABEL_DRAFTED;

    @BindString(R.string.player_label_draft_round)
    String LABEL_DRAFT_ROUND;

    @BindString(R.string.player_label_draft_pos)
    String LABEL_DRAFT_POS;

    @BindString(R.string.player_undrafted)
    String UNDRAFTED;

    @BindString(R.string.player_pos_goaltender)
    String POSITION_GOALTENDER;

    @BindString(R.string.player_pos_center)
    String POSITION_CENTER;

    @BindString(R.string.player_pos_left)
    String POSITION_LEFT;

    @BindString(R.string.player_pos_right)
    String POSITION_RIGHT;

    @BindString(R.string.player_pos_defense)
    String POSITION_DEFENSE;

    @BindString(R.string.player_hand_right)
    String HAND_RIGHT;

    @BindString(R.string.player_hand_left)
    String HAND_LEFT;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.player_detail_backdrop)
    ImageView player_backdrop;

    @BindView(R.id.player_detail_mugshot)
    ImageView player_mugshot;

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

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.viewpager)
    ViewPager viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null) {

            mPlayer = savedInstanceState.getParcelable(Player.PARAM_PLAYER_PARCEL);

        } else {

            mPlayer = getIntent().getExtras().getParcelable(Player.PARAM_PLAYER_PARCEL);
        }

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

        getSupportActionBar().setTitle(mPlayer.getName());

        Typeface TypefaceArvoNormal = Typeface.createFromAsset(getAssets(), TYPEFACE_ARVO_NORMAL);

        collapsing_toolbar.setCollapsedTitleTypeface(TypefaceArvoNormal);
        collapsing_toolbar.setExpandedTitleTypeface(TypefaceArvoNormal);

        Glide.with(this)
                .load(BACKDROP_ROOT + mPlayer.getNhl_id() + Util.PLAYER_IMG_FILE_EXT)
                //.placeholder(R.drawable.img_player_backdrop_placeholder)
                .centerCrop()
                .into(player_backdrop);

        Glide.with(this)
                .load(MUGSHOT_ROOT + mPlayer.getNhl_id() + Util.PLAYER_IMG_FILE_EXT)
                .transform(new CircleTransform(this))
                .into(player_mugshot);

        player_mugshot.setContentDescription(mPlayer.getName());

        String status = mPlayer.getStatus();

        if(status != null) {

            if(mPlayer.getIs_captain()) {

                player_status.setText(CAPTAIN);

            } else if (mPlayer.getIs_captain()) {

                player_status.setText(ASSISTANT);
            }

            player_status.setVisibility(View.VISIBLE);
        }

        player_age.setText(String.format("%s: %s (%s)", LABEL_AGE, mPlayer.getAge(), mPlayer.getDobString()));

        if(mPlayer.getIs_drafted()) {

            draft_year.setText(String.format("%s %d by %s", LABEL_DRAFTED, mPlayer.getDraft_year(), mPlayer.getDraft_team()));

            draft_position.setText(String.format("%s %s | %s %s",
                    Util.formatNumSuffix((int) mPlayer.getDraft_round()), LABEL_DRAFT_ROUND,
                    Util.formatNumSuffix((int) mPlayer.getDraft_position()), LABEL_DRAFT_POS));

            draft_position.setVisibility(View.VISIBLE);

        } else {

            draft_year.setText(UNDRAFTED);
        }

        String pos = mPlayer.getPosition();
        StringBuilder position = new StringBuilder();

        switch(pos) {

            case "G": {

                position.append(POSITION_GOALTENDER);

                break;
            }

            case "C": { position.append(POSITION_CENTER); break; }
            case "LW": { position.append(POSITION_LEFT); break; }
            case "RW": { position.append(POSITION_RIGHT); break; }
            case "D": { position.append(POSITION_DEFENSE); break; }
        }

        position.append(" " + (mPlayer.getHand().equals("L") ? HAND_LEFT : HAND_RIGHT));

        player_position.setText(position.toString());

        initViewPager();
    }


    private void initViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle args = new Bundle();
        args.putParcelable(Player.PARAM_PLAYER_PARCEL, mPlayer);

        PlayerContractsFragment contractsFragment = new PlayerContractsFragment();
        contractsFragment.setArguments(args);
        adapter.addFragment(contractsFragment, "CONTRACTS");

        PlayerNewsFragment newsFragment = new PlayerNewsFragment();
        newsFragment.setArguments(args);
        adapter.addFragment(newsFragment, "NEWS");

        PlayerGoalsFragment goalsFragment = new PlayerGoalsFragment();
        goalsFragment.setArguments(args);
        adapter.addFragment(goalsFragment, "GOALS");

        viewpager.setAdapter(adapter);
        tab_layout.setupWithViewPager(viewpager);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {

            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragments.get(position);
        }

        @Override
        public int getCount() {

            return mFragments.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitles.get(position);
        }
    }
}
