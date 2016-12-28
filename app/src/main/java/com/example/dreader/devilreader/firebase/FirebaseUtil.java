package com.example.dreader.devilreader.firebase;

import android.util.Log;

import com.example.dreader.devilreader.model.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtil {

    public static final String ORDER_BY = "ORDER_BY";

    public static void queryStory(String param, String paramValue,
                                  final FirebaseCallback callback) {

        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference("Story");
        Query query;

        switch(param) {

            case ORDER_BY: {

                query = ref.orderByChild(paramValue).limitToLast(30);

                break;
            }

            default: {

                query = null;
            }
        }

        if(query != null) {

            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot data) {

                    List<Story> list = new ArrayList<>();

                    for(DataSnapshot child : data.getChildren()) {

                        list.add(child.getValue(Story.class));
                    }

                    callback.onResult(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    // TODO: log error + handle gracefully
                }
            });
        }
    }
}
