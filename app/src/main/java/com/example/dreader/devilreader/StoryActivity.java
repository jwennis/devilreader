package com.example.dreader.devilreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dreader.devilreader.model.Story;

public class StoryActivity extends AppCompatActivity {

    private Story mStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_story);

        mStory = getIntent().getExtras().getParcelable(Story.PARAM_STORY_PARCEL);
    }
}
