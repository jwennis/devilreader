package com.example.dreader.devilreader.ui;


import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.Util;
import com.example.dreader.devilreader.model.PlayerContract;

import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractViewHolder>{

    private static final String ATTR_STANDARD_PLAYER_CONTRACT = "SPC";
    private static final String ATTR_ENTRY_LEVEL_CONTRACT = "ELC";
    private static final String ATTR_HISTORIC_CONTRACT = "H";
    private static final String ATTR_UNRESTRICTED_FREE_AGENT = "UFA";
    private static final String ATTR_RESTRICTED_FREE_AGENT = "RFA";

    private List<PlayerContract> mItems;


    public ContractAdapter(List<PlayerContract> list) {

        super();

        mItems = list;
    }


    @Override
    public ContractAdapter.ContractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contractView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_player_contract, parent, false);

        return new ContractViewHolder(contractView);
    }


    private SpannableStringBuilder createSpan(String label, String value) {

        SpannableStringBuilder builder = new SpannableStringBuilder(label + "  " + value);

        builder.setSpan(new StyleSpan(Typeface.BOLD),
                0, label.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }


    @Override
    public void onBindViewHolder(ContractAdapter.ContractViewHolder holder, int position) {

        PlayerContract contract = mItems.get(position);

        String value = Util.format$(contract.getValue());
        holder.value.setText(createSpan(holder.CONTRACT_LABEL_VALUE, value));

        String capHit = Util.format$(contract.getCapHit());
        holder.cap_hit.setText(createSpan(holder.CONTRACT_LABEL_CAPHIT, capHit));

        switch(contract.getType()) {

            case ATTR_STANDARD_PLAYER_CONTRACT: {

                holder.type.setText(createSpan(holder.STANDARD_PLAYER_CONTRACT, ""));

                break;
            }

            case ATTR_ENTRY_LEVEL_CONTRACT: {

                holder.type.setText(createSpan(holder.ENTRY_LEVEL_CONTRACT, ""));

                break;
            }

            case ATTR_HISTORIC_CONTRACT: {

                holder.type.setText(createSpan(holder.CONTRACT_LABEL_TYPE, holder.HISTORIC_CONTRACT));

                break;
            }

            default: {

                holder.type.setText(createSpan(holder.CONTRACT_LABEL_TYPE, holder.CONTRACT_TYPE_NA));
            }
        }

        String expiry;

        switch(contract.getExpiry()) {

            case ATTR_UNRESTRICTED_FREE_AGENT: { expiry = holder.CONTRACT_LABEL_UNRESTRICTED; break; }
            case ATTR_RESTRICTED_FREE_AGENT: { expiry = holder.CONTRACT_LABEL_RESTRICTED; break; }
            default: { expiry = holder.CONTRACT_TYPE_NA; }
        }

        holder.expiry.setText(createSpan(holder.CONTRACT_LABEL_EXPIRY, expiry));

        List<PlayerContract.ContractYear> years = contract.getYears();

        for(int i = 0; i < years.size(); i++) {

            PlayerContract.ContractYear year = years.get(i);

            String clause = year.isNoMove() ? holder.NO_MOVEMENT_CLAUSE :
                    year.isNoTrade() ? holder.NO_TRADE_CLAUSE : "";

            bindYear(holder, holder.years.getChildAt(i), year.getSeason(), year.getNhlSalary(),
                    year.getAhlSalary(), year.getPerformanceBonus(), year.getSigningBonus(), clause);
        }
    }


    private void bindYear(ContractViewHolder holder, View layout, String s, long nSalary, long aSalary,
                          long sBonus, long pBonus, final String c) {

        if(aSalary == 0) {

            aSalary = nSalary;
        }

        TextView season = (TextView) layout.findViewById(R.id.contract_year_season);
        season.setText(s);

        TextView nhl = (TextView) layout.findViewById(R.id.contract_year_nhl);
        nhl.setText(format$((int) nSalary));

        TextView ahl = (TextView) layout.findViewById(R.id.contract_year_ahl);
        ahl.setText(format$((int) aSalary));

        layout.setVisibility(View.VISIBLE);

        final ImageView expand = (ImageView) layout.findViewById(R.id.contract_year_expand);
        final ImageView collapse = (ImageView) layout.findViewById(R.id.contract_year_collapse);
        final ViewGroup extras = (ViewGroup) layout.findViewById(R.id.contract_year_extras);

        final TextView signing = (TextView) extras.findViewById(R.id.contract_year_signing);
        signing.setText(createSpan(holder.CONTRACT_LABEL_SIGNING_BONUS, Util.format$((int) sBonus)));

        final TextView performance = (TextView) extras.findViewById(R.id.contract_year_performance);
        performance.setText(createSpan(holder.CONTRACT_LABEL_PERFORMANCE_BONUS, Util.format$((int) pBonus)));

        final TextView clause = (TextView) extras.findViewById(R.id.contract_year_clause);
        clause.setText(c);

        expand.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                extras.setVisibility(View.VISIBLE);

                if(c.length() > 0) {

                    clause.setVisibility(View.VISIBLE);
                }

                expand.setVisibility(View.GONE);
                collapse.setVisibility(View.VISIBLE);
            }
        });

        collapse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                extras.setVisibility(View.GONE);

                expand.setVisibility(View.VISIBLE);
                collapse.setVisibility(View.GONE);
            }
        });
    }


    private String format$(int number) {

        return number > 0 ? Util.format$(number) : "-";
    }


    @Override
    public int getItemCount() {

        return mItems.size();
    }


    public class ContractViewHolder extends RecyclerView.ViewHolder {

        @BindString(R.string.player_contract_nomove)
        String NO_MOVEMENT_CLAUSE;

        @BindString(R.string.player_contract_notrade)
        String NO_TRADE_CLAUSE;

        @BindString(R.string.player_contract_type_standard)
        String STANDARD_PLAYER_CONTRACT;

        @BindString(R.string.player_contract_type_entry_level)
        String ENTRY_LEVEL_CONTRACT;

        @BindString(R.string.player_contract_type_historic)
        String HISTORIC_CONTRACT;

        @BindString(R.string.player_contract_type_na)
        String CONTRACT_TYPE_NA;

        @BindString(R.string.player_contract_label_type)
        String CONTRACT_LABEL_TYPE;

        @BindString(R.string.player_contract_label_expiry)
        String CONTRACT_LABEL_EXPIRY;

        @BindString(R.string.player_contract_label_value)
        String CONTRACT_LABEL_VALUE;

        @BindString(R.string.player_contract_label_caphit)
        String CONTRACT_LABEL_CAPHIT;

        @BindString(R.string.player_contract_label_unrestricted)
        String CONTRACT_LABEL_UNRESTRICTED;

        @BindString(R.string.player_contract_label_restricted)
        String CONTRACT_LABEL_RESTRICTED;

        @BindString(R.string.player_contract_label_signing)
        String CONTRACT_LABEL_SIGNING_BONUS;

        @BindString(R.string.player_contract_label_performance)
        String CONTRACT_LABEL_PERFORMANCE_BONUS;

        @BindView(R.id.contract_value)
        TextView value;

        @BindView(R.id.contract_caphit)
        TextView cap_hit;

        @BindView(R.id.contract_type)
        TextView type;

        @BindView(R.id.contract_expiry)
        TextView expiry;

        @BindView(R.id.contract_years)
        LinearLayout years;

        public ContractViewHolder(View view) {

            super(view);

            ButterKnife.bind(this, view);
        }
    }
}
