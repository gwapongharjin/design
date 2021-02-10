package com.m3das.biomech.design.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.machinedb.MachinesRepository;
import com.m3das.biomech.design.profiledb.Profile;
import com.m3das.biomech.design.profiledb.ProfileRepository;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    public ProfileRepository repositoryProfile;
    public LiveData<List<Profile>> allProfileNames;


    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repositoryProfile = new ProfileRepository(application);
        allProfileNames = repositoryProfile.getAllUserNames();
    }

    public void insert(Profile profile) {
        repositoryProfile.insert(profile);
    }

    public void update(Profile profile) {
        repositoryProfile.update(profile);
    }

    public void delete(Profile profile) {
        repositoryProfile.delete(profile);
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return allProfileNames;
    }

    public void deleteAll(){repositoryProfile.deleteAllProfiles();}

    public void nukeAll(){repositoryProfile.nukeAll();}
}