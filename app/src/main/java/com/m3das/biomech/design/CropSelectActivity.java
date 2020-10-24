package com.m3das.biomech.design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CropSelectActivity extends AppCompatActivity {
    public int cropSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_select_activity);
        setTitle("Crop Selection");
        Button corn, rice, sugarcane;

        corn = findViewById(R.id.btnCorn);
        rice = findViewById(R.id.btnRice);
        sugarcane = findViewById(R.id.btnSugarCane);

        corn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                cropSelected = 1;
            }
        });
        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                cropSelected = 2;
            }
        });
        sugarcane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                cropSelected = 3;
            }
        });

    }
}