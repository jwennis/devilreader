package com.example.dreader.devilreader.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;

import com.example.dreader.devilreader.NewsFragment;
import com.example.dreader.devilreader.PlayerNewsFragment;
import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.StoryActivity;
import com.example.dreader.devilreader.data.StoryContract.StoryEntry;
import com.example.dreader.devilreader.model.Story;

import java.util.List;


public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private static Typeface TypefaceArvoNormal;

    private List<Story> mItems;
    private Cursor mData;
    private String mCaller;


    public StoryAdapter(Cursor data, String caller) {

        super();

        mData = data;
        mCaller = caller;
    }


    public StoryAdapter(List<Story> list, String caller) {

        super();

        mItems = list;
        mCaller = caller;
    }


    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new StoryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_story, parent, false));
    }


    @Override
    public void onBindViewHolder(final StoryViewHolder holder, int position) {

        final Context context = holder.layout_root.getContext();

        final Story item = getItemAt(position);

        holder.title.setText(item.getTitle());
        holder.title.setTypeface(TypefaceArvoNormal);

        if(mCaller.equals(NewsFragment.class.getSimpleName()) ||
                mCaller.equals(PlayerNewsFragment.class.getSimpleName())) {

            ViewCompat.setElevation(holder.layout_root,
                    context.getResources().getDimension(R.dimen.default_elevation));

        } else if(position > 0) {

            holder.divider.setVisibility(View.VISIBLE);
            holder.title.setPadding(0, 8, 0, 0);
        }

        holder.byline.setText(item.getShortByline());
        holder.tagline.setText(item.getTagline());

        if(item.isRead()) {

            holder.title.setTextColor(0xff888888);
            holder.byline.setTextColor(0xffaaaaaa);
            holder.tagline.setTextColor(0xffaaaaaa);

        } else {

            holder.title.setTextColor(0xff444444);
            holder.byline.setTextColor(0xff555555);
            holder.tagline.setTextColor(0xff555555);
        }

        ViewGroup.LayoutParams layoutParams = holder.thumbnail.getLayoutParams();

        final float DENSITY = context.getResources().getDisplayMetrics().density;
        Resources res = context.getResources();

        final int DEFAULT_WIDTH =
                (int) (res.getDimension(R.dimen.story_thumbnail_width) * DENSITY);

        final int PLACEHOLDER_WIDTH =
                (int) (res.getDimension(R.dimen.story_thumbnail_height) * DENSITY);

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
                .fitCenter()
                .into(holder.thumbnail);

        holder.toggleSaveIcon(item.isSaved());

        holder.layout_root.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent storyIntent = new Intent(context, StoryActivity.class);
                storyIntent.putExtra(Story.PARAM_STORY_PARCEL, item);

                context.startActivity(storyIntent);
            }
        });

        holder.layout_root.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                item.toggleIsSaved();

                holder.toggleSaveIcon(item.isSaved());

                ContentValues values = new ContentValues();
                values.put(StoryEntry.COL_IS_SAVED, item.isSaved()
                        ? item.getSavedTimestamp() : - item.getSavedTimestamp());

                context.getContentResolver().update(StoryEntry.buildUri(item.getKey()),
                        values, null, null);

                String message = context.getString(item.isSaved()
                        ? R.string.story_saved : R.string.story_unsaved);

                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();

                return true;
            }
        });
    }


    @Override
    public int getItemCount() {

        return mData != null ? mData.getCount() : mItems.size();
    }


    public Story getItemAt(int pos) {

        if(mItems != null) {

            return mItems.get(pos);
        }

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

        public void toggleSaveIcon(boolean isSaved) {

            save_icon.setImageResource(isSaved
                    ? R.drawable.ic_menu_saved_dark : R.drawable.ic_menu_save_dark);
        }
    }
}
