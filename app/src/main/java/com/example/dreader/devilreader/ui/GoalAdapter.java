package com.example.dreader.devilreader.ui;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.model.Goal;

import java.util.List;

import butterknife.ButterKnife;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private List<Goal> mItems;
    private String mCaller;


    public GoalAdapter(List<Goal> list, String caller) {

        super();

        mItems = list;
        mCaller = caller;
    }


    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new GoalViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_goal, parent, false));
    }


    @Override
    public void onBindViewHolder(final GoalViewHolder holder, int position) {

        final Goal item = getItemAt(position);



    }


    @Override
    public int getItemCount() {

        return mItems != null ? mItems.size() : 0;
    }


    private Goal getItemAt(int pos) {

        return pos < getItemCount() ? mItems.get(pos) : null;
    }


    protected class GoalViewHolder extends RecyclerView.ViewHolder {

        public ViewGroup layout_root;


        public GoalViewHolder(View itemView) {

            super(itemView);

            layout_root = (ViewGroup) itemView;

            ButterKnife.bind(this, layout_root);
        }
    }
}
