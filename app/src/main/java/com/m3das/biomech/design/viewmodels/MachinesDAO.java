package com.m3das.biomech.design.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.m3das.biomech.design.Machines;

import java.util.List;

@Dao
public interface MachinesDAO {

    @Insert
    void insertMachine(Machines machines);

    @Update
    void updateMachine(Machines machines);

    @Delete
    void deleteMachine(Machines machines);

    @Query("SELECT * FROM Machines ORDER BY id DESC")
    LiveData<List<Machines>> getAllMachines();

    @Query("SELECT id,machine_qrcode FROM Machines ORDER BY id DESC")
    LiveData<List<Machines>> getListofMachines();

}
