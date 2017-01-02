package com.example.dreader.devilreader;

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
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dreader.devilreader.model.Player;
import com.example.dreader.devilreader.ui.CircleTransform;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

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

        mPlayer.print();

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mPlayer.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface TypefaceArvoNormal = Typeface.createFromAsset(getAssets(), TYPEFACE_ARVO_NORMAL);

        collapsing_toolbar.setCollapsedTitleTypeface(TypefaceArvoNormal);
        collapsing_toolbar.setExpandedTitleTypeface(TypefaceArvoNormal);

        Glide.with(this)
                .load(BACKDROP_ROOT + mPlayer.getNhl_id() + "@2x.jpg")
                //.placeholder(R.drawable.img_player_backdrop_placeholder)
                .centerCrop()
                .into(player_backdrop);

        Glide.with(this)
                .load(MUGSHOT_ROOT + mPlayer.getNhl_id() + "@2x.jpg")
                .transform(new CircleTransform(this))
                .into(player_mugshot);

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
        goalsFragment.setArguments(args);//
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
