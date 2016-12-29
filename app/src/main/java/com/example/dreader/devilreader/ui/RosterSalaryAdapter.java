package com.example.dreader.devilreader.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
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

public class RosterSalaryAdapter extends RecyclerView.Adapter<RosterSalaryAdapter.SalaryViewHolder> {

    private static Typeface TypefaceArvoNormal;

    private List<Player> mItems;

    public RosterSalaryAdapter(List<Player> items) {

        super();

        mItems = items;
    }


    @Override
    public RosterSalaryAdapter.SalaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new SalaryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_roster_salary, parent, false));
    }


    @Override
    public void onBindViewHolder(RosterSalaryAdapter.SalaryViewHolder holder, int position) {

        final Context context = holder.layout_root.getContext();
        final Player item = mItems.get(position);

        List<String> values = item.getCapLabelValues();

        for(int i = 0; i < values.size(); i++) {

            TextView label = holder.getLabel(i);
            String value = values.get(i);

            if(value.equals("UFA")) {

                label.setTextColor(Color.parseColor("#ffffff"));
                label.setBackgroundColor(Color.parseColor("#2c7a17"));

            } else if (value.equals("RFA")) {

                label.setTextColor(Color.parseColor("#ffffff"));
                label.setBackgroundColor(Color.parseColor("#d30000"));

            } else {

                label.setTextColor(Color.parseColor("#000000"));
            }

            label.setText(value);
            label.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {

        return mItems.size();
    }


    protected class SalaryViewHolder extends RecyclerView.ViewHolder {

        ViewGroup layout_root;


        public SalaryViewHolder(View itemView) {

            super(itemView);

            layout_root = (ViewGroup) itemView;

            ButterKnife.bind(this, layout_root);
        }

        public TextView getLabel(int pos) {

            return (TextView) layout_root.getChildAt(pos);
        }
    }
}

