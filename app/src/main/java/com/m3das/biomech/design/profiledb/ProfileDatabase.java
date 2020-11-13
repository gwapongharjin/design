package com.m3das.biomech.design.profiledb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Profile.class},version = 1)
public abstract class ProfileDatabase extends RoomDatabase {


        private static ProfileDatabase instance;
        public abstract ProfileDAO profileDAO();

        public static synchronized ProfileDatabase getInstance(Context context){
            if (instance == null){
                instance = Room.databaseBuilder(context.getApplicationContext(), ProfileDatabase.class, "profile")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }

            return instance;
        }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProfileDAO profileDAO;
        private PopulateDbAsyncTask(ProfileDatabase db) {
            profileDAO = db.profileDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            profileDAO.insertProfile(new Profile("PABBLA110320150305","owner","ssa","farm",
                    "Harjinder Singh Pabla","U2 Ocampo Apts., Sierra Madre Extn., Umali Subd., Los Banos, Laguna", "24","Male","Mobile",
                    "090","","","","college"));
            profileDAO.insertProfile(new Profile("JONDOE110320150305","owner","ssa","farm",
                    "Jonathan B Doe","5/F B And L Building 116 Legaspi Street Legaspi Village, Makati City", "24","Male","Mobile",
                    "090","","","","college"));
            profileDAO.insertProfile(new Profile("NAVBLA110320150305","owner","ssa","farm",
                    "Nav Pabla","5/F B And L Building 116 Legaspi Street Legaspi Village, Makati City", "24","Male","Mobile",
                    "090","","","","college"));
            profileDAO.insertProfile(new Profile("EARGIE110320150305","owner","ssa","farm",
                    "Earl Lowigie","5/F B And L Building 116 Legaspi Street Legaspi Village, Makati City", "24","Male","Mobile",
                    "090","","","","college"));
            profileDAO.insertProfile(new Profile("LAWVIN110320150305","owner","ssa","farm",
                    "Lawrence Darvin","5/F B And L Building 116 Legaspi Street Legaspi Village, Makati City", "24","Male","Mobile",
                    "090","","","","college"));
            return null;
        }
    }


}
