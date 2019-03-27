package com.uninorte.edu.co.tracku;

import android.Manifest;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.uninorte.edu.co.tracku.com.uninorte.edu.co.tracku.gps.GPSManager;
import com.uninorte.edu.co.tracku.com.uninorte.edu.co.tracku.gps.GPSManagerInterface;
import com.uninorte.edu.co.tracku.database.core.TrackUDatabaseManager;
import com.uninorte.edu.co.tracku.database.entities.GPSlocation;
import com.uninorte.edu.co.tracku.database.entities.User;
import com.uninorte.edu.co.tracku.networking.WebServiceManager;
import com.uninorte.edu.co.tracku.networking.WebServiceManagerInterface;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.uninorte.edu.co.tracku.MainActivity.getDatabase;

public class OsmActivity extends AppCompatActivity
        implements
        GPSManagerInterface, WebServiceManagerInterface, Runnable {

    Activity thisActivity = this;
    GPSManager gpsManager;
    double latitude;
    double longitude;
    //static TrackUDatabaseManager INSTANCE;
    MapView map;
    //static User user;

    private void checkForDatabase() {
        if (MainActivity.INSTANCE == null) {
            MainActivity.getDatabase(this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        setContentView(R.layout.activity_osm);
        checkPermissions();
        checkForDatabase();


        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = findViewById(R.id.oms_map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        new Thread(new OsmActivity()).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        map.onResume();

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        MyLocationNewOverlay myLocationNewOverlay =
                new MyLocationNewOverlay(
                        new GpsMyLocationProvider(this), map);
        myLocationNewOverlay.enableMyLocation();
        this.map.getOverlays().add(myLocationNewOverlay);
    }

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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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

    /**
     * Recibimos la posición de un usuario
     * @param latitude
     * @param longitude
     */
    @Override
    public void LocationReceived(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
        ((TextView) findViewById(R.id.lat_val)).setText(latitude + "");
        ((TextView) findViewById(R.id.lon_val)).setText(longitude + "");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");


        /* TODO LO SIGUIENTE OCURRE CUANDO YA TENEMOS CONEXIÓN AL WS
         if(conexión al WS exite){

            this.verifyAll();

            WebServiceManager.CallWebServiceOperation(this,
                "http://10.20.16.80:8080/WebServices/webresources/web.gpslocation/insert/"
                        +MainMenuAct.user.userId +"/"+latitude+"/"+longitude +"/"+ dateFormat.format(new Date())
                +"/"+ hourFormat.format(new Date()), "SaveLocation",getApplicationContext());

            Aquí hacemos un INSERT en la tabla LastLocation, donde actualizamos el campo del usuario
            con la columna en activo

         }else{

            GPSlocation LCT = new GPSlocation();
            LCT.userId = MainMenuAct.user.userId;
            LCT.latitude = latitude;
            LCT.longitude = longitude;
            LCT.date = dateFormat.format(new Date());
            LCT.hour = hourFormat.format(new Date());
            LCT.sync = false;
            MainMenuAct.INSTANCE.locationDao().insertLocation(LCT);
        }


         */

        this.setCenter(latitude, longitude,
                hourFormat.format(new Date()), dateFormat.format(new Date()),
                true, MainMenuAct.user);
    }

    /**
     * Verificamos que todas las posiciones que están como falsas en el base de datos local
     * se puedan subir al WS
     */
    private void verifyAll() {
        List<GPSlocation> gpSlocations = MainMenuAct.INSTANCE.locationDao().syncWS(false);
        for (GPSlocation gps : gpSlocations) {
            WebServiceManager.CallWebServiceOperation(this,
                    "http://10.20.16.80:8080/WebServices/webresources/web.gpslocation/insert/"
                            + gps.userId + "/" + gps.latitude + "/" + gps.longitude + "/" + gps.date
                            + "/" + gps.hour, "SaveLocation", getApplicationContext());
            MainMenuAct.INSTANCE.locationDao().deleteUser(gps);
        }
    }

    @Override
    public void GPSManagerException(Exception error) {

    }


    /**
     * Centra la posición del usuario en el mapa
     *
     * @param latitude
     * @param longitude
     */
    public void setCenter(double latitude, double longitude, String h, String d, boolean sw, User user) {
        IMapController mapController = map.getController();
        mapController.setZoom(9.5);
        GeoPoint newCenter = new GeoPoint(latitude, longitude);
        mapController.setCenter(newCenter);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(newCenter);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        if(!sw)//Si es falso, pone el label en rojo y esta inactivo
            startMarker.setTextLabelBackgroundColor(Color.parseColor("FF0000"));
        else//Si es verdadero, pone el label en verde y esta activo
            startMarker.setTextLabelBackgroundColor(Color.parseColor("008F39"));

        startMarker.setTitle(user.fname + " " + user.lname);
        startMarker.setSubDescription("In date: " + d + " at " + h);
        map.getOverlays().add(startMarker);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void WebServiceMessageReceived(String userState, String message) {

    }

    @Override
    public void WebServiceMessageReceived(String userState, JSONObject message) {

    }

    @Override
    public void WebServiceMessageReceived(String userState, JSONArray message) {

    }

    @Override
    public void run() {
        /*TODO LO SIGUIENTE OCURRE CUANDO EXISTE CONEXIÓN CON EL WS

        if(conexión al WS existe){
            Arraylist <- JSON que contiene la posición donde estan los usuarios conectados al WS
                            tabla LastLocation
            User user <- con el userID obtengo el nombre del usuario para mostrar en el icono de la posición
            Esta información la obtenemos con un delay
            dibujamos la posición del usuario -> conectado va en verde, rojo esta desconectado

            for(LastLocation gps: ArrayList){
                if(gps.desconectado){
                    setCenter(gps.latitude, gps.longitude, gps.date, gps.hour, user, false)
                }else{
                    setCenter(gps.latitude, gps.longitude, gps.date, gps.hour, user, true)
                }
            }

        }else{
            Toast -> No hay conexión con el WS
        }

         */
    }



}
