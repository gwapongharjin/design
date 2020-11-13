package com.m3das.biomech.design.implementdb;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;

public class ImplementsRepository {
    private final ImplementsDAO impDAO;
    private final LiveData<List<Implements>> allImplements;


    public ImplementsRepository(Application application) {
        ImplementDatabase database = ImplementDatabase.getInstance(application);
        impDAO = database.implementsDAO();
        allImplements = impDAO.getAllImplements();
    }

    public void insert(Implements imp) {
        new InsertImplementAsyncTask(impDAO).execute(imp);
    }

    public void update(Implements imp) {
        new UpdateImplementAsyncTask(impDAO).execute(imp);
    }

    public void delete(Implements imp) {
        new DeleteImplementAsyncTask(impDAO).execute(imp);
    }

    public LiveData<List<Implements>> getAllImplements() {
        return allImplements;
    }


    public static class InsertImplementAsyncTask extends AsyncTask<Implements, Void, Void> {
        private final ImplementsDAO impDAO;

        private InsertImplementAsyncTask(ImplementsDAO impDAO) {
            this.impDAO = impDAO;
        }

        @Override
        protected Void doInBackground(Implements... implement) {
            impDAO.insertImplement(implement[0]);
            return null;
        }
    }

    public static class UpdateImplementAsyncTask extends AsyncTask<Implements, Void, Void> {
        private final ImplementsDAO impDAO;

        private UpdateImplementAsyncTask(ImplementsDAO impDAO) {
            this.impDAO = impDAO;
        }

        @Override
        protected Void doInBackground(Implements... implement) {
            impDAO.updateImplement(implement[0]);
            return null;
        }
    }

    public static class DeleteImplementAsyncTask extends AsyncTask<Implements, Void, Void> {
        private final ImplementsDAO impDAO;

        private DeleteImplementAsyncTask(ImplementsDAO impDAO) {
            this.impDAO = impDAO;
        }

        @Override
        protected Void doInBackground(Implements... implement) {
            impDAO.deleteImplements(implement[0]);
            return null;
        }
    }
}
