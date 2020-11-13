package com.m3das.biomech.design.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.machinedb.MachinesRepository;
import com.m3das.biomech.design.profiledb.Profile;
import com.m3das.biomech.design.profiledb.ProfileRepository;

import java.util.List;

public class MachineListViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    public MachinesRepository repositoryMachines;
    public LiveData<List<Machines>> allMachines;

    public LiveData<List<Profile>> allProfiles;


    public MachineListViewModel(@NonNull Application application) {
        super(application);
        repositoryMachines = new MachinesRepository(application);
        allMachines = repositoryMachines.getAllMachines();

        ProfileRepository repositoryProfiles = new ProfileRepository(application);
        allProfiles = repositoryProfiles.getAllUserNames();

    }

    public void insert(Machines machines) {
        repositoryMachines.insert(machines);
    }

    public void update(Machines machines) {
        repositoryMachines.update(machines);
    }

    public void delete(Machines machines) {
        repositoryMachines.delete(machines);
    }

    public LiveData<List<Machines>> getAllMachines() {
        return allMachines;
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return allProfiles;
    }

}