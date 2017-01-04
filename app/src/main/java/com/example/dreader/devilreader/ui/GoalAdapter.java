package com.example.dreader.devilreader.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dreader.devilreader.GameActivity;
import com.example.dreader.devilreader.PlayerGoalsFragment;
import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.model.Goal;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
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
        final Context context = holder.layout_root.getContext();

        Glide.with(context)
                .load(holder.MUGSHOT_ROOT + item.getPlayer_id() + "@2x.jpg")
                .transform(new CircleTransform(context))
                .into(holder.mugshot);

        if(mCaller.equals(GameActivity.class.getSimpleName())) {

            holder.title.setText(String.format("%s (%d)", item.getName(), item.getCount()));

        } else if (mCaller.equals(PlayerGoalsFragment.class.getSimpleName())) {

            holder.title.setText(item.getTitle());
        }

        holder.assists.setText(item.getAssists());
        holder.time.setText(item.getTime());

        if(item.hasVideo()){

            holder.video.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    String videoUrl = item.getVideo();

                    Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                    videoIntent.setDataAndType(Uri.parse(videoUrl),"video/mp4");

                    ((Activity) context).startActivityForResult(videoIntent, 0);
                }
            });
        }

        holder.divider.setVisibility(position == getItemCount() - 1
                ? View.GONE : View.VISIBLE);
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

        @BindString(R.string.player_mugshot_root)
        String MUGSHOT_ROOT;

        @BindView(R.id.goal_mugshot)
        ImageView mugshot;

        @BindView(R.id.goal_title)
        TextView title;

        @BindView(R.id.goal_assists)
        TextView assists;

        @BindView(R.id.goal_time)
        TextView time;

        @BindView(R.id.goal_video)
        ImageView video;

        @BindView(R.id.goal_divider)
        View divider;


        public GoalViewHolder(View itemView) {

            super(itemView);

            layout_root = (ViewGroup) itemView;

            ButterKnife.bind(this, layout_root);
        }
    }
}
