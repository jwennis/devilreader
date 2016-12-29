package com.example.dreader.devilreader.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;


public class PlayerContract {

    private long playerId;

    private String type;
    private String expiry;
    private List<ContractYear> years;


    public PlayerContract() {

    }


    public PlayerContract(String t, String e) {

        type = t;
        expiry = e;

        years = new ArrayList<>();
    }


    public PlayerContract(DataSnapshot data) {

        playerId = (long) data.child("player_id").getValue();
        type = (String) data.child("type").getValue();
        expiry = (String) data.child("expiry").getValue();
        years = new ArrayList<>();

        for(DataSnapshot year : data.child("years").getChildren()) {

            String season = (String) year.child("season").getValue();
            long nSalary = (long) year.child("salary_nhl").getValue();
            long aSalary = (long) year.child("salary_ahl").getValue();
            long sBonus = (long) year.child("bonus_signing").getValue();
            long pBonus = (long) year.child("bonus_performance").getValue();
            boolean mClause = (boolean) year.child("clause_nomove").getValue();
            boolean tClause = (boolean) year.child("clause_notrade").getValue();

            addYear(season, nSalary, aSalary, sBonus, pBonus, mClause, tClause);
        }
    }


    public void addYear(String season, long nSalary, long aSalary,
                        long sBonus, long pBonus, boolean mClause, boolean tClause) {

        years.add(new ContractYear(season, nSalary, aSalary,
                sBonus, pBonus, mClause, tClause));
    }


    public String getType() {

        return type;
    }


    public String getExpiry() {

        return expiry;
    }


    public List<ContractYear> getYears() {

        return years;
    }


    public int getCapHit() {

        int totalSalary = 0;
        int numYears = 0;

        for(ContractYear year : years) {

            if(year.getNhlSalary() > 0) {

                totalSalary += year.getNhlSalary();
                numYears++;
            }
        }

        return numYears > 0 ? totalSalary / numYears : 0;
    }


    public void print() {

        Log.v("DREADER", "Contract for " + playerId);
        Log.v("DREADER", "Type =  " + type);
        Log.v("DREADER", "Expiry = " + expiry);

        for(ContractYear year : years) {

            year.print();
        }
    }


    public class ContractYear {

        private String season;
        private long salary_nhl;
        private long salary_ahl;
        private long bonus_signing;
        private long bonus_performance;
        private boolean clause_nomove;
        private boolean clause_notrade;


        public ContractYear(String s, long nSalary, long aSalary,
                            long sBonus, long pBonus, boolean mClause, boolean tClause) {

            season = s;
            salary_nhl = nSalary;
            salary_ahl = aSalary;
            bonus_signing = sBonus;
            bonus_performance = pBonus;
            clause_nomove = mClause;
            clause_notrade = tClause;
        }


        public String getSeason() {

            return season;
        }


        public long getNhlSalary() {

            return salary_nhl;
        }


        public long getAhlSalary() {

            return salary_ahl;
        }


        public long getSigningBonus() {

            return bonus_signing;
        }


        public long getPerformanceBonus() {

            return bonus_performance;
        }


        public boolean isNoMove() {

            return clause_nomove;
        }


        public boolean isNoTrade() {

            return clause_notrade;
        }


        public void print() {

            Log.v("DREADER", season + ": "
                    + "NHL " + salary_nhl +  ", "
                    + "AHL " + salary_ahl +  ", "
                    + "Signing " + bonus_signing +  ", "
                    + "Performance " + bonus_performance +  ", "
                    + "No Move? " + clause_nomove +  ", "
                    + "No Trade? " + clause_notrade);
        }
    }
}