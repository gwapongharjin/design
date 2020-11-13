package com.m3das.biomech.design.implementdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Implements.class}, version = 1)
public abstract class ImplementDatabase extends RoomDatabase {
    private static ImplementDatabase instance;

    public abstract ImplementsDAO implementsDAO();

    public static synchronized ImplementDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ImplementDatabase.class, "implement")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return instance;
    }
}