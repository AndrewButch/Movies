package com.andrewbutch.movies.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkStatusWatcher extends BroadcastReceiver {
    private ConnectivityManager connectivityManager;
    private boolean isNetworkConnected;

    public NetworkStatusWatcher(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        checkConnection();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (connectivityManager != null) {
            checkConnection();
        }
    }

    public boolean isNetworkConnected() {
        return isNetworkConnected;
    }

    private void checkConnection() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getState() ==  NetworkInfo.State.CONNECTED) {
            isNetworkConnected = true;
        } else {
            isNetworkConnected = false;
        }
    }
}
