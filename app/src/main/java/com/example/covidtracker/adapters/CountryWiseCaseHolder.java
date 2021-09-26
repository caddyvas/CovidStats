package com.example.covidtracker.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.covidtracker.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class CountryWiseCaseHolder extends RecyclerView.ViewHolder {

    TextView mProvinceTxt;
    TextView mConfirmedCases;
    TextView mRecoveredCases;
    TextView mFatalCases;
    TextView mFatalCasesLabel;

    CountryWiseCaseHolder(@NonNull View itemView) {
        super(itemView);

        this.mProvinceTxt = itemView.findViewById(R.id.txt_recyc_province_value);
        this.mConfirmedCases = itemView.findViewById(R.id.txt_recyc_confirmed_value);
        this.mRecoveredCases = itemView.findViewById(R.id.txt_recyc_recovered_value);
        this.mFatalCases = itemView.findViewById(R.id.txt_recyc_fatal_value);
        this.mFatalCasesLabel = itemView.findViewById(R.id.txt_recyc_fatal_label);
    }
}
