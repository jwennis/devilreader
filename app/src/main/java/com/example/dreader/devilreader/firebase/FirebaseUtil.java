package com.example.dreader.devilreader.firebase;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.example.dreader.devilreader.BuildConfig;
import com.example.dreader.devilreader.model.Game;
import com.example.dreader.devilreader.model.Goal;
import com.example.dreader.devilreader.model.Player;
import com.example.dreader.devilreader.model.PlayerContract;
import com.example.dreader.devilreader.model.Story;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;


public class FirebaseUtil {

    public static final String KEY = "KEY";
    public static final String ORDER_BY = "ORDER_BY";
    public static final String TEAM = "TEAM";
    public static final String TAG_STORY = "TAG_STORY";
    public static final String TAG_PLAYER = "TAG_PLAYER";
    public static final String TAG_GAME = "TAG_GAME";

    private static FirebaseDatabase mFirebase;

    private enum QueryType { ITEM, LIST }


    private static FirebaseDatabase getFirebaseInstance() {

        if(mFirebase == null) {

            mFirebase = FirebaseDatabase.getInstance();
            mFirebase.setPersistenceEnabled(true);
        }

        return mFirebase;
    }


    private static DatabaseReference getReference(String key) {

        DatabaseReference ref = getFirebaseInstance().getReference(key);
        ref.keepSynced(true);

        return ref;
    }


    public static void queryStory(String param, String paramValue, FirebaseCallback callback) {

        DatabaseReference ref =  getReference("Story");

        switch(param) {

            case ORDER_BY: {

                Query query = ref.orderByChild(paramValue).limitToLast(50);

                queryStory(query, QueryType.LIST, callback);

                break;
            }

            case KEY: {

                queryStory(ref.child(paramValue), QueryType.ITEM, callback);

                break;
            }

            case TAG_PLAYER: {

                queryTag(param, paramValue, ref, callback);

                break;
            }
        }
    }


    private static void queryStory(Query query, final QueryType type,
                                   final FirebaseCallback callback) {

        query.addListenerForSingleValueEvent(new FirebaseListener() {

            private List<Story> list;
            private Story item;

            @Override
            protected void onDataResult(DataSnapshot data) {

                switch(type) {

                    case LIST: {

                        list = new ArrayList<>();

                        for(DataSnapshot child : data.getChildren()) {

                            list.add(child.getValue(Story.class));
                        }

                        break;
                    }

                    case ITEM: {

                        item = data.getValue(Story.class);

                        break;
                    }
                }

                publishProgress();
            }

            @Override
            protected void sendResult() {

                if(list != null) {

                    callback.onStoryResult(list);

                } else if (item != null) {

                    callback.onStoryResult(item);
                }
            }
        });
    }


    public static void queryPlayer(String param, String paramValue, FirebaseCallback callback) {

        DatabaseReference ref =  getReference("Player");

        switch(param) {

            case TEAM: {

                Query query = ref.orderByChild(TEAM.toLowerCase()).equalTo(paramValue);

                queryPlayer(query, QueryType.LIST, callback);

                break;
            }

            case KEY: {

                queryPlayer(ref.child(paramValue), QueryType.ITEM, callback);

                break;
            }

            case TAG_STORY: {

                queryTag(param, paramValue, ref, callback);

                break;
            }
        }
    }


    private static void queryPlayer(Query query, final QueryType type,
                                    final FirebaseCallback callback) {

        query.addListenerForSingleValueEvent(new FirebaseListener() {

            private List<Player> list;
            private Player item;

            @Override
            protected void onDataResult(DataSnapshot data) {

                switch(type) {

                    case LIST: {

                        list = new ArrayList<>();

                        for(DataSnapshot child : data.getChildren()) {

                            list.add(child.getValue(Player.class));
                        }

                        break;
                    }

                    case ITEM: {

                        item = data.getValue(Player.class);

                        break;
                    }
                }

                publishProgress();
            }

            @Override
            protected void sendResult() {

                if(list != null) {

                    callback.onPlayerResult(list);

                } else if (item != null) {

                    callback.onPlayerResult(item);
                }
            }
        });
    }


