package com.m3das.biomech.design;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.m3das.biomech.design.viewmodels.MachinesDAO;

@Database(entities = {Machines.class},version = 1)
public abstract class MachinesDatabase extends RoomDatabase {

    private static MachinesDatabase instance;
    public abstract MachinesDAO machinesDAO();

    public static synchronized MachinesDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MachinesDatabase.class, "machines")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }





}