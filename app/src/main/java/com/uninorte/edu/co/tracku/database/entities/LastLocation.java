package com.uninorte.edu.co.tracku.database.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class LastLocation {

    @PrimaryKey(autoGenerate = true)
    public int idLl;

    @ColumnInfo(name = "User ID")
    public int userId;

    @ColumnInfo(name = "Last Latitude")
    public double lastLatitude;

    @ColumnInfo(name = "Last Longitude")
    public double lastLongitude;

    @ColumnInfo(name = "Active")
    public int active;
}
