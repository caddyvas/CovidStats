package com.example.covidtracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covidtracker.R;
import com.example.covidtracker.countries.CountryWise;
import com.example.covidtracker.countries.UnitedStates;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CountryWiseCaseAdapter extends RecyclerView.Adapter<CountryWiseCaseHolder> {

    private ArrayList<CountryWise> mCountryWiseCasesList;

    private boolean isFatalCasesDisplayed;

    public CountryWiseCaseAdapter(ArrayList<CountryWise> mCountryWiseCasesList) {
        this.mCountryWiseCasesList = mCountryWiseCasesList;
    }

    @NonNull
    @Override
    public CountryWiseCaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new CountryWiseCaseHolder(view); // this would return our view to holder class
    }

    @Override
    public void onBindViewHolder(@NonNull CountryWiseCaseHolder holder, int position) {

        CountryWise countryWiseCase = mCountryWiseCasesList.get(position);
        holder.mProvinceTxt.setText(countryWiseCase.getProvince());
        holder.mConfirmedCases.setText(countryWiseCase.getConfirmedCases());
        holder.mRecoveredCases.setText(countryWiseCase.getRecoveredCases());
        holder.mFatalCases.setText(countryWiseCase.getFatalCases());
        holder.mFatalCasesLabel.setVisibility(isFatalCasesDisplayed ? View.VISIBLE : View.GONE);
        holder.mFatalCases.setVisibility(isFatalCasesDisplayed ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mCountryWiseCasesList.size();
    }

    public void isFatalCaseDisplayed(boolean isDisplayed) {
        isFatalCasesDisplayed = isDisplayed;
    }

    /**
     * Contains the list of searched province
     *
     * @param searchCountryWiseList - parameter
     */
    public void filteredList(ArrayList<CountryWise> searchCountryWiseList) {
        mCountryWiseCasesList = searchCountryWiseList;
        notifyDataSetChanged();
    }
}
