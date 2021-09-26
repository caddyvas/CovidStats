package com.example.covidtracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.covidtracker.R;
import com.example.covidtracker.utilities.Constants;
import com.example.covidtracker.utilities.UIUtils;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1500;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_splash);

        // check for internet connection
        boolean isInternetAvailable = UIUtils.getInstance().isInternetAvailable(SplashScreen.this);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(SplashScreen.this, MainActivity.class);
                in.putExtra(Constants.SPLASH_INTERNET_CHECK, isInternetAvailable);
                startActivity(in);
                SplashScreen.this.finish();
            }
        }, SPLASH_DURATION);
    }
}