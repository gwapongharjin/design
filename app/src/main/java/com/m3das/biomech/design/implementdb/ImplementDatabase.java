package com.m3das.biomech.design.implementdb;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.m3das.biomech.design.R;
import com.m3das.biomech.design.Variable;

import java.io.ByteArrayOutputStream;

@Database(entities = {Implements.class}, version = 3)
public abstract class ImplementDatabase extends RoomDatabase {
    private static ImplementDatabase instance;

    public abstract ImplementsDAO implementsDAO();

    public static synchronized ImplementDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ImplementDatabase.class, "implement")
                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
                    .build();

        }

        return instance;
    }
//
//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "implement" + "'");
////            new PopulateDbAsyncTask(instance).execute();
//        }
//    };
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        private ImplementsDAO implementsDAO;
//        private PopulateDbAsyncTask(ImplementDatabase db) {
//            implementsDAO = db.implementsDAO();
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//            implementsDAO.insertImplement(new Implements("implement type","R04BATI00021","12123123","R04BATM00038",
//                    "akjsnd","LAND CLEARING","PREPLANTING","PLANTING","FERTAPP","PESTAPP",
//                    "IRRI DRAIN","CULTIVATION","RATOONING","HARVESTING","POST HARVEST","HAULING","21",
//                    "23","127","5.5","20","21","",
//                    "","","","","",""
//                    ,"","","","","",""
//                    ,"","","","","","",
//                    "","","","","","",
//                    "","","","",
//                    "","","","","2001","BRAND NEW","GARAGE","LAGUNA","LOS BANOS"
//                    ,"BATONG MALAKE", Variable.getDummyImg(),"12.1212","141.123123","6.073628978267829"));
//
//            return null;
//        }
//    }
}