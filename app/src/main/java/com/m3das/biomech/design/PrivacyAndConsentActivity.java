package com.m3das.biomech.design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class PrivacyAndConsentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_and_consent_activity);
        CustomScroll customScroll = findViewById(R.id.svPrivacyAndConsent);
        Button btnProceed = findViewById(R.id.btnProceed);
        btnProceed.setEnabled(false);
        setTitle("Privacy and Consent");
        customScroll.setOnBottomReachedListener(new CustomScroll.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                btnProceed.setEnabled(true);
            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CropSelectActivity.class);
                startActivity(intent);
            }
        });

    }
}