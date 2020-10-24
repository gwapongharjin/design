package com.m3das.biomech.design;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabItem tabMap, tabInfo, tabMachines, tabData, tabQR;
    private ViewPager viewPager;
    private TabLayout tabLayoutMain;
    public PageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    private void initViews(){

        tabMap = findViewById(R.id.tabMap);
        tabInfo = findViewById(R.id.tabInfo);
        tabMachines = findViewById(R.id.tabMachines);
        tabData = findViewById(R.id.tabData);
        tabQR = findViewById(R.id.tabImplements);
        tabLayoutMain = findViewById(R.id.tabLayoutMain);
        viewPager = findViewById(R.id.viewpager);
    }

}