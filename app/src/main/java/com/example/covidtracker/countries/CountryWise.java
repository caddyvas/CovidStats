package com.example.covidtracker.countries;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryWise implements Parcelable {

    private String country;
    private String confirmedCases;
    private String fatalCases;
    private String recoveredCases;
    private String province;


    private CountryWise(Parcel parcel) {
        country = parcel.readString();
        confirmedCases = parcel.readString();
        recoveredCases = parcel.readString();
        fatalCases = parcel.readString();
    }

    public CountryWise() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getConfirmedCases() {
        return confirmedCases;
    }

    public void setConfirmedCases(String confirmedCases) {
        this.confirmedCases = confirmedCases;
    }

    public String getFatalCases() {
        return fatalCases;
    }

    public void setFatalCases(String fatalCases) {
        this.fatalCases = fatalCases;
    }

    public String getRecoveredCases() {
        return recoveredCases;
    }

    public void setRecoveredCases(String recoveredCases) {
        this.recoveredCases = recoveredCases;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Store the countryWise data in parcel object
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(country);
        parcel.writeString(confirmedCases);
        parcel.writeString(recoveredCases);
        parcel.writeString(fatalCases);
    }

    public static final Creator<CountryWise> CREATOR = new Creator<CountryWise>() {
        @Override
        public CountryWise createFromParcel(Parcel parcel) {
            return new CountryWise(parcel);
        }

        @Override
        public CountryWise[] newArray(int i) {
            return new CountryWise[i];
        }
    };

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}