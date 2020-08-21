package com.m3das.biomech.design;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.m3das.biomech.design.fragments.DataFragment;
import com.m3das.biomech.design.fragments.InfoFragment;
import com.m3das.biomech.design.fragments.MachinesFragment;
import com.m3das.biomech.design.fragments.MapFragment;
import com.m3das.biomech.design.fragments.QrCodeFragment;

public class PageAdapter extends FragmentPagerAdapter {
private int numTabs;

    public PageAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, numOfTabs);
        this.numTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MapFragment();
            case 1:
                return new InfoFragment();
            case 2:
                return new MachinesFragment();
            case 3:
                return new DataFragment();
            case 4:
                return new QrCodeFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
