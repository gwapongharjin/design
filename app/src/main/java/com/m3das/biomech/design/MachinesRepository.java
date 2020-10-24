package com.m3das.biomech.design;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MachinesRepository {
    private final MachinesDAO machinesDAO;
    private final LiveData<List<Machines>> allMachines;
    private final LiveData<List<Machines>> listOfMachines, machineSpecificID;
    String idOfMachine;

    public MachinesRepository(Application application) {
        MachinesDatabase database = MachinesDatabase.getInstance(application);
        machinesDAO = database.machinesDAO();
        allMachines = machinesDAO.getAllMachines();
        listOfMachines = machinesDAO.getListofMachines();
        machineSpecificID = machinesDAO.getMachineID(idOfMachine);
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

    public LiveData<List<Machines>> getAllMachines() {
        return allMachines;
    }

    public LiveData<List<Machines>> getListOfMachines() {
        return listOfMachines;
    }

    public LiveData<List<Machines>> getMachineID(String getID){
        return machineSpecificID;
    }


    public static class InsertMachineAsyncTask extends AsyncTask<Machines, Void, Void> {
        private final MachinesDAO machinesDAO;

        private InsertMachineAsyncTask(MachinesDAO machinesDAO) {
            this.machinesDAO = machinesDAO;
        }

        @Override
        protected Void doInBackground(Machines... machines) {
            machinesDAO.insertMachine(machines[0]);
            return null;
        }
    }

    public static class UpdateMachineAsyncTask extends AsyncTask<Machines, Void, Void> {
        private final MachinesDAO machinesDAO;

        private UpdateMachineAsyncTask(MachinesDAO machinesDAO) {
            this.machinesDAO = machinesDAO;
        }

        @Override
        protected Void doInBackground(Machines... machines) {
            machinesDAO.updateMachine(machines[0]);
            return null;
        }
    }

    public static class DeleteMachineAsyncTask extends AsyncTask<Machines, Void, Void> {
        private final MachinesDAO machinesDAO;

        private DeleteMachineAsyncTask(MachinesDAO machinesDAO) {
            this.machinesDAO = machinesDAO;
        }

        @Override
        protected Void doInBackground(Machines... machines) {
            machinesDAO.deleteMachine(machines[0]);
            return null;
        }
    }

}
