package com.example.dreader.devilreader.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;


public class PlayerContract {

    private static final String ATTR_PLAYER_ID = "player_id";
    private static final String ATTR_CONTRACT_TYPE = "type";
    private static final String ATTR_CONTRACT_EXPIRY = "expiry";
    private static final String ATTR_CONTRACT_YEARS = "years";
    private static final String ATTR_CONTRACT_SEASON = "season";
    private static final String ATTR_CONTRACT_SALARY_NHL = "salary_nhl";
    private static final String ATTR_CONTRACT_SALARY_AHL = "salary_ahl";
    private static final String ATTR_CONTRACT_BONUS_S = "bonus_signing";
    private static final String ATTR_CONTRACT_BONUS_P = "bonus_performance";
    private static final String ATTR_CONTRACT_CLAUSE_M = "clause_nomove";
    private static final String ATTR_CONTRACT_CLAUSE_T = "clause_notrade";

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

        playerId = (long) data.child(ATTR_PLAYER_ID).getValue();
        type = (String) data.child(ATTR_CONTRACT_TYPE).getValue();
        expiry = (String) data.child(ATTR_CONTRACT_EXPIRY).getValue();
        years = new ArrayList<>();

        for(DataSnapshot year : data.child(ATTR_CONTRACT_YEARS).getChildren()) {

            String season = (String) year.child(ATTR_CONTRACT_SEASON).getValue();
            long nSalary = (long) year.child(ATTR_CONTRACT_SALARY_NHL).getValue();
            long aSalary = (long) year.child(ATTR_CONTRACT_SALARY_AHL).getValue();
            long sBonus = (long) year.child(ATTR_CONTRACT_BONUS_S).getValue();
            long pBonus = (long) year.child(ATTR_CONTRACT_BONUS_P).getValue();
            boolean mClause = (boolean) year.child(ATTR_CONTRACT_CLAUSE_M).getValue();
            boolean tClause = (boolean) year.child(ATTR_CONTRACT_CLAUSE_T).getValue();

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


    public int getValue() {

        int totalSalary = 0;

        for(ContractYear year : years) {

            totalSalary += year.getNhlSalary();
        }

        return totalSalary;
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
    }
}
