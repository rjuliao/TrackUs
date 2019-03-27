package com.uninorte.edu.co.tracku.database.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class GPSlocation {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "UserId")
    public int userId;

    @ColumnInfo(name = "Latitude")
    public double latitude;

    @ColumnInfo(name = "Longitude")
    public double longitude;

    @ColumnInfo(name = "Date")
    public String date;

    @ColumnInfo(name = "Hour")
    public String hour;

    /**
     * Variable que es falsa si el usuario no esta conectado al WS
     * y es verdadera cuando se sube al WS
     */
    @ColumnInfo(name = "Sincronizar")
    public boolean sync;

}
