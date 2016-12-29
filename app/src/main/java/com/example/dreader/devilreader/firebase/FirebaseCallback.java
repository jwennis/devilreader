package com.example.dreader.devilreader.firebase;

import java.util.List;

import com.example.dreader.devilreader.model.Player;
import com.example.dreader.devilreader.model.PlayerContract;
import com.example.dreader.devilreader.model.Story;

public class FirebaseCallback {

    public void onStoryResult(List<Story> list) { }
    public void onStoryResult(Story item) { }

    public void onPlayerResult(List<Player> list) { }
    public void onPlayerResult(Player item) { }

    public void onContractResult(List<PlayerContract> list) { }
}
