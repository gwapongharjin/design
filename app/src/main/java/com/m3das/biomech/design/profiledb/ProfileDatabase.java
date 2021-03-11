package com.m3das.biomech.design.profiledb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Profile.class}, version = 2)
public abstract class ProfileDatabase extends RoomDatabase {


    private static ProfileDatabase instance;

    public abstract ProfileDAO profileDAO();

    public static synchronized ProfileDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ProfileDatabase.class, "profile")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }


}
