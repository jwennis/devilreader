package com.example.dreader.devilreader.firebase;

import java.util.List;
import java.util.ArrayList;

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

//        if(query != null) {
//
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                @Override
//                public void onDataChange(DataSnapshot data) {
//
//                        List<Story> list = new ArrayList<>();
//
//                        for(DataSnapshot child : data.getChildren()) {
//
//                            list.add(child.getValue(Story.class));
//                        }
//
//                        callback.onResult(list);
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                    // TODO: log error + handle gracefully
//                }
//            });
//        }
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

                        callback.onResult(list);

                        break;
                    }

                    case ITEM: {

                        Story item = data.getValue(Story.class);

                        callback.onResult(item);

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

    private enum QueryType { ITEM, LIST }
}
