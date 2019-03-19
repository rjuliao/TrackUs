package com.uninorte.edu.co.tracku.database.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class LastLocation {

    @PrimaryKey(autoGenerate = true)
    public int idLl;

    @ColumnInfo(name = "UserID")
    public int userId;

    @ColumnInfo(name = "LastLatitude")
    public double lastLatitude;

    @ColumnInfo(name = "LastLongitude")
    public double lastLongitude;

    @ColumnInfo(name = "Active")
    public int active;
}
