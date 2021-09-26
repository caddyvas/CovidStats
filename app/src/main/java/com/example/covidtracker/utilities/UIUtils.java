package com.example.covidtracker.utilities;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.InetAddresses;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class UIUtils {

    private static UIUtils mInstance = null;

    private String TAG = UIUtils.class.getSimpleName();

    public static UIUtils getInstance() {

        if (mInstance == null) {
            mInstance = new UIUtils();
        }
        return mInstance;
    }

    public boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            System.out.println(TAG + " - " + "API LEVEL is equal to or above 23");
            assert connectivityManager != null;
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }
            System.out.println(TAG + "WIFI: " + capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) +
                    "----" + "PHONE_DATA:" + capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
        } else {
            System.out.println(TAG + " - " + "API LEVEL is below 23");
            assert connectivityManager != null;
            NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            assert dataNetworkInfo != null;
            assert wifiNetworkInfo != null;
            System.out.println(TAG + "WIFI: " + wifiNetworkInfo.isConnected() + "----" + "PHONE_DATA:" + dataNetworkInfo.isConnected());
            return wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected();
        }
    }


}
