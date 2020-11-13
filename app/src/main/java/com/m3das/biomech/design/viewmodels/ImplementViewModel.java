package com.m3das.biomech.design.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.m3das.biomech.design.implementdb.Implements;
import com.m3das.biomech.design.implementdb.ImplementsRepository;
import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.machinedb.MachinesRepository;

import java.util.List;

public class ImplementViewModel extends AndroidViewModel {
    public ImplementsRepository repositoryImplements;
    public LiveData<List<Implements>> listOfImplements;

    public ImplementViewModel(@NonNull Application application) {
        super(application);
        repositoryImplements = new ImplementsRepository(application);
        listOfImplements = repositoryImplements.getAllImplements();
    }

    public LiveData<List<Implements>> getListOfMachines(){
        return listOfImplements;
    }
    public void insert(Implements imp) {
        repositoryImplements.insert(imp);
    }

    public void update(Implements imp) {
        repositoryImplements.update(imp);
    }

    public void delete(Implements imp) {
        repositoryImplements.delete(imp);
    }

    public LiveData<List<Implements>> getAllImplements() {
        return listOfImplements;
    }


}