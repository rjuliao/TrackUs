package com.uninorte.edu.co.tracku.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.widget.ListView;

import com.uninorte.edu.co.tracku.database.entities.LastLocation;

import java.util.List;

@Dao
public interface LastLocationDao {

    @Query("SELECT * FROM LastLocation WHERE UserID = :userId")
    List<LastLocation> getUserLastLocation(int userId);


    @Insert
    void insertLocation(LastLocation lloc);

    @Delete
    void deleteLocation(LastLocation lloc);
}
