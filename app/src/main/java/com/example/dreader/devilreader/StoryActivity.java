package com.example.dreader.devilreader;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dreader.devilreader.data.StoryContract.StoryEntry;
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

        if(!mStory.isRead()) {

            markAsRead();
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

            case android.R.id.home: { onBackPressed(); return true; }
            case R.id.action_browser: { openLinkExternal(); return true; }
            case R.id.action_save: { toggleSaved(); return true; }
            case R.id.action_share: { shareLink(); return true; }

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


    /**
     * Marks the Story as read, persists
     * the change in the database
     */
    private void markAsRead() {

        mStory.markAsRead();

        ContentValues values = new ContentValues();
        values.put(StoryEntry.COL_IS_READ, 1);

        getContentResolver().update(StoryEntry.buildUri(mStory.getKey()), values, null, null);
    }


    /**
     * Toggles the saved/unsaved status of the Story,
     * persists the change in the database
     */
    private void toggleSaved() {

        mStory.toggleIsSaved();

        ContentValues values = new ContentValues();
        values.put(StoryEntry.COL_IS_SAVED, mStory.isSaved()
                ? mStory.getSavedTimestamp() : - mStory.getSavedTimestamp());

        invalidateOptionsMenu();

        String message = getString(mStory.isSaved()
                ? R.string.story_saved : R.string.story_unsaved);

        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();

        getContentResolver().update(StoryEntry.buildUri(mStory.getKey()),
                values, null, null);
    }


    /**
     * Opens the Share menu, passing the external link (URL)
     * of the Story
     */
    private void shareLink() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mStory.getLink());
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }


    private void openLinkExternal() {

        // TODO: implement this
    }

}
