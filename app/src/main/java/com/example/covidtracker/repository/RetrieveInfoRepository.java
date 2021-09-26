package com.example.covidtracker.repository;

import android.os.AsyncTask;
import android.util.Pair;

import com.example.covidtracker.utilities.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class RetrieveInfoRepository {

    private MutableLiveData<Pair<Integer, Elements>> mScrapeResponse = new MutableLiveData<>();
    public LiveData<Pair<Integer, Elements>> mScrapeObserver;


    private Elements mCovidElements = null;
    private Document mCovidDocument = null;

    private int taskExecuted;

    public RetrieveInfoRepository() {
        // Apply transformation to observe change in network response
        mScrapeObserver = Transformations.map(mScrapeResponse, result -> getNetworkResponse());
    }

    public void getGlobalCaseInformation() {
        // start AsyncTask to collect Global data
        new WebScrapData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.SCRAPE_TASK_GLOBAL);
    }

    public void getCountryWiseInfo() {
        // start AsyncTask to collect country wise data
        new WebScrapData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.SCRAPE_TASK_COUNTRYWISE);
    }

    public void getInfoForCountry(String countrySelected) {
        int countryValue = 0;
        switch (countrySelected) {
            case Constants.COUNTRY_USA:
                countryValue = Constants.SCRAPE_TASK_USA;
                break;
            case Constants.COUNTRY_INDIA:
                countryValue = Constants.SCRAPE_TASK_INDIA;
                break;
            case Constants.COUNTRY_CANADA:
                countryValue = Constants.SCRAPE_TASK_CANADA;
                break;

            default:
                System.out.println(this.getClass().getSimpleName() + " - Default executed");
        }
        // start AsyncTask to collect for specific country
        new WebScrapData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, countryValue);
    }

    private Pair<Integer, Elements> getNetworkResponse() {
        return mScrapeResponse.getValue();
    }

    private class WebScrapData extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {

            int taskType = integers[0];
            switch (taskType) {

                case Constants.SCRAPE_TASK_GLOBAL:
                    try {
                        mCovidDocument = Jsoup.connect(Constants.URL_WIKI_GLOBAL).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCovidElements = mCovidDocument.select(Constants.DOC_TABLE_GLOBAL);
                    taskExecuted = Constants.SCRAPE_TASK_GLOBAL;
                    break;

                case Constants.SCRAPE_TASK_COUNTRYWISE:
                    try {
                        mCovidDocument = Jsoup.connect(Constants.URL_WIKI_GLOBAL).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCovidElements = mCovidDocument.select(Constants.DOC_TABLE_COUNTRIES);
                    taskExecuted = Constants.SCRAPE_TASK_COUNTRYWISE;
                    break;

                case Constants.SCRAPE_TASK_USA:
                    try {
                        mCovidDocument = Jsoup.connect(Constants.URL_WIKI_USA).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCovidElements = mCovidDocument.select(Constants.DOC_TABLE_USA).
                            not(Constants.TR_SORTBOTTOM).not(Constants.TAG_TR_EQ_0).
                            not(Constants.TAG_TR_EQ_1).
                            not(Constants.TAG_TR_EQ_2);
                    taskExecuted = Constants.SCRAPE_TASK_USA;
                    break;

                case Constants.SCRAPE_TASK_CANADA:
                    try {
                        mCovidDocument = Jsoup.connect(Constants.URL_WIKI_CANADA).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCovidElements = mCovidDocument.select(Constants.DOC_TABLE_CANADA);
                    taskExecuted = Constants.SCRAPE_TASK_CANADA;
                    break;

                case Constants.SCRAPE_TASK_INDIA:
                    try {
                        mCovidDocument = Jsoup.connect(Constants.URL_WIKI_INDIA).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCovidElements = mCovidDocument.select(Constants.DOC_TABLE_INDIA).
                            not(Constants.TR_SORTBOTTOM).
                            not(Constants.TR_SORTTOP).
                            not(Constants.TAG_TR_EQ_0).
                            not(Constants.TAG_TR_EQ_1);
                    taskExecuted = Constants.SCRAPE_TASK_INDIA;
                    break;

                default:
                    System.out.println(this.getClass().getSimpleName() + "---" + "Background Task - Default Executed");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Pair<Integer, Elements> resultPair;
            System.out.println(this.getClass().getSimpleName() + "--" + "SCRAPE_TASK: " + taskExecuted + " has been Processed !!");
            resultPair = new Pair<>(taskExecuted, mCovidElements);
            mScrapeResponse.postValue(resultPair);
        }
    }
}
