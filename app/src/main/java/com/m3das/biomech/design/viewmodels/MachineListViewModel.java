package com.m3das.biomech.design.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.m3das.biomech.design.Machines;
import com.m3das.biomech.design.MachinesRepository;

import java.util.List;

public class MachineListViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    public MachinesRepository repository;
    public LiveData<List<Machines>> allMachines;
    private final LiveData<List<Machines>> listOfMachines, machinesID;
    String idOfMachine;

    public MachineListViewModel(@NonNull Application application) {
        super(application);
        repository = new MachinesRepository(application);
        allMachines = repository.getAllMachines();
        listOfMachines = repository.getListOfMachines();
        machinesID = repository.getMachineID(idOfMachine);
    }

    public void insert(Machines machines) {
        repository.insert(machines);
    }

    public void update(Machines machines) {
        repository.update(machines);
    }

    public void delete(Machines machines) {
        repository.delete(machines);
    }

    public LiveData<List<Machines>> getAllMachines() {
        return allMachines;
    }

    public LiveData<List<Machines>> getListOfMachines() {
        return listOfMachines;
    }

    public LiveData<List<Machines>> getMachineID(String getID){
        return machinesID;
    }

}