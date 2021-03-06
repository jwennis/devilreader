package com.example.dreader.devilreader.firebase;

import java.util.HashMap;
import java.util.List;

import com.example.dreader.devilreader.model.Game;
import com.example.dreader.devilreader.model.Goal;
import com.example.dreader.devilreader.model.Player;
import com.example.dreader.devilreader.model.PlayerContract;
import com.example.dreader.devilreader.model.Story;
import com.google.firebase.database.DataSnapshot;

public class FirebaseCallback {

    public void onStoryResult(List<Story> list) { }
    public void onStoryResult(Story item) { }

    public void onPlayerResult(List<Player> list) { }
    public void onPlayerResult(Player item) { }

    public void onGameResult(List<Game> list) { }
    public void onGameResult(Game item) { }

    public void onTagResult(List<String> tags) { }

    public void onGoalResult(List<Goal> list) { }
    public void onContractResult(List<PlayerContract> list) { }

    public void onUserDataResult(List<String> read, HashMap<String, Long> saved) { }
    public void onByteArrayResult(byte[] bytes) { }
}
