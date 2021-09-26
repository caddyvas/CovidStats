package com.example.covidtracker.countries;

public class Global {

    private String confirmedCases;
    private String recoveredCases;
    private String fatalCases;

    public String getConfirmedCases() {
        return confirmedCases;
    }

    public void setConfirmedCases(String confirmedCases) {
        this.confirmedCases = confirmedCases;
    }

    public String getRecoveredCases() {
        return recoveredCases;
    }

    public void setRecoveredCases(String recoveredCases) {
        this.recoveredCases = recoveredCases;
    }

    public String getFatalCases() {
        return fatalCases;
    }

    public void setFatalCases(String fatalCases) {
        this.fatalCases = fatalCases;
    }
}
