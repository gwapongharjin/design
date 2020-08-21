package com.m3das.biomech.design.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m3das.biomech.design.R;
import com.m3das.biomech.design.viewmodels.QrCodeViewModel;

public class QrCodeFragment extends Fragment {

    private QrCodeViewModel mViewModel;

    public static QrCodeFragment newInstance() {
        return new QrCodeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.qr_code_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(QrCodeViewModel.class);
        // TODO: Use the ViewModel
    }

}