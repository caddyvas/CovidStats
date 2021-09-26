package com.example.covidtracker.viewmodel;

import com.example.covidtracker.repository.RetrieveInfoRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TrackerInfoViewModelFactory implements ViewModelProvider.Factory {

    private RetrieveInfoRepository retrieveInfoRepository;

    public TrackerInfoViewModelFactory(RetrieveInfoRepository retrieveInfoRepository) {
        this.retrieveInfoRepository = retrieveInfoRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TrackerInfoViewModel(retrieveInfoRepository);
    }
}
