package com.uninorte.edu.co.tracku.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public int userId;

    @ColumnInfo(name = "FirstName")
    public String fname;

    @ColumnInfo(name = "LastName")
    public String lname;

    @ColumnInfo(name="email")
    public String email;

    @ColumnInfo(name="password_hash")
    public String passwordHash;
}
