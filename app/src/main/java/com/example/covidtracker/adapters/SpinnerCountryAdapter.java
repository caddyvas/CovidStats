package com.example.covidtracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.covidtracker.R;
import com.example.covidtracker.countries.CountryWise;

import java.util.List;

public class SpinnerCountryAdapter extends ArrayAdapter<CountryWise> {

    private List<CountryWise> countryWiseList;
    private LayoutInflater inflater;
    private Context mContext;

    public SpinnerCountryAdapter(List<CountryWise> countryWiseList, Context context) {
        super(context, 0, countryWiseList);
        this.countryWiseList = countryWiseList;
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This function called for each row ( Called data.size() times )
    private View getCustomView(int position, View convertView, ViewGroup parent) {

        // Inflate spinner_rows.xml file for each row ( Defined below )
        View row = inflater.inflate(R.layout.spinner_text, parent, false);

        // Get each Model object from Arraylist
        CountryWise countryWise = countryWiseList.get(position);

        TextView country = row.findViewById(R.id.txt_country);
        TextView confirmedCase = row.findViewById(R.id.txt_confirmedCase);

        if (position == 0) {
            country.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            confirmedCase.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        }


        // Set values for spinner each row
        country.setText(countryWise.getCountry());
        confirmedCase.setText(countryWise.getConfirmedCases());

        return row;
    }
}
