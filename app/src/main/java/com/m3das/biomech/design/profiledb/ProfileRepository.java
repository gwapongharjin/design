package com.m3das.biomech.design.profiledb;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.m3das.biomech.design.machinedb.MachinesDAO;
import com.m3das.biomech.design.machinedb.MachinesRepository;

import java.util.List;

public class ProfileRepository {
    private final ProfileDAO profileDAO;
    private final LiveData<List<Profile>> allProfileNames;

    public ProfileRepository(Application application) {
        ProfileDatabase database = ProfileDatabase.getInstance(application);
        profileDAO = database.profileDAO();
        allProfileNames = profileDAO.getAllProfiles();
    }

    public void insert(Profile profile) {
        new ProfileRepository.InsertProfileAsyncTask(profileDAO).execute(profile);
    }

    public void update(Profile profile) {
        new ProfileRepository.UpdateProfileAsyncTask(profileDAO).execute(profile);
    }

    public void delete(Profile profile) {
        new ProfileRepository.DeleteProfileAsyncTask(profileDAO).execute(profile);
    }

    public void deleteAllProfiles() {
        new DeleteAllProfilesAsyncTask(profileDAO).execute();
    }

    public LiveData<List<Profile>> getAllUserNames(){
        return allProfileNames;
    }


    public static class InsertProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
        private final ProfileDAO profileDAO;

        private InsertProfileAsyncTask(ProfileDAO profileDAO) {
            this.profileDAO = profileDAO;
        }

        @Override
        protected Void doInBackground(Profile... profile) {
            profileDAO.insertProfile(profile[0]);
            return null;
        }
    }

    public static class UpdateProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
        private final ProfileDAO profileDAO;

        private UpdateProfileAsyncTask(ProfileDAO profileDAO) {
            this.profileDAO = profileDAO;
        }

        @Override
        protected Void doInBackground(Profile... profile) {
            profileDAO.updateProfile(profile[0]);
            return null;
        }
    }

    public static class DeleteProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
        private final ProfileDAO profileDAO;

        private DeleteProfileAsyncTask(ProfileDAO profileDAO) {
            this.profileDAO = profileDAO;
        }

        @Override
        protected Void doInBackground(Profile... profile) {
            profileDAO.deleteProfile(profile[0]);
            return null;
        }
    }

    private static class DeleteAllProfilesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProfileDAO profileDAO;

        private DeleteAllProfilesAsyncTask(ProfileDAO profileDAO) {
            this.profileDAO = profileDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            profileDAO.deleteAllProfiles();
            return null;
        }
    }
}
