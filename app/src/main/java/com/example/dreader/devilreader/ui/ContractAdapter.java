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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractViewHolder>{

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
        holder.value.setText(createSpan("Value", value));

        String capHit = Util.format$(contract.getCapHit());
        holder.cap_hit.setText(createSpan("Cap Hit",capHit));

        switch(contract.getType()) {

            case "SPC": {

                holder.type.setText(createSpan("Standard Player Contract", ""));

                break;
            }

            case "ELC": {

                holder.type.setText(createSpan("Entry Level Contract", ""));

                break;
            }

            case "H": {

                holder.type.setText(createSpan("Type", "Historic"));

                break;
            }

            default: {

                holder.type.setText(createSpan("Type", "N/A"));
            }
        }

        String expiry;

        switch(contract.getExpiry()) {

            case "UFA": { expiry = "Unrestricted"; break; }
            case "RFA": { expiry = "Restricted"; break; }
            default: { expiry = "N/A"; }
        }

        holder.expiry.setText(createSpan("Expiry", expiry));

        List<PlayerContract.ContractYear> years = contract.getYears();

        for(int i = 0; i < years.size(); i++) {

            PlayerContract.ContractYear year = years.get(i);

            bindYear(holder.years.getChildAt(i), year.getSeason(), year.getNhlSalary(),
                    year.getAhlSalary(), year.getPerformanceBonus(), year.getSigningBonus(), year.getClause());
        }
    }

    private void bindYear(View layout, String s, long nSalary, long aSalary,
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
        signing.setText(createSpan("Signing Bonus", Util.format$((int) sBonus)));

        final TextView performance = (TextView) extras.findViewById(R.id.contract_year_performance);
        performance.setText(createSpan("Performance Bonus", Util.format$((int) pBonus)));

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
