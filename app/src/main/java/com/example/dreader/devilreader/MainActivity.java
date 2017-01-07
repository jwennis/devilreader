package com.example.dreader.devilreader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;

import com.example.dreader.devilreader.data.StoryContract.StoryEntry;
import com.example.dreader.devilreader.firebase.FirebaseCallback;
import com.example.dreader.devilreader.firebase.FirebaseUtil;
import com.example.dreader.devilreader.model.Story;
import com.example.dreader.devilreader.ui.CircleTransform;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener,
        FirebaseAuth.AuthStateListener {

    private static final String PARAM_GOOGLE_USER_ACCOUNT = "PARAM_GOOGLE_USER_ACCOUNT";
    private static final String PARAM_FIREBASE_AUTH_UID = "PARAM_FIREBASE_AUTH_UID";

    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private GoogleSignInAccount mAccount;
    private String mFirebaseAuthUid;

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

        GoogleSignInOptions options =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(BuildConfig.OAUTH2_CLIENT_ID)
                        .requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options).build();

        if(savedInstanceState != null) {

            mAccount = savedInstanceState.getParcelable(PARAM_GOOGLE_USER_ACCOUNT);
            mFirebaseAuthUid = savedInstanceState.getString(PARAM_FIREBASE_AUTH_UID);

            if(mAccount != null) {

                updateDrawerHeader();
            }

        } else {

            initSignIn();

            swapFragment(Util.getPreferredStartScreen(this) == DiscoverFragment.class
                    ? new DiscoverFragment() : new NewsFragment(), false);
        }
    }


    @Override
    public void onStart() {

        super.onStart();

        FirebaseUtil.addAuthListener(this);
    }


    @Override
    public void onStop() {

        super.onStop();

        FirebaseUtil.removeAuthListener(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(PARAM_GOOGLE_USER_ACCOUNT, mAccount);
        outState.putString(PARAM_FIREBASE_AUTH_UID, mFirebaseAuthUid);

        super.onSaveInstanceState(outState);
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

            case R.id.drawer_signin: {

                if(mAccount == null) { signIn(); } else { signOut(); }

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


    private void updateDrawerHeader() {

        Menu nav_menu = nav.getMenu();
        MenuItem signin = nav_menu.findItem(R.id.drawer_signin);

        View nav_header = nav.getHeaderView(0);
        ImageView auth_icon = (ImageView) nav_header.findViewById(R.id.auth_icon);
        TextView auth_name = (TextView) nav_header.findViewById(R.id.auth_name);
        TextView auth_email = (TextView) nav_header.findViewById(R.id.auth_email);

        if(mAccount != null) {

            Glide.with(MainActivity.this)
                    .load(mAccount.getPhotoUrl())
                    .transform(new CircleTransform(MainActivity.this))
                    .into(auth_icon);

            auth_name.setText(mAccount.getDisplayName());
            auth_email.setText(mAccount.getEmail());

            signin.setTitle(R.string.auth_sign_out);

        } else {

            auth_icon.setImageResource(R.mipmap.ic_launcher);
            auth_name.setText(R.string.app_name);
            auth_email.setText(R.string.auth_not_signed_in);

            signin.setTitle(R.string.auth_sign_in);
        }
    }


    private void swapFragment(Fragment fragment, boolean addToBackstack) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);

        if(addToBackstack) {

            transaction.addToBackStack(null);
        }

        transaction.commit();
    }


    /**
     * Creates a snackbar message     *
     *
     * @param message the text to show in the snackbar
     */
    public void showSnackbar(String message) {

        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    /**
     * Creates a snackbar message     *
     *
     * @param stringResId the string resource ID containing the message to show
     */
    public void showSnackbar(int stringResId) {

        showSnackbar(getString(stringResId));
    }


    /**
     * Initializes the sign in process upon Activity creation
     */
    private void initSignIn() {

        OptionalPendingResult<GoogleSignInResult> pendingResult =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (pendingResult.isDone()) {

            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.

            handleSignInResult(pendingResult.get());

        } else {

            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

            showProgressDialog();

            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {

                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {

                    hideProgressDialog();

                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    /**
     * Initializes the sign in process upon NavigationDrawer selection
     */
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        showProgressDialog();
    }


    /**
     * Signs out the user upon NavigationDrawer selection
     */
    private void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(@NonNull Status status) {

                        mAccount = null;

                        showSnackbar(R.string.auth_signed_out);

                        updateDrawerHeader();
                    }
                });

        FirebaseUtil.unauthenticate();
    }


    /**
     * Displays a loading indicator while signing in
     */
    private void showProgressDialog() {

        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.auth_loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    /**
     * Removes the loading indicator
     */
    private void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {

            mProgressDialog.hide();
        }
    }


    /**
     * Handles the result of a sign in attempt
     *
     * @param result the result of the sign in process
     */
    private void handleSignInResult(GoogleSignInResult result) {

        hideProgressDialog();

        if (result.isSuccess()) {

            mAccount = result.getSignInAccount();
            FirebaseUtil.authenticate(mAccount.getIdToken());

            showSnackbar(getString(R.string.auth_signed_in_fmt, mAccount.getDisplayName()));

        } else {

            mAccount = null;
            FirebaseUtil.unauthenticate();
        }

        updateDrawerHeader();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);
        }
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {

            mFirebaseAuthUid = user.getUid();

            syncUserData();

        } else {

            mFirebaseAuthUid = null;
        }
    }


    private void syncUserData() {

        final Cursor data = getContentResolver().query(
                StoryEntry.CONTENT_URI, null, null, null, null);

        FirebaseUtil.queryUserData(mFirebaseAuthUid, new FirebaseCallback() {

            @Override
            public void onUserDataResult(List<String> read, HashMap<String, Long> saved) {

                List<String> firebaseReadQueue = new ArrayList<>();
                List<String> deviceReadQueue = new ArrayList<>();

                HashMap<String, Long> firebaseSaveQueue = new HashMap<>();
                HashMap<String, Long> deviceSaveQueue = new HashMap<>();

                while(data.moveToNext()) {

                    Story story = new Story(data);

                    String id = story.getId();

                    if(story.isRead() && !read.contains(id)) {

                        firebaseReadQueue.add(id);

                    } else if(!story.isRead() && read.contains(id)) {

                        deviceReadQueue.add(id);
                    }

                    long deviceTimestamp = story.getSavedTimestamp();

                    if(story.isSaved()) {

                        if(!saved.containsKey(id)) {

                            firebaseSaveQueue.put(id, deviceTimestamp);

                        } else if(saved.get(id) < 0) {

                            long firebaseTimestmap = Math.abs(saved.get(id));

                            if(deviceTimestamp > firebaseTimestmap) {

                                firebaseSaveQueue.put(id, deviceTimestamp);

                            } else {

                                deviceSaveQueue.put(id, -firebaseTimestmap);
                            }
                        }

                    } else if(!story.isSaved() && saved.containsKey(id) && saved.get(id) > 0) {

                        long firebaseTimestmap = saved.get(id);

                        if(deviceTimestamp > firebaseTimestmap) {

                            firebaseSaveQueue.put(id, -deviceTimestamp);

                        } else {

                            deviceSaveQueue.put(id, firebaseTimestmap);
                        }
                    }
                }

                syncReadStories(firebaseReadQueue, deviceReadQueue);
                syncSavedStories(firebaseSaveQueue, deviceSaveQueue);
            }
        });
    }


    private void syncReadStories(List<String> firebaseQueue, List<String> deviceQueue) {

        for(String storyId : firebaseQueue) {

            FirebaseUtil.markStoryAsRead(mFirebaseAuthUid, storyId);
        }

        if(deviceQueue.size() > 0) {

            StringBuilder where = new StringBuilder();
            where.append(StoryEntry.COL_ID + " IN(");

            int numSelected = 0;

            for(int i = 0; i < deviceQueue.size(); i++) {

                where.append(numSelected++ == 0 ? "?" : ",?");
            }

            where.append(")");

            ContentValues values = new ContentValues();
            values.put(StoryEntry.COL_IS_READ, 1);

            getContentResolver().update(StoryEntry.CONTENT_URI,
                    values, where.toString(), deviceQueue.toArray(new String[ deviceQueue.size() ]));
        }
    }


    private void syncSavedStories(HashMap<String, Long> firebaseQueue,
                                 HashMap<String, Long> deviceQueue) {

        for (Map.Entry<String, Long> entry : firebaseQueue.entrySet()) {

            FirebaseUtil.updateStorySavedStatus(mFirebaseAuthUid, entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Long> entry : deviceQueue.entrySet()) {

            ContentValues values = new ContentValues();
            values.put(StoryEntry.COL_IS_SAVED, entry.getValue());

            getContentResolver().update(StoryEntry.CONTENT_URI,
                    values, StoryEntry.COL_ID + " = ?", new String[] { entry.getKey() });
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        showSnackbar(getString(R.string.auth_failed));
    }
}
