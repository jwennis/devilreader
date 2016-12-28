package com.example.dreader.devilreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dreader.devilreader.model.Story;


public class StoryActivity extends AppCompatActivity {

    private Story mStory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_story);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null) {

            mStory = savedInstanceState.getParcelable(Story.PARAM_STORY_PARCEL);

        } else {

            mStory = getIntent().getExtras().getParcelable(Story.PARAM_STORY_PARCEL);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(Story.PARAM_STORY_PARCEL, mStory);

        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.story, menu);

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem toggleSave = menu.findItem(R.id.action_save);

        toggleSave.setTitle(mStory.isSaved() ?
                R.string.action_story_unsave : R.string.action_story_save);

        toggleSave.setIcon(mStory.isSaved() ?
                R.drawable.ic_menu_saved : R.drawable.ic_menu_save);

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case android.R.id.home: {

                onBackPressed();
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
