package com.example.dreader.devilreader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dreader.devilreader.model.Story;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private static Typeface TypefaceArvoNormal;

    private Cursor mData;
    private String mCaller;


    public StoryAdapter(Cursor data, String caller) {

        super();

        mData = data;
        mCaller = caller;
    }


    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new StoryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_story, parent, false));
    }


    @Override
    public void onBindViewHolder(StoryViewHolder holder, int position) {

        final Context context = holder.layout_root.getContext();

        final Story item = getItemAt(position);

        holder.title.setText(item.getTitle());
        holder.title.setTypeface(TypefaceArvoNormal);

        if(mCaller.equals(NewsFragment.class.getSimpleName())) {
            // or PlayerNewsFragment

            ViewCompat.setElevation(holder.layout_root,
                    context.getResources().getDimension(R.dimen.default_elevation));

        } else if(position > 0) {

            holder.divider.setVisibility(View.VISIBLE);
            holder.title.setPadding(0, 8, 0, 0);
        }

        holder.byline.setText(item.getShortByline());
        holder.tagline.setText(item.getTagline());

        if(item.isRead()) {

            holder.title.setTextColor(Color.parseColor("#888888"));
            holder.byline.setTextColor(Color.parseColor("#aaaaaa"));
            holder.tagline.setTextColor(Color.parseColor("#aaaaaa"));

        } else {

            holder.title.setTextColor(Color.parseColor("#444444"));
            holder.byline.setTextColor(Color.parseColor("#555555"));
            holder.tagline.setTextColor(Color.parseColor("#555555"));
        }

        ViewGroup.LayoutParams layoutParams = holder.thumbnail.getLayoutParams();

        final float DENSITY = context.getResources().getDisplayMetrics().density;
        final int DEFAULT_WIDTH = (int) (96 * DENSITY);
        final int PLACEHOLDER_WIDTH = (int) (54 * DENSITY);

        String attachment = item.getAttachment();

        if(attachment == null || attachment.isEmpty()) {

            attachment = context.getString(R.string.story_thumbnail_placeholder);

            layoutParams.width = PLACEHOLDER_WIDTH;

        } else {

            layoutParams.width = DEFAULT_WIDTH;
        }

        holder.thumbnail.setLayoutParams(layoutParams);

        Glide.with(context)
                .load(attachment)
                .centerCrop()
                .into(holder.thumbnail);

        holder.save_icon.setImageResource(item.isSaved()
                ? R.drawable.ic_menu_saved_dark : R.drawable.ic_menu_save_dark);

        holder.layout_root.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // TODO: implement press for Story detail
            }
        });

        holder.layout_root.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                // TODO: implement long press to save

                return true;
            }
        });
    }


    @Override
    public int getItemCount() {

        return mData != null ? mData.getCount() : 0;
    }


    public Story getItemAt(int pos) {

        if(pos < getItemCount()) {

            mData.moveToPosition(pos);

            return new Story(mData);
        }

        return null;
    }


    public void swapCursor(Cursor data) {

        mData = data;

        notifyDataSetChanged();
    }


    protected class StoryViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup layout_root;

        @BindString(R.string.typeface_arvo_normal)
        String TYPEFACE_ARVO_NORMAL;

        @BindView(R.id.story_item_title)
        TextView title;

        @BindView(R.id.story_item_byline)
        TextView byline;

        @BindView(R.id.story_item_tagline)
        TextView tagline;

        @BindView(R.id.story_item_thumbnail)
        ImageView thumbnail;

        @BindView(R.id.story_item_save_icon)
        ImageView save_icon;

        @BindView(R.id.story_item_divider)
        View divider;

        public StoryViewHolder(View itemView) {

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
