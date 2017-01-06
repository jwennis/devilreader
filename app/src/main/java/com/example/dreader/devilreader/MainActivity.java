package com.example.dreader.devilreader;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.reflect.Field;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @BindString(R.string.typeface_arvo_bold)
    String TYPEFACE_ARVO_BOLD;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(this);

        Typeface TypefaceArvoBold = Typeface.createFromAsset(getAssets(), TYPEFACE_ARVO_BOLD);

        try { // Set the toolbar font

            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);

            TextView title = (TextView) f.get(toolbar);

            if(title != null) {

                title.setTypeface(TypefaceArvoBold);
                title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            }

        } catch (NoSuchFieldException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }

        if(savedInstanceState == null) {

            swapFragment(Util.getPreferredStartScreen(this) == DiscoverFragment.class
                    ? new DiscoverFragment() : new NewsFragment(), false);
        }
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {

            case R.id.drawer_discover: {

                swapFragment(new DiscoverFragment(), true);

                break;
            }

            case R.id.drawer_news: {

                swapFragment(new NewsFragment(), true);

                break;
            }

            case R.id.drawer_roster: {

                swapFragment(new RosterFragment(), true);

                break;
            }

            case R.id.drawer_schedule: {

                swapFragment(new ScheduleFragment(), true);

                break;
            }

            case R.id.drawer_settings: {

                startActivity(new Intent(this, SettingsActivity.class));

                break;
            }
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    private void swapFragment(Fragment fragment, boolean addToBackstack) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);

        if(addToBackstack) {

            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
