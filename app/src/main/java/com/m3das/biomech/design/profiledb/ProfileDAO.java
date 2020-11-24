package com.m3das.biomech.design.profiledb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProfileDAO {

    @Insert
    void insertProfile(Profile profile);

    @Update
    void updateProfile(Profile profile);

    @Delete
    void deleteProfile(Profile profile);

    @Query("SELECT * FROM respondent_profile ORDER BY id DESC")
    LiveData<List<Profile>> getAllProfiles();

    @Query("DELETE FROM respondent_profile")
    void deleteAllProfiles();

}
