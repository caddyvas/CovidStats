package com.example.covidtracker.viewmodel;

import android.util.Pair;

import com.example.covidtracker.repository.RetrieveInfoRepository;

import org.jsoup.select.Elements;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TrackerInfoViewModel extends ViewModel {


    private RetrieveInfoRepository retrieveInfoRepository;
    public LiveData<Pair<Integer, Elements>> mScrapeResponse;

    TrackerInfoViewModel(RetrieveInfoRepository retrieveInfoRepository) {
        this.retrieveInfoRepository = retrieveInfoRepository;
        mScrapeResponse = retrieveInfoRepository.mScrapeObserver;
    }

    public void obtainGlobalCasesInfo() {
        retrieveInfoRepository.getGlobalCaseInformation();
    }

    public void obtainCountryWiseInfo() {
        retrieveInfoRepository.getCountryWiseInfo();
    }

    public void obtainInfoForCountry(String countrySelected) {
        retrieveInfoRepository.getInfoForCountry(countrySelected);
    }
}
