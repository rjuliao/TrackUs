package com.uninorte.edu.co.tracku;

import android.Manifest;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uninorte.edu.co.tracku.com.uninorte.edu.co.tracku.gps.GPSManager;
import com.uninorte.edu.co.tracku.com.uninorte.edu.co.tracku.gps.GPSManagerInterface;
import com.uninorte.edu.co.tracku.database.core.TrackUDatabaseManager;
import com.uninorte.edu.co.tracku.database.entities.GPSlocation;
import com.uninorte.edu.co.tracku.database.entities.User;
import com.uninorte.edu.co.tracku.networking.WebServiceManagerInterface;

import org.osmdroid.config.Configuration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainMenuAct extends AppCompatActivity
        implements GPSManagerInterface, WebServiceManagerInterface {

    Activity thisActivity = this;
    GPSManager gpsManager;
    double latitude;
    double longitude;

    static TrackUDatabaseManager INSTANCE;
    static User user;


    /**
     * Iniciamos ROOM DB
     *
     * @param context
     * @return
     */
    static TrackUDatabaseManager getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TrackUDatabaseManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            TrackUDatabaseManager.class, "database-OSM").
                            allowMainThreadQueries().fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Obtengo la información del usuario
     *
     * @param userName
     * @param password
     * @return
     */
    public boolean userAuth(String userName, String password) {
        try {
            List<User> usersFound = getDatabase(this).userDao().getUserByEmail(userName);
            if (usersFound.size() > 0) {
                if (usersFound.get(0).passwordHash.equals(md5(password))) {
                    user = new User();
                    user = usersFound.get(0);
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception error) {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * Guardo la información del usuario en Room
     *
     * @param fname
     * @param lname
     * @param userName
     * @param password
     * @return
     */
    public boolean userRegistration(String fname, String lname, String userName, String password) {
        try {
            user = new User();
            user.fname = fname;
            user.lname = lname;
            user.email = userName;
            user.passwordHash = md5(password);
            INSTANCE.userDao().insertUser(user);

        } catch (Exception error) {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        setContentView(R.layout.activity_main_menu);
        checkPermissions();
        getDatabase(this);


        String callType = getIntent().getStringExtra("callType");
        if (callType.equals("userLogin")) {
            String userName = getIntent().getStringExtra("userName");
            String password = getIntent().getStringExtra("password");

            if (!userAuth(userName, password)) {
                Toast.makeText(this, "User not found!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Welcome! ", Toast.LENGTH_LONG).show();
            }


        } else if (callType.equals("userRegistration")) {
            String fsname = getIntent().getStringExtra("fName");
            String lsname = getIntent().getStringExtra("lName");
            String userName = getIntent().getStringExtra("userName");
            String password = getIntent().getStringExtra("password");

            if (!userRegistration(fsname, lsname, userName, password)) {
                Toast.makeText(this, "Error while registering user!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "User registered!", Toast.LENGTH_LONG).show();
            }
        } else {
            finish();
        }

        ((TextView) findViewById(R.id.menu_msg)).setText(
                "Welcome" + " " + user.fname +
                        " " + user.lname + "!");


    }

    /**
     * Pedimos los permisos necesarios para la aplicación.
     */
    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                    "We need the GPS location to track U and other permissions, please grant all the permissions...");
            builder.setTitle("Permissions granting");
            builder.setPositiveButton(R.string.accept,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(thisActivity,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1227);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        } else {
            this.gpsManager = new GPSManager(this, this);
            gpsManager.InitLocationManager();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void LocationReceived(double latitude, double longitude) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1227) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(
                        "The permissions weren't granted, then the app will be close");
                builder.setTitle("Permissions granting");
                builder.setPositiveButton(R.string.accept,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                this.gpsManager = new GPSManager(this, this);
                gpsManager.InitLocationManager();
            }
        }
    }

    @Override
    public void GPSManagerException(Exception error) {

    }

    @Override
    public void WebServiceMessageReceived(String userState, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pressMyLocation(View view) {

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), OsmActivity.class);
        startActivity(intent);
    }


    public void pressToHistory(View view) {

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), HistoryLocation.class);
        startActivity(intent);
    }
}