    public static void queryTag(final String param, String paramValue,
                                final DatabaseReference ref, final FirebaseCallback callback) {

        DatabaseReference tagRef = getReference("Tags");
        Query query = null;

        switch(param) {

            case TAG_STORY: {

                query = tagRef.orderByChild("story_id").equalTo(paramValue);

                break;
            }

            case TAG_PLAYER: {

                query = tagRef.orderByChild("player_id").equalTo(paramValue);

                break;
            }
        }

        query.addListenerForSingleValueEvent(new FirebaseListener() {

            private List<Player> players;
            private List<String> keys;

            @Override
            protected void onDataResult(DataSnapshot data) {

                switch(param) {

                    case TAG_STORY: {

                        keys = new ArrayList<>();

                        for(DataSnapshot tag : data.getChildren()) {

                            keys.add((String) tag.child("player_id").getValue());
                        }

                        queryPlayer(ref, QueryType.LIST, new FirebaseCallback() {

                            @Override
                            public void onPlayerResult(List<Player> list) {

                                Iterator<Player> iterator = list.iterator();

                                while (iterator.hasNext()) {

                                    Player player = iterator.next();

                                    if(!keys.contains(Long.toString(player.getNhl_id()))) {

                                        iterator.remove();
                                    }
                                }

                                players = list;

                                publishProgress();
                            }
                        });

                        break;
                    }

                    case TAG_PLAYER: {

                        keys = new ArrayList<>();

                        for(DataSnapshot tag : data.getChildren()) {

                            keys.add((String) tag.child("story_id").getValue());
                        }

                        publishProgress();

                        break;
                    }
                }
            }

            @Override
            protected void sendResult() {

                if(players != null) {

                    callback.onPlayerResult(players);

                } else {

                    callback.onTagResult(keys);
                }
            }
        });
    }


    public static void queryContract(long playerId, final FirebaseCallback callback) {

        DatabaseReference ref = getReference("Contract");
        Query query = ref.orderByChild("player_id").equalTo(playerId);

        query.addListenerForSingleValueEvent(new FirebaseListener() {

            private List<PlayerContract> list;

            @Override
            protected void onDataResult(DataSnapshot data) {

                list = new ArrayList<>();

                for(DataSnapshot child : data.getChildren()) {

                    list.add(new PlayerContract(child));
                }

                publishProgress();
            }

            @Override
            protected void sendResult() {

                callback.onContractResult(list);
            }
        });
    }


    public static void queryGame(String param, String paramValue, FirebaseCallback callback) {

        DatabaseReference ref = getReference("Game");

        switch(param) {

            case ORDER_BY: {

                Query query = ref.orderByChild(paramValue).limitToLast(82);

                queryGame(query, QueryType.LIST, callback);

                break;
            }

            case KEY: {

                queryGame(ref.child(paramValue), QueryType.ITEM, callback);

                break;
            }
        }
    }


    private static void queryGame(Query query, final QueryType type,
                                   final FirebaseCallback callback) {

        query.addListenerForSingleValueEvent(new FirebaseListener() {

            private List<Game> list;
            private Game item;

            @Override
            protected void onDataResult(DataSnapshot data) {

                switch(type) {

                    case LIST: {

                        list = new ArrayList<>();

                        for(DataSnapshot child : data.getChildren()) {

                            list.add(child.getValue(Game.class));
                        }

                        break;
                    }

                    case ITEM: {

                        item = data.getValue(Game.class);

                        break;
                    }
                }

                publishProgress();
            }

            @Override
            protected void sendResult() {

                if(list != null) {

                    callback.onGameResult(list);

                } else if (item != null) {

                    callback.onGameResult(item);
                }
            }
        });
    }


    public static void queryGoal(String param, long paramValue, final FirebaseCallback callback) {

        DatabaseReference goalRef =  getReference("Goal");
        Query query;

        switch(param) {

            case TAG_GAME: {

                query = goalRef.orderByChild("game_id").equalTo(paramValue);

                break;
            }

            case TAG_PLAYER: {

                query = goalRef.orderByChild("player_id").equalTo(paramValue);

                break;
            }

            default: {

                query = null;
            }
        }

        query.addListenerForSingleValueEvent(new FirebaseListener() {

            private List<Goal> list;

            @Override
            protected void onDataResult(DataSnapshot data) {

                list = new ArrayList<>();

                for(DataSnapshot child : data.getChildren()) {

                    list.add(child.getValue(Goal.class));
                }

                publishProgress();
            }

            @Override
            protected void sendResult() {

                callback.onGoalResult(list);
            }
        });
    }


    public static void queryStorage(String filename, final FirebaseCallback callback) {

        FirebaseStorage storage = FirebaseStorage.getInstance();

        storage.getReferenceFromUrl(BuildConfig.FIREBASE_STORAGE_BUCKET).child(filename)
                .getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

            @Override
            public void onSuccess(byte[] bytes) {

                callback.onByteArrayResult(bytes);
            }
        });
    }


    public static void addAuthListener(FirebaseAuth.AuthStateListener listener) {

        FirebaseAuth.getInstance().addAuthStateListener(listener);
    }


    public static void removeAuthListener(FirebaseAuth.AuthStateListener listener) {

        FirebaseAuth.getInstance().removeAuthStateListener(listener);
    }


    public static void authenticate(String token) {

        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
        FirebaseAuth.getInstance().signInWithCredential(credential);
    }


    public static void unauthenticate() {

        FirebaseAuth.getInstance().signOut();
    }
}
