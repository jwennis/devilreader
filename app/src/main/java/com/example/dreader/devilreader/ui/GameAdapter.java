package com.example.dreader.devilreader.ui;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreader.devilreader.model.Game;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> mItems;

    public GameAdapter(List<Game> list) {

        super();

        mItems = list;
    }

    @Override
    public GameAdapter.GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }


    @Override
    public void onBindViewHolder(GameAdapter.GameViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {

        return mItems != null ? mItems.size() : 0;
    }


    private Game getItemAt(int pos) {

        return pos < getItemCount() ? mItems.get(pos) : null;
    }


    protected class GameViewHolder extends RecyclerView.ViewHolder {


        public GameViewHolder(View itemView) {

            super(itemView);
        }
    }
}
