package com.m3das.biomech.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class LauncherMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_main_activity);
        ConstraintLayout constraintLayout = findViewById(R.id.clLauncher);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrivacyAndConsentActivity.class);
                startActivity(intent);
            }
        });
        setTitle("M3DAS");
    }
}