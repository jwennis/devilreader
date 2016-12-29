package com.example.dreader.devilreader.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RosterLabelAdapter extends RecyclerView.Adapter<RosterLabelAdapter.LabelViewHolder> {

    public RosterLabelAdapter() {

        super();
    }

    @Override
    public RosterLabelAdapter.LabelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(RosterLabelAdapter.LabelViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return 0;
    }

    protected class LabelViewHolder extends RecyclerView.ViewHolder {

        public LabelViewHolder(View itemView) {

            super(itemView);
        }
    }
}

