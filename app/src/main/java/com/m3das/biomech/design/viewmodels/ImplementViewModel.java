package com.m3das.biomech.design.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.m3das.biomech.design.Machines;
import com.m3das.biomech.design.MachinesRepository;

import java.util.List;

public class ImplementViewModel extends AndroidViewModel {
    public MachinesRepository repository;
    public LiveData<List<Machines>> allMachines, listOfMachines;
    public ImplementViewModel(@NonNull Application application) {
        super(application);
        repository = new MachinesRepository(application);
        listOfMachines = repository.getListOfMachines();
    }
    public LiveData<List<Machines>> getListOfMachines(){
    return listOfMachines;
    }
}