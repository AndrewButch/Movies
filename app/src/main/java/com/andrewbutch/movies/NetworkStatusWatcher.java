package com.andrewbutch.movies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.andrewbutch.movies.activities.MainActivity;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkStatusWatcher extends BroadcastReceiver {
    ConnectivityManager connectivityManager;
    MainActivity mainActivity;

    public NetworkStatusWatcher(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        connectivityManager = (ConnectivityManager) mainActivity.getSystemService(CONNECTIVITY_SERVICE);
        checkConnection();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (connectivityManager != null) {
            checkConnection();
        }
    }

    private void checkConnection() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getState() ==  NetworkInfo.State.CONNECTED) {
            // Connected
            mainActivity.setNetworkConnected(true);
        } else {
            // No connect
            mainActivity.setNetworkConnected(false);
        }
    }
}
