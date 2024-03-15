package com.vision_classes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.vision_classes.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    WebView policyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        policyView = findViewById(R.id.policyView);

        policyView.loadUrl("https://chalksnboard.com/privacypolicy");


    }
}
