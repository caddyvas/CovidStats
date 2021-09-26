package com.example.covidtracker.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.covidtracker.R;
import com.example.covidtracker.adapters.CountryWiseCaseAdapter;
import com.example.covidtracker.countries.CountryWise;
import com.example.covidtracker.repository.RetrieveInfoRepository;
import com.example.covidtracker.utilities.Constants;
import com.example.covidtracker.viewmodel.TrackerInfoViewModel;
import com.example.covidtracker.viewmodel.TrackerInfoViewModelFactory;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CountryWiseActivity extends AppCompatActivity implements View.OnClickListener {

    // JSoup Components
    private Elements mCovidUsaCases;
    private Elements mCovidCanadaCases;
    private Elements mCovidIndiaCases;

    private CountryWiseCaseAdapter mAdapter;

    private TrackerInfoViewModel mTrackerInfoViewModel;

    // Layout items
    private RecyclerView mRecyclerView;
    private Switch mSwitchBtn;
    private EditText mEditTxtSearch;
    private TextView mSideScrollConfirmedCases;
    private TextView mSideScrollRecoveredCases;
    private TextView mSideScrollFatalCasesLabel;
    private TextView mSideScrollFatalCasesValue;
    private View mProgressBarOverlay;

    private ArrayList<CountryWise> mCountryWiseList;
    private CountryWise mCountryOverallCases;

    private static final String LOG_TAG = CountryWiseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countrywise);

        initializeLayoutItems();

        if (getIntent() != null) {
            mCountryOverallCases = getIntent().getParcelableExtra(Constants.COUNTRY_SPECIFIED);
            assert mCountryOverallCases != null;
        }
        populateSideScrollBar();

        // setup ViewModel
        mTrackerInfoViewModel = new ViewModelProvider(this, new TrackerInfoViewModelFactory(new RetrieveInfoRepository()))
                .get(TrackerInfoViewModel.class);

        mTrackerInfoViewModel.mScrapeResponse.observe(this, scrapeResult -> {
            switch (scrapeResult.first) {

                case Constants.SCRAPE_TASK_USA:
                    mCovidUsaCases = scrapeResult.second;
                    // upon receiving USA cases, populate results
                    parseAndPopulateUsaCases();
                    break;
                case Constants.SCRAPE_TASK_CANADA:
                    mCovidCanadaCases = scrapeResult.second;
                    // upon receiving Canada cases, populate results
                    parseAndPopulateCanadaStats();
                    break;
                case Constants.SCRAPE_TASK_INDIA:
                    mCovidIndiaCases = scrapeResult.second;
                    // upon receiving India cases, populate results
                    parseAndPopulateIndiaCases();
                    break;
            }
        });

        if (mCountryOverallCases != null)
            mTrackerInfoViewModel.obtainInfoForCountry(mCountryOverallCases.getCountry());

        handleSwitchButton();

        activateEditTextSearchOperation();

        startProgressBar();
    }

    private void parseAndPopulateUsaCases() {

        for (Element element : mCovidUsaCases) {

            CountryWise usaCases = new CountryWise();

            if (element.text().isEmpty()) {
                continue;
            }

            String province = element.select("th").text();
            String confirmedCases = element.getElementsByTag("td").get(0).text();

            String deathCases = element.getElementsByTag("td").get(1).text();
            String recoveredCases = element.getElementsByTag("td").get(2).text();

            usaCases.setProvince(province);
            usaCases.setConfirmedCases(confirmedCases);
            usaCases.setFatalCases(deathCases);
            usaCases.setRecoveredCases(recoveredCases);

            mCountryWiseList.add(usaCases);
        }
        populateAdapter(Constants.COUNTRY_USA);
    }

    private void parseAndPopulateCanadaStats() {

        if (mCovidCanadaCases.size() > 1)
            mCovidCanadaCases.remove(1);

        Elements ele = mCovidCanadaCases.select("tbody tr").not("tr:eq(0)").not("tr:eq(1)");
        for (Element element : ele) {

            CountryWise canadaCases = new CountryWise();
            Elements elem = element.getElementsByTag("td");
            if (elem.text().isEmpty()) {
                continue;
            }

            String province = elem.select("td:eq(0)").text();
            String confirmedCases = elem.select("td:eq(4)").text();
            String fatalCases = elem.select("td:eq(7)").text();
            String recoveredCases = elem.select("td:eq(6)").text();

            canadaCases.setProvince(province);
            canadaCases.setConfirmedCases(confirmedCases);
            canadaCases.setFatalCases(fatalCases);
            canadaCases.setRecoveredCases(recoveredCases);

            mCountryWiseList.add(canadaCases);
        }
        populateAdapter(Constants.COUNTRY_CANADA);
    }

    private void parseAndPopulateIndiaCases() {

        int indiaStateCount = 36;
        for (int i = 0; i < indiaStateCount; i++) {
            Element element = mCovidIndiaCases.get(i);
            CountryWise indiaCases = new CountryWise();
            String state = element.select("th[scope]").text();
            String activeCases = element.getElementsByTag("td").get(0).text();
            String fatalCases = element.getElementsByTag("td").get(1).text();
            String recoveredCases = element.getElementsByTag("td").get(2).text();

            indiaCases.setProvince(state);
            indiaCases.setConfirmedCases(activeCases);
            indiaCases.setFatalCases(fatalCases);
            indiaCases.setRecoveredCases(recoveredCases);

            mCountryWiseList.add(indiaCases);
        }

        populateAdapter(Constants.COUNTRY_INDIA);

    }

    private void initializeLayoutItems() {

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mSwitchBtn = findViewById(R.id.countrywise_switch);

        ImageView mImgBack = findViewById(R.id.img_countrywise_back);
        ImageView mImgRefresh = findViewById(R.id.img_countrywise_refresh);

        mSideScrollConfirmedCases = findViewById(R.id.sideScroll_conf_value);
        mSideScrollRecoveredCases = findViewById(R.id.sideScroll_recov_value);
        mSideScrollFatalCasesLabel = findViewById(R.id.sideScroll_fatal_label);
        mSideScrollFatalCasesValue = findViewById(R.id.sideScroll_fatal_value);

        mEditTxtSearch = findViewById(R.id.edtTxt_search);
        mEditTxtSearch.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        mProgressBarOverlay = findViewById(R.id.frame_progress_overlay);

        // set onclick listeners
        mImgBack.setOnClickListener(this);
        mImgRefresh.setOnClickListener(this);

        mCountryWiseList = new ArrayList<>();
    }

    private void handleSwitchButton() {

        mSwitchBtn.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            // if true, show Fatal cases
            if (isChecked) {
                displayFatalCases();
            } else {
                hideFatalCases();
            }
            refreshAdapter();
        });
    }

    /**
     * Refresh adapter when needed especially for fatal cases
     */
    private void refreshAdapter() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Hide Keyboard on edit text lose focus
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(mEditTxtSearch.getWindowToken(), 0);
    }

    /**
     * Method to present the cases by text on edit text
     *
     * @param text - from Edit text
     */
    private void filterList(String text) {

        ArrayList<CountryWise> countryWiseList = new ArrayList<>();
        for (CountryWise item : mCountryWiseList) {
            if (item.getProvince().toLowerCase().startsWith(text.toLowerCase())) {
                countryWiseList.add(item);
            }
        }
        mAdapter.filteredList(countryWiseList);
    }

    private void activateEditTextSearchOperation() {
        mEditTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterList(editable.toString());
            }
        });
    }

    private void populateAdapter(String selectedCountry) {
        System.out.println(LOG_TAG + "--" + "Populating Provincial cases for Country: " + selectedCountry);
        int i = 1;
        for (CountryWise provincialCase : mCountryWiseList) {
            System.out.println(i + "--" + provincialCase.getProvince() + "---" + provincialCase.getConfirmedCases() + "---" +
                    provincialCase.getRecoveredCases());
            i++;
        }

        mAdapter = new CountryWiseCaseAdapter(mCountryWiseList);
        mRecyclerView.setAdapter(mAdapter);
        stopProgressBar();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_countrywise_back:
                finish();
                break;

            case R.id.img_countrywise_refresh:
                refreshOperation();
                break;
        }
    }

    /**
     * handle the visibility of Fatal cases
     *
     * @param isVisible - boolean value
     */
    private void showHideFatalCasesTextViews(boolean isVisible) {
        int visibility = (isVisible ? View.VISIBLE : View.GONE);
        mSideScrollFatalCasesLabel.setVisibility(visibility);
        mSideScrollFatalCasesValue.setVisibility(visibility);
    }

    /**
     * Populate the items inside side scroll bar
     */
    private void populateSideScrollBar() {
        /*String doubleSpace = "  ";
        String confirmedCases = getString(R.string.confirmed_cases) + doubleSpace + mCountryOverallCases.getConfirmedCases() + doubleSpace;
        String recoveredCases = getString(R.string.recovered_cases) + doubleSpace + mCountryOverallCases.getRecoveredCases() + doubleSpace;
        String fatalCases = getString(R.string.fatal_cases) + doubleSpace + mCountryOverallCases.getFatalCases() + doubleSpace;
        String concatenate_1 = confirmedCases + recoveredCases;
        String concatenate_2 = confirmedCases + recoveredCases + fatalCases;
        TextView sideScrollMarquee = findViewById(R.id.sideScroll_main_text);*/

        if (mCountryOverallCases != null) {
            mSideScrollConfirmedCases.setText(mCountryOverallCases.getConfirmedCases());
            mSideScrollRecoveredCases.setText(mCountryOverallCases.getRecoveredCases());
            mSideScrollFatalCasesValue.setText(mCountryOverallCases.getFatalCases());
        }
    }

    private void startProgressBar() {
        mProgressBarOverlay.setVisibility(View.VISIBLE);
        // disable user interaction
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void stopProgressBar() {
        mProgressBarOverlay.setVisibility(View.GONE);
        // enable user interaction back
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * Refresh function - on Refresh perform certain duties
     */
    private void refreshOperation() {
        startProgressBar();
        mCountryWiseList.clear();
        refreshAdapter();
        mTrackerInfoViewModel.obtainInfoForCountry(mCountryOverallCases.getCountry());

        // important to check the switch button status
        if (mSwitchBtn.isChecked())
            displayFatalCases();
        else
            hideFatalCases();
    }

    /**
     * Method to show Fatal cases in scroll bar, recycler view list
     */
    private void displayFatalCases() {
        mAdapter.isFatalCaseDisplayed(true);
        mSwitchBtn.setText(getText(R.string.hide_fatalcase));
        showHideFatalCasesTextViews(true);
    }

    /**
     * Method to hide Fatal cases in scroll bar, recycler view list
     */
    private void hideFatalCases() {
        mAdapter.isFatalCaseDisplayed(false);
        mSwitchBtn.setText(getText(R.string.show_fatalcase));
        showHideFatalCasesTextViews(false);
    }
}
