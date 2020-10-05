package com.m3das.biomech.design;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.m3das.biomech.design.Machines;
import com.m3das.biomech.design.MachinesDatabase;
import com.m3das.biomech.design.viewmodels.MachinesDAO;

import java.util.List;

public class MachinesRepository {
    private MachinesDAO machinesDAO;
    private LiveData<List<Machines>> allMachines, listOfMachines;

    public MachinesRepository(Application application){
        MachinesDatabase database = MachinesDatabase.getInstance(application);
        machinesDAO = database.machinesDAO();
        allMachines = machinesDAO.getAllMachines();
        listOfMachines = machinesDAO.getListofMachines();
    }

    public void insert(Machines machines) {
        new InsertMachineAsyncTask(machinesDAO).execute(machines);
    }
    public void update(Machines machines) {
        new UpdateMachineAsyncTask(machinesDAO).execute(machines);
    }
    public void delete(Machines machines) {
        new DeleteMachineAsyncTask(machinesDAO).execute(machines);
    }

    public LiveData<List<Machines>> getAllMachines(){
        return allMachines;
    }

    public LiveData<List<Machines>> getListOfMachines(){
        return listOfMachines;
    }

    public static class InsertMachineAsyncTask extends AsyncTask<Machines, Void, Void>{
        private MachinesDAO machinesDAO;
        private InsertMachineAsyncTask(MachinesDAO machinesDAO){
            this.machinesDAO = machinesDAO;
        }

        @Override
        protected Void doInBackground(Machines... machines) {
            machinesDAO.insertMachine(machines[0]);
            return null;
        }
    }

    public static class UpdateMachineAsyncTask extends AsyncTask<Machines, Void, Void>{
        private MachinesDAO machinesDAO;
        private UpdateMachineAsyncTask(MachinesDAO machinesDAO){
            this.machinesDAO = machinesDAO;
        }

        @Override
        protected Void doInBackground(Machines... machines) {
            machinesDAO.updateMachine(machines[0]);
            return null;
        }
    }
    public static class DeleteMachineAsyncTask extends AsyncTask<Machines, Void, Void>{
        private MachinesDAO machinesDAO;
        private DeleteMachineAsyncTask(MachinesDAO machinesDAO){
            this.machinesDAO = machinesDAO;
        }

        @Override
        protected Void doInBackground(Machines... machines) {
            machinesDAO.deleteMachine(machines[0]);
            return null;
        }
    }

}
