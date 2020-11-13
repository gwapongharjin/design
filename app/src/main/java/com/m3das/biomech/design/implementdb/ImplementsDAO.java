package com.m3das.biomech.design.implementdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;
@Dao
public interface ImplementsDAO {


        @Insert
        void insertImplement(Implements implement);

        @Update
        void updateImplement(Implements implement);

        @Delete
        void deleteImplements(Implements implement);

        @Query("SELECT * FROM implement ORDER BY id DESC")
        LiveData<List<Implements>> getAllImplements();


}
