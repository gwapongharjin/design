package com.m3das.biomech.design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CropSelectActivity extends AppCompatActivity {
    public int cropSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_select_activity);
        setTitle("Crop Selection");
        Button corn, rice, sugarcane;
        ImageView cornImg, riceImg, sugarcaneImg;

        corn = findViewById(R.id.btnCorn);
        rice = findViewById(R.id.btnRice);
        sugarcane = findViewById(R.id.btnSugarCane);

        cornImg = findViewById(R.id.imgCorn);
        riceImg = findViewById(R.id.imgRice);
        sugarcaneImg = findViewById(R.id.imgSugar);

        cornImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.corn));
        riceImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rice));
        sugarcaneImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sugarcane));

        corn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                cropSelected = 1;
//                Variable.setCrop(cropSelected);
                Toast.makeText(getApplicationContext(),"This option is unavailable", Toast.LENGTH_SHORT).show();
            }
        });
        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                cropSelected = 2;
//                Variable.setCrop(cropSelected);
                Toast.makeText(getApplicationContext(),"This option is unavailable", Toast.LENGTH_SHORT).show();
            }
        });
        sugarcane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                cropSelected = 3;
//                Variable.setCrop(cropSelected);
            }
        });

    }
}