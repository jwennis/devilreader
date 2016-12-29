package com.example.dreader.devilreader;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.customtabs.CustomTabsService;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;

import com.example.dreader.devilreader.data.StoryContract.StoryEntry;
import com.example.dreader.devilreader.firebase.FirebaseCallback;
import com.example.dreader.devilreader.firebase.FirebaseUtil;
import com.example.dreader.devilreader.model.Story;


public class StoryActivity extends AppCompatActivity {

    private static String sChromeTabPackageName;

    private CustomTabsClient mChromeTabClient;
    private CustomTabsSession mChromeTabSession;
    private CustomTabsServiceConnection mTabServiceConnection;

    private Story mStory;

    @BindString(R.string.typeface_arvo_bold)
    String TYPEFACE_ARVO_BOLD;

    @BindView(R.id.story_title)
    TextView title;

    @BindView(R.id.story_byline)
    TextView byline;

    @BindView(R.id.story_attachment)
    ImageView attachment;

    @BindView(R.id.story_content)
    TextView content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_story);

        ButterKnife.bind(this);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        if(savedInstanceState != null) {

            mStory = savedInstanceState.getParcelable(Story.PARAM_STORY_PARCEL);

        } else {

            mStory = getIntent().getExtras().getParcelable(Story.PARAM_STORY_PARCEL);
        }

        bindStory();
    }


    @Override
    protected void onStart() {

        super.onStart();

        if (mChromeTabClient != null && mTabServiceConnection != null) { return; }

        if(sChromeTabPackageName == null) {

            PackageManager pm = getPackageManager();
            Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"));
            ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);

            String defaultViewHandlerPackageName = null;

            if (defaultViewHandlerInfo != null) {

                defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName;
            }

            List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);
            List<String> packagesSupportingCustomTabs = new ArrayList<>();

            for (ResolveInfo info : resolvedActivityList) {

                Intent serviceIntent = new Intent();

                serviceIntent.setAction(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
                serviceIntent.setPackage(info.activityInfo.packageName);

                if (pm.resolveService(serviceIntent, 0) != null) {

                    packagesSupportingCustomTabs.add(info.activityInfo.packageName);
                }
            }

            for (ResolveInfo info : resolvedActivityList) {

                Intent serviceIntent = new Intent();
                serviceIntent.setAction(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
                serviceIntent.setPackage(info.activityInfo.packageName);

                if (pm.resolveService(serviceIntent, 0) != null) {

                    packagesSupportingCustomTabs.add(info.activityInfo.packageName);
                }
            }

            boolean hasSpecializedHandlerIntents = false;

            try {

                List<ResolveInfo> handlers = pm.queryIntentActivities( activityIntent, PackageManager.GET_RESOLVED_FILTER);

                if (handlers == null || handlers.size() == 0) {

                    hasSpecializedHandlerIntents = false;
                }

                for (ResolveInfo resolveInfo : handlers) {

                    IntentFilter filter = resolveInfo.filter;
                    if (filter == null) continue;
                    if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue;
                    if (resolveInfo.activityInfo == null) continue;

                    hasSpecializedHandlerIntents = true;
                }

            } catch (RuntimeException e) {

            }

            final String STABLE_PACKAGE = "com.android.chrome";
            final String BETA_PACKAGE = "com.chrome.beta";
            final String DEV_PACKAGE = "com.chrome.dev";
            final String LOCAL_PACKAGE = "com.google.android.apps.chrome";

            if (packagesSupportingCustomTabs.isEmpty()) {

                sChromeTabPackageName = null;

            } else if (packagesSupportingCustomTabs.size() == 1) {

                sChromeTabPackageName = packagesSupportingCustomTabs.get(0);

            } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
                    && !hasSpecializedHandlerIntents
                    && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {

                sChromeTabPackageName = defaultViewHandlerPackageName;

            } else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {

                sChromeTabPackageName = STABLE_PACKAGE;

            } else if (packagesSupportingCustomTabs.contains(BETA_PACKAGE)) {

                sChromeTabPackageName = BETA_PACKAGE;

            } else if (packagesSupportingCustomTabs.contains(DEV_PACKAGE)) {

                sChromeTabPackageName = DEV_PACKAGE;

            } else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {

                sChromeTabPackageName = LOCAL_PACKAGE;
            }
        }

        if (sChromeTabPackageName == null) { return; }

        mTabServiceConnection = new CustomTabsServiceConnection() {

            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {

                mChromeTabClient = client;
                mChromeTabClient.warmup(0L);

                mChromeTabSession = mChromeTabClient.newSession(null);
                mChromeTabSession.mayLaunchUrl(Uri.parse(mStory.getLink()), null, null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        CustomTabsClient.bindCustomTabsService(this, sChromeTabPackageName, mTabServiceConnection);
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        unbindService(mTabServiceConnection);
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


    private void bindStory() {

        Typeface TypefaceArvoBold = Typeface.createFromAsset(getAssets(), TYPEFACE_ARVO_BOLD);

        title.setText(mStory.getTitle());
        title.setTypeface(TypefaceArvoBold);

        byline.setText(mStory.getLongByline());

        if(mStory.hasAttachment()) {

            Glide.with(this)
                    .load(mStory.getAttachment())
                    .fitCenter()
                    .into(attachment);
        } else {

            attachment.setVisibility(View.GONE);
        }

        if(mStory.hasMedia()) {

            // TODO: implement media player

            Log.v("DREADER", "Media: " + mStory.getMedia());
        }

        if(!mStory.hasContent()) {

            initContent();

        } else {

            bindContent();
        }

        if(!mStory.isRead()) {

            markAsRead();
        }
    }


    private void initContent() {

        FirebaseUtil.queryStory(FirebaseUtil.KEY, mStory.getId(), new FirebaseCallback() {

            @Override
            public void onResult(Story item) {

                mStory.setContent(item.getContent());

                bindContent();
            }
        });
    }


    private void bindContent() {

        StringBuilder builder = new StringBuilder();

        for(String paragraph : mStory.getContent()) {

            builder.append(paragraph + "\n\n");
        }

        content.setText(builder.toString());
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


    /**
     * Opens the Story URL in the users' preferred method - Chrome custom Tab or otherwise
     */
    private void openLinkExternal() {

        boolean isChromeTabPreferred = Util.getOpenExternalLinkMethod(this).equals("1");

        if (isChromeTabPreferred && sChromeTabPackageName != null) {

            CustomTabsIntent.Builder builder =
                    new CustomTabsIntent.Builder(mChromeTabSession);

            //builder.setToolbarColor(colorInt);
            builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            builder.setExitAnimations(this,
                    android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            CustomTabsIntent tabIntent = builder.build();

            tabIntent.intent.setPackage(sChromeTabPackageName);
            tabIntent.launchUrl(this, Uri.parse(mStory.getLink()));

        } else {

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mStory.getLink())));
        }
    }
}
