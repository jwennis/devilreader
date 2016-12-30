package com.example.dreader.devilreader.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dreader.devilreader.PlayerActivity;
import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.model.Player;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerMugshotAdapter extends RecyclerView.Adapter<PlayerMugshotAdapter.LabelViewHolder> {

    private List<Player> mItems;


    public PlayerMugshotAdapter(List<Player> items) {

        super();

        mItems = items;
    }


    @Override
    public PlayerMugshotAdapter.LabelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new LabelViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_player_mugshot, parent, false));
    }


    @Override
    public void onBindViewHolder(PlayerMugshotAdapter.LabelViewHolder holder, int position) {

        final Context context = holder.layout_root.getContext();
        final Player item = mItems.get(position);

        Glide.with(context)
                .load(holder.MUGSHOT_ROOT + item.getNhl_id() + "@2x.jpg")
                .transform(new CircleTransform(context))
                .into(holder.mugshot);

        holder.layout_root.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent playerIntent = new Intent(context, PlayerActivity.class);
                playerIntent.putExtra(Player.PARAM_PLAYER_PARCEL, item);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    View mugshot = view.findViewById(R.id.roster_label_mugshot);

                    Bundle bundle = ActivityOptions
                            .makeSceneTransitionAnimation((Activity) context, mugshot, mugshot.getTransitionName())
                            .toBundle();

                    context.startActivity(playerIntent, bundle);

                } else {

                    context.startActivity(playerIntent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {

        return mItems.size();
    }


    protected class LabelViewHolder extends RecyclerView.ViewHolder {

        ViewGroup layout_root;

        @BindView(R.id.roster_label_mugshot)
        ImageView mugshot;

        @BindString(R.string.player_mugshot_root)
        String MUGSHOT_ROOT;


        public LabelViewHolder(View itemView) {

            super(itemView);

            layout_root = (ViewGroup) itemView;

            ButterKnife.bind(this, layout_root);
        }
    }
}

