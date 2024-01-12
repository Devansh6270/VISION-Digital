package com.vision_digital.internetConnectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;

import com.vision_digital.activities.SplashScreen;
import com.google.android.material.snackbar.Snackbar;

public class ConnectivityReciever extends BroadcastReceiver {

    public static ConnectivityRecieverListener connectivityRecieverListener;

    public ConnectivityReciever() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (connectivityRecieverListener != null) {
            connectivityRecieverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectivityRecieverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    public static Snackbar snackbar;

    public void showSnackbar(boolean isConnected, View layoutView, final Context context) {
        //isConnected is used to get connection status
        //layoutView is used to get view for snackbar
        //showConnected signify if we have to display snackbar when user connect to data
        String message = "";

        if (isConnected) {
            if (snackbar != null) {
                message = "Connected to Internet! Please Restart.";
                View sbView = snackbar.getView();
                TextView sbText = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                snackbar.setDuration(Snackbar.LENGTH_LONG);
                sbText.setTextColor(Color.WHITE);
                sbView.setBackgroundColor(Color.rgb(76, 175, 80));
                sbText.setText(message);
                snackbar.setAction("Quit", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent homeIntent = new Intent(context, SplashScreen.class);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(homeIntent);
                    }
                });
                snackbar.show();
            }
        } else {
            snackbar = Snackbar.make(layoutView, message, Snackbar.LENGTH_INDEFINITE);
            View sbView = snackbar.getView();
            TextView sbText = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);

            message = "Sorry! Not connected to Internet";
            sbText.setTextColor(Color.WHITE);
            sbView.setBackgroundColor(Color.rgb(244, 67, 54));
            snackbar.setText(message);
            snackbar.show();
        }
    }
}
