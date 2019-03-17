package com.uninorte.edu.co.tracku.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.uninorte.edu.co.tracku.database.entities.GPSlocation;

import java.util.List;

@Dao
public interface GpsDao {

    @Query("select * from gpslocation where UserId =:id")
    List<GPSlocation> getUserById(int id);

    @Insert
    void insertLocation(GPSlocation loc);


    @Delete
    void deleteUser(GPSlocation loc);
}
