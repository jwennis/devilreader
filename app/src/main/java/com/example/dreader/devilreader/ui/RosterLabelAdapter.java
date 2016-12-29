package com.example.dreader.devilreader.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.model.Player;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RosterLabelAdapter extends RecyclerView.Adapter<RosterLabelAdapter.LabelViewHolder> {

    private static Typeface TypefaceArvoNormal;

    private List<Player> mItems;

    public RosterLabelAdapter(List<Player> items) {

        super();

        mItems = items;
    }


    @Override
    public RosterLabelAdapter.LabelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new LabelViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_roster_label, parent, false));
    }


    @Override
    public void onBindViewHolder(RosterLabelAdapter.LabelViewHolder holder, int position) {

        final Context context = holder.layout_root.getContext();
        final Player item = mItems.get(position);

        Glide.with(context)
                .load(holder.MUGSHOT_ROOT + item.getNhl_id() + "@2x.jpg")
                .transform(new CircleTransform(context))
                .into(holder.mugshot);

        holder.player_name.setText(item.getName());
        holder.player_name.setTypeface(TypefaceArvoNormal);

        holder.layout_root.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                Intent playerIntent = new Intent(context, PlayerActivity.class);
//                playerIntent.putExtra(Player.PARAM_PLAYER_PARCEL, item);
//
//                context.startActivity(playerIntent);
            }
        });
    }


    @Override
    public int getItemCount() {

        return mItems.size();
    }


    protected class LabelViewHolder extends RecyclerView.ViewHolder {

        ViewGroup layout_root;

        @BindString(R.string.typeface_arvo_normal)
        String TYPEFACE_ARVO_NORMAL;

        @BindView(R.id.roster_label_mugshot)
        ImageView mugshot;

        @BindView(R.id.roster_label_player_name)
        TextView player_name;

        @BindString(R.string.player_mugshot_root)
        String MUGSHOT_ROOT;


        public LabelViewHolder(View itemView) {

            super(itemView);

            layout_root = (ViewGroup) itemView;

            ButterKnife.bind(this, layout_root);

            if(TypefaceArvoNormal == null) {

                AssetManager assets = layout_root.getContext().getAssets();

                TypefaceArvoNormal = Typeface.createFromAsset(assets, TYPEFACE_ARVO_NORMAL);
            }
        }
    }
}

