package com.example.dreader.devilreader.firebase;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.example.dreader.devilreader.model.Player;
import com.example.dreader.devilreader.model.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FirebaseUtil {

    public static final String KEY = "KEY";
    public static final String ORDER_BY = "ORDER_BY";
    public static final String TEAM = "TEAM";
    public static final String TAG_STORY = "TAG_STORY";
    public static final String TAG_PLAYER = "TAG_PLAYER";

    private enum QueryType { ITEM, LIST }


    public static void queryStory(String param, String paramValue, FirebaseCallback callback) {

        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference("Story");
        Query query;

        QueryType queryType = QueryType.LIST;

        switch(param) {

            case ORDER_BY: {

                query = ref.orderByChild(paramValue).limitToLast(30);
                queryType = QueryType.LIST;

                break;
            }

            case KEY: {

                query = ref.child(paramValue);
                queryType = QueryType.ITEM;

                break;
            }

            default: {

                query = null;
            }
        }

        if(query != null) {

            queryStory(query, queryType, callback);
        }
    }


    private static void queryStory(Query query, final QueryType type,
                                   final FirebaseCallback callback) {

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot data) {

                switch(type) {

                    case LIST: {

                        List<Story> list = new ArrayList<>();

                        for(DataSnapshot child : data.getChildren()) {

                            list.add(child.getValue(Story.class));
                        }

                        callback.onStoryResult(list);

                        break;
                    }

                    case ITEM: {

                        Story item = data.getValue(Story.class);

                        callback.onStoryResult(item);

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // TODO: log error + handle gracefully
            }
        });
    }


    public static void queryPlayer(String param, String paramValue, FirebaseCallback callback) {

        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference("Player");

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
            }
        }
    }


    private static void queryPlayer(Query query, final QueryType type,
                                    final FirebaseCallback callback) {

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot data) {

                switch(type) {

                    case LIST: {

                        List<Player> list = new ArrayList<>();

                        for(DataSnapshot child : data.getChildren()) {

                            list.add(child.getValue(Player.class));
                        }

                        callback.onPlayerResult(list);

                        break;
                    }

                    case ITEM: {

                        Player item = data.getValue(Player.class);

                        callback.onPlayerResult(item);

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // TODO: log error + handle gracefully
            }
        });
    }


    public static void queryTag(final String param, String paramValue,
                                final DatabaseReference ref, final FirebaseCallback callback) {

        DatabaseReference tagRef = FirebaseDatabase.getInstance().getReference("Tags");
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

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot data) {

                switch(param) {

                    case TAG_STORY: {

                        final List<Long> keys = new ArrayList<>();

                        for(DataSnapshot tag : data.getChildren()) {

                            keys.add(Long.parseLong((String) tag.child("player_id").getValue()));
                        }

                        queryPlayer(ref, QueryType.LIST, new FirebaseCallback() {

                            @Override
                            public void onPlayerResult(List<Player> list) {

                                Iterator<Player> iterator = list.iterator();

                                while (iterator.hasNext()) {

                                    Player player = iterator.next();

                                    if(!keys.contains(player.getNhl_id())) {

                                        iterator.remove();
                                    }
                                }

                                callback.onPlayerResult(list);
                            }
                        });

                        break;
                    }

                    case TAG_PLAYER: {

                        queryStory(ref, QueryType.LIST, new FirebaseCallback() {

                            @Override
                            public void onStoryResult(List<Story> list) {


                            }
                        });

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // TODO: log error + handle gracefully
            }
        });
    }
}
