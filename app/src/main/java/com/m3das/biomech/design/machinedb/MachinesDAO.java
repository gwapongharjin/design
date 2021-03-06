package com.m3das.biomech.design.machinedb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MachinesDAO {

    @Insert
    void insertMachine(Machines machines);

    @Update
    void updateMachine(Machines machines);

    @Delete
    void deleteMachine(Machines machines);

    @Query("SELECT * FROM machines ORDER BY id DESC")
    LiveData<List<Machines>> getAllMachines();

    @Query("DELETE FROM machines")
    void deleteAllMachines();
}
