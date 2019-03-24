package com.uninorte.edu.co.tracku.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.google.android.gms.wearable.internal.StorageInfoResponse;
import com.uninorte.edu.co.tracku.database.entities.GPSlocation;

import java.util.List;

@Dao
public interface GpsDao {

    @Query("SELECT * FROM gpslocation " +
            "WHERE UserId LIKE :id  AND Date BETWEEN :date AND :date1 ")
    List<GPSlocation> getUsersByDate(int id, String date, String date1);

    @Query("SELECT * FROM gpslocation " +
            "WHERE UserId LIKE :id AND Hour BETWEEN :hour AND :hour1 ")
    List<GPSlocation> getUserByHour(int id, String hour, String hour1);

    @Query("select * from gpslocation where UserId =:id")
    GPSlocation getUserById(int id);

    @Insert
    void insertLocation(GPSlocation loc);


    @Delete
    void deleteUser(GPSlocation loc);
}
