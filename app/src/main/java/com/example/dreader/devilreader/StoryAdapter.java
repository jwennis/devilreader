package com.example.dreader.devilreader;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dreader.devilreader.data.StoryContract;
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

        Story item = getItemAt(position);

        holder.title.setText(item.getTitle());
        holder.title.setTypeface(TypefaceArvoNormal);
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
