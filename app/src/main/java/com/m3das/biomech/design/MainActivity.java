package com.m3das.biomech.design;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private TabItem tabMap, tabInfo, tabMachines, tabData, tabQR;
    private ViewPager viewPager;
    private TabLayout tabLayoutMain;
    public PageAdapter pagerAdapter;
    private ImageView cropSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        tryThis();

        String cropType;
        if (Variable.getCrop() == 1) {
            cropSelected.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.corn));
            cropType = "Corn";
        } else if (Variable.getCrop() == 2) {
            cropSelected.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rice));
            cropType = "Rice";
        } else {
            cropSelected.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sugarcane));
            cropType = "Sugarcane";
        }

        cropSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Your selected crop is " + cropType, Toast.LENGTH_SHORT).show();
            }
        });

        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayoutMain.getTabCount());
        viewPager.setAdapter(pagerAdapter);


        tabLayoutMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutMain));


    }

    private void tryThis() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.m3das_logo_copy);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgInBye = byteArrayOutputStream.toByteArray();
        String dummyImg = Base64.encodeToString(imgInBye, Base64.DEFAULT);
        Variable.setDummyImg(dummyImg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void initViews() {

        tabMap = findViewById(R.id.tabMap);
        tabInfo = findViewById(R.id.tabInfo);
        tabMachines = findViewById(R.id.tabMachines);
        tabData = findViewById(R.id.tabData);
        tabQR = findViewById(R.id.tabImplements);
        tabLayoutMain = findViewById(R.id.tabLayoutMain);
        viewPager = findViewById(R.id.viewpager);
        cropSelected = findViewById(R.id.imgCropSelected);
    }

}