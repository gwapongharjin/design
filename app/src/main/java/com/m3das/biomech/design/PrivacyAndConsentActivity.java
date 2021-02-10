package com.m3das.biomech.design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class PrivacyAndConsentActivity extends AppCompatActivity {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitByBackKey() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.privacy_and_consent_activity);
        CustomScroll customScroll = findViewById(R.id.svPrivacyAndConsent);
        Button btnProceed = findViewById(R.id.btnProceed);

        btnProceed.setEnabled(false);

        customScroll.setOnBottomReachedListener(new CustomScroll.OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                btnProceed.setEnabled(true);
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}