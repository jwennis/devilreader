package com.example.dreader.devilreader.ui;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.Util;
import com.example.dreader.devilreader.model.Game;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> mItems;

    public GameAdapter(List<Game> list) {

        super();

        mItems = list;
    }

    @Override
    public GameAdapter.GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new GameViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_game, parent, false));
    }


    @Override
    public void onBindViewHolder(GameAdapter.GameViewHolder holder, int position) {

        final Game item = getItemAt(position);

        holder.date.setText(item.getDate());

        if(item.isPending()) {

            holder.score.setText(item.getPuckdrop());
            holder.status.setText(item.getNetworks());

        } else {

            int home_score = item.getFinalScoreHome();
            int away_score = item.getFinalScoreAway();

            boolean isWin = item.isHome()
                    ? home_score > away_score : home_score < away_score;

            holder.score.setText(String.format("%d-%d %s",
                    away_score, home_score, isWin ? "W" : "L"));

            holder.status.setText(String.format("FINAL%1$s",
                    item.isRegulation() ? "" :  "/" + item.getStatus()));
        }

        if(item.isHome()) {

            holder.home_icon.setImageResource(Util.getTeamIcon("NJD"));
            holder.away_icon.setImageResource(Util.getTeamIcon(item.getOpponent()));

        } else {

            holder.home_icon.setImageResource(Util.getTeamIcon(item.getOpponent()));
            holder.away_icon.setImageResource(Util.getTeamIcon("NJD"));
        }

        holder.layout_root.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                if (!game.isPending()) {
//
//                    Context context = holder.layout_root.getContext();
//
//                    Intent gameIntent = new Intent(context, GameActivity.class);
//                    gameIntent.putExtra(GameModel.PARAM_GAME_PARCEL, game);
//
//                    context.startActivity(gameIntent);
//                }
            }
        });
    }


    @Override
    public int getItemCount() {

        return mItems != null ? mItems.size() : 0;
    }


    private Game getItemAt(int pos) {

        return pos < getItemCount() ? mItems.get(pos) : null;
    }


    protected class GameViewHolder extends RecyclerView.ViewHolder {

        public ViewGroup layout_root;

        @BindView(R.id.game_date)
        TextView date;

        @BindView(R.id.game_score)
        TextView score;

        @BindView(R.id.game_status)
        TextView status;

        @BindView(R.id.game_icon_home)
        ImageView home_icon;

        @BindView(R.id.game_icon_away)
        ImageView away_icon;


        public GameViewHolder(View itemView) {

            super(itemView);

            layout_root = (ViewGroup) itemView;

            ButterKnife.bind(this, layout_root);
        }
    }
}
