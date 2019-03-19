package com.uninorte.edu.co.tracku.database.core;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.uninorte.edu.co.tracku.database.daos.GpsDao;
import com.uninorte.edu.co.tracku.database.daos.UserDao;
import com.uninorte.edu.co.tracku.database.entities.GPSlocation;
import com.uninorte.edu.co.tracku.database.entities.LastLocation;
import com.uninorte.edu.co.tracku.database.entities.User;

@Database(entities = {User.class, GPSlocation.class, LastLocation.class},version = 6)
public abstract class TrackUDatabaseManager extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract GpsDao locationDao();
}