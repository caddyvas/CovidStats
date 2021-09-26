package com.example.covidtracker.utilities;

public class Constants {

    // URL Constants
    public static final String URL_WIKI_GLOBAL = "https://en.wikipedia.org/wiki/COVID-19_pandemic";
    public static final String URL_WIKI_CANADA = "https://en.wikipedia.org/wiki/COVID-19_pandemic_in_Canada";
    public static final String URL_WIKI_USA = "https://en.wikipedia.org/wiki/COVID-19_pandemic_in_the_United_States";
    public static final String URL_WIKI_INDIA = "https://en.wikipedia.org/wiki/COVID-19_pandemic_in_India";

    // HTML TABLES in WIKI Site as Constants
    public static final String DOC_TABLE_GLOBAL = "table.wikitable.plainrowheaders.sortable tbody tr.sorttop th";
    public static final String DOC_TABLE_COUNTRIES = "table.wikitable.plainrowheaders.sortable tbody tr";
    public static final String DOC_TABLE_USA = "table.wikitable.plainrowheaders.sortable tbody tr";
    public static final String DOC_TABLE_INDIA = "table.wikitable.plainrowheaders.sortable tbody tr";
    public static final String DOC_TABLE_CANADA = "table.wikitable.sortable";

    // HTML TAGS as Constants
    public static final String TR_SORTBOTTOM = "tr.sortbottom";
    public static final String TR_SORTTOP = "tr.sorttop";
    public final static String CLASS_COL_XS_8 = "div.col-xs-8";
    public final static String TAG_TBODY = "tbody";

    public final static String TAG_TR = "tr";
    public static final String TAG_TD = "td";
    public static final String TAG_A = "a";
    public static final String TAG_TH = "th";

    public static final String TAG_TR_EQ_0 = "tr:eq(0)";
    public static final String TAG_TR_EQ_1 = "tr:eq(1)";
    public static final String TAG_TR_EQ_2 = "tr:eq(2)";
    public static final String TAG_TR_EQ_3 = "tr:eq(3)";
    public static final String TAG_TR_EQ_4 = "tr:eq(4)";

    public static final String TAG_TD_EQ_0 = "td:eq(0)";
    public static final String TAG_TD_EQ_1 = "td:eq(1)";
    public static final String TAG_TD_EQ_2 = "td:eq(2)";
    public static final String TAG_TD_EQ_3 = "td:eq(3)";
    public static final String TAG_TD_EQ_4 = "td:eq(4)";
    public static final String TAG_TD_EQ_5 = "td:eq(5)";
    public static final String TAG_TD_EQ_6 = "td:eq(6)";
    public static final String TAG_TD_EQ_7 = "td:eq(7)";
    public static final String TAG_TD_EQ_8 = "td:eq(8)";

    public static final String EMPTY_TEXT = "empty";

    public static final String SPLASH_INTERNET_CHECK = "INTERNET_CONNECTIVITY";


    public static final String COUNTRY_SPECIFIED = "COUNTRY_SPECIFIED";
    public static final int SCRAPE_TASK_GLOBAL = 1;
    public static final int SCRAPE_TASK_COUNTRYWISE = 2;
    public static final int SCRAPE_TASK_USA = 3;
    public static final int SCRAPE_TASK_CANADA = 4;
    public static final int SCRAPE_TASK_INDIA = 5;

    public static final String COUNTRY_USA = "United States";
    public static final String COUNTRY_CANADA = "Canada";
    public static final String COUNTRY_INDIA = "India";
}
