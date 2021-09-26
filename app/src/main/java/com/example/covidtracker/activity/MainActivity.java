package com.example.covidtracker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.R;
import com.example.covidtracker.adapters.SpinnerCountryAdapter;
import com.example.covidtracker.countries.CountryWise;
import com.example.covidtracker.repository.RetrieveInfoRepository;
import com.example.covidtracker.utilities.Constants;
import com.example.covidtracker.viewmodel.TrackerInfoViewModel;
import com.example.covidtracker.viewmodel.TrackerInfoViewModelFactory;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // JSoup Components
    private Elements mCovidOverallCases;
    private Elements mCovidCountryWiseCases;

    // Layout components
    private TextView mConfirmedCasesTxt;
    private TextView mRecoveredCasesTxt;
    private TextView mFatalCasesTxt;
    private TextView mFatalCaseTxtLabel;
    private Button mViewCountryWiseBtn;
    private Spinner mCountrySpinner;
    private Switch mFatalCaseSwitch;
    private ProgressBar mProgressBar;

    private TrackerInfoViewModel mTrackerInfoViewModel;
    private CountryWise mSelectedCountry;

    private String mSpinnerSelectedCountry = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get intent values from Splash
        if (getIntent() != null) {
            boolean isInternetActive = getIntent().getBooleanExtra(Constants.SPLASH_INTERNET_CHECK, false);
            if (!isInternetActive) {
                displayToastMessage();
                return;
            }
        }

        // initialize layout components TODO - import butterknife library
        initialize();

        startProgressBar();

        // setup ViewModel
        mTrackerInfoViewModel = new ViewModelProvider(this, new TrackerInfoViewModelFactory(new RetrieveInfoRepository()))
                .get(TrackerInfoViewModel.class);

        mTrackerInfoViewModel.mScrapeResponse.observe(this, scrapeResult -> {

            int resultCase = scrapeResult.first;
            switch (resultCase) {
                case Constants.SCRAPE_TASK_GLOBAL:
                    mCovidOverallCases = scrapeResult.second;
                    // upon receiving Global case, start Country wise results
                    mTrackerInfoViewModel.obtainCountryWiseInfo();
                    break;
                case Constants.SCRAPE_TASK_COUNTRYWISE:
                    mCovidCountryWiseCases = scrapeResult.second;
                    // upon receiving Global case, populate results
                    populateGlobalAndCountryWiseCases();
                    break;
            }
        });
        mTrackerInfoViewModel.obtainGlobalCasesInfo();

        handleSwitchForFatalCases();
    }

    private void initialize() {
        mConfirmedCasesTxt = findViewById(R.id.main_confirmed_value);
        mRecoveredCasesTxt = findViewById(R.id.main_recovered_value);
        mFatalCasesTxt = findViewById(R.id.main_fatal_value);
        mCountrySpinner = findViewById(R.id.spinner_country);
        mFatalCaseTxtLabel = findViewById(R.id.main_fatal_label);
        mViewCountryWiseBtn = findViewById(R.id.btn_countrywise);
        mFatalCaseSwitch = findViewById(R.id.fatal_case_switch);
        mProgressBar = findViewById(R.id.progressbar);
        ImageView mImgRefresh = findViewById(R.id.img_refresh);
        ImageView mImgAbout = findViewById(R.id.img_about);
        mViewCountryWiseBtn = findViewById(R.id.btn_countrywise);

        // set on click listeners for buttons
        mViewCountryWiseBtn.setOnClickListener(this);
        mImgRefresh.setOnClickListener(this);
        mImgAbout.setOnClickListener(this);

        // capture spinner selection
        captureSpinnerSelection();
        TextView textView = findViewById(R.id.textView12);
        textView.setSelected(true);

    }

    private void populateGlobalAndCountryWiseCases() {

        // Display Overall cases
        List<CountryWise> mCountryWiseList = new ArrayList<>();
        if (mCovidOverallCases != null) {
            String confirmedCases = mCovidOverallCases.get(2).text();
            String recoveredCases = mCovidOverallCases.get(4).text();
            String fatalCases = mCovidOverallCases.get(3).text();

            mConfirmedCasesTxt.setText(confirmedCases);
            mRecoveredCasesTxt.setText(recoveredCases);
            mFatalCasesTxt.setText(fatalCases);
        }

        for (Element element : mCovidCountryWiseCases) {

            CountryWise countryWise = new CountryWise();
            String country = element.select("th[scope=row] a[title]").text();
            Elements ele = element.getElementsByTag("td").not("td:eq(5)");

            String confirmedCase = ele.select("td:eq(2)").text();
            String deathCase = ele.select("td:eq(3)").text();
            String recoveredCase = ele.select("td:eq(4)").text();

            if (confirmedCase.isEmpty() && deathCase.isEmpty() && recoveredCase.isEmpty()) {
                // skip
                continue;
            }

            countryWise.setCountry(country);
            countryWise.setConfirmedCases(confirmedCase);
            countryWise.setFatalCases(deathCase);
            countryWise.setRecoveredCases(recoveredCase);
            mCountryWiseList.add(countryWise);
        }

        // add text to top position - index 0
        CountryWise countryWise = new CountryWise();
        countryWise.setCountry(getResources().getString(R.string.country_spinner_txt));
        countryWise.setConfirmedCases(getResources().getString(R.string.confirmed_cases_spinner_txt));
        mCountryWiseList.add(0, countryWise);

        SpinnerCountryAdapter spinnerCountryAdapter = new SpinnerCountryAdapter(mCountryWiseList, MainActivity.this);
        mCountrySpinner.setAdapter(spinnerCountryAdapter);
        // once everything is complete stop progress bar
        stopProgressBar();
    }

    /*
    Method handles the switch operation for Fatal cases
     */
    private void handleSwitchForFatalCases() {

        mFatalCaseSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            // if true, show Fatal cases
            if (isChecked) {
                mFatalCasesTxt.setVisibility(View.VISIBLE);
                mFatalCaseTxtLabel.setVisibility(View.VISIBLE);
                mFatalCaseSwitch.setText(getText(R.string.hide_fatalcase));
            } else {
                mFatalCasesTxt.setVisibility(View.INVISIBLE);
                mFatalCaseTxtLabel.setVisibility(View.INVISIBLE);
                mFatalCaseSwitch.setText(getText(R.string.show_fatalcase));
            }
        });
    }

    private void startProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        // disable user interaction
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void stopProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        // enable user interaction back
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_refresh:
                startProgressBar();
                mTrackerInfoViewModel.obtainGlobalCasesInfo();
                break;

            case R.id.img_about:
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.popup_mainscreen);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.TOP);
                dialog.show();
                break;

            case R.id.btn_countrywise:
                goToNextScreen();
                break;
        }
    }

    /*
     Capture spinner selection
     */
    private void captureSpinnerSelection() {

        mCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                mSelectedCountry = (CountryWise) adapterView.getItemAtPosition(position);
                mSpinnerSelectedCountry = (mSelectedCountry.getCountry() != null ? mSelectedCountry.getCountry() : "");
                enableDisableCountryWiseButton(mSpinnerSelectedCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /*
    based on spinner selection, enable or disable the button
     */
    private void enableDisableCountryWiseButton(String countrySelected) {

        boolean isSelected = countrySelected.equals(Constants.COUNTRY_USA) ||
                countrySelected.equals(Constants.COUNTRY_CANADA) ||
                countrySelected.equals(Constants.COUNTRY_INDIA);

        mViewCountryWiseBtn.setEnabled(isSelected);
        mViewCountryWiseBtn.setBackgroundColor(isSelected ?
                getResources().getColor(R.color.buttonEnable) : getResources().getColor(R.color.buttonDisable));
    }

    private void displayToastMessage() {

        // throw a toast message in case of no internet // TODO convert this to a UI feature
        Toast toast = Toast.makeText(MainActivity.this, getString(R.string.no_internet), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    /*
    Go to next screen - carry spinner selected item
     */
    private void goToNextScreen() {
        Intent in = new Intent(MainActivity.this, CountryWiseActivity.class);
        in.putExtra(Constants.COUNTRY_SPECIFIED, mSelectedCountry);
        startActivity(in);
    }
}