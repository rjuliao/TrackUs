package com.uninorte.edu.co.tracku;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.uninorte.edu.co.tracku.database.entities.GPSlocation;
import com.uninorte.edu.co.tracku.database.entities.User;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

public class DateHourLoc extends AppCompatActivity {

    IMapController mapController;
    private MapView map;
    private GPSlocation uloc;

    private void checkForDatabase() {
        if (MainActivity.INSTANCE == null) {
            MainActivity.getDatabase(this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        setContentView(R.layout.activity_date_hour_loc);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = findViewById(R.id.dh_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        mapController = map.getController();
        mapController.setZoom(9.5);


        this.checkForDatabase();
        this.uloc = new GPSlocation();

        String callType = getIntent().getStringExtra("callType");
        String date;
        String date1;
        String hour;
        String hour1;
        int id;
        if (callType.equals("Date_no_Hour")) {
            //Obtengo fecha pero no una hora
            date = getIntent().getStringExtra("Date 1");
            date1 = getIntent().getStringExtra("Date 2");
            id = Integer.parseInt(getIntent().getStringExtra("id"));
            ((TextView) findViewById(R.id.q_tv)).setText("User between date: " + date +
                    " and "+ date1);
            this.getUsersByDate(id, date, date1);

        } else if (callType.equals("Hour_no_Date")) {
            //Obtengo la hora, pero no la fecha
            hour = getIntent().getStringExtra("Hour 1");
            hour1 = getIntent().getStringExtra("Hour 2");
            id = Integer.parseInt(getIntent().getStringExtra("id"));

            ((TextView) findViewById(R.id.q_tv)).setText("User between date: " + hour +
                    " and "+ hour1);
            this.getUsersByHour(id, hour, hour1);
        }


    }


    /**
     * Obtengo la localización de todos los usuarios en fecha específica
     *
     * @param date
     */
    private void getUsersByDate(int id, String date, String date1) {
        List<GPSlocation> allLoct = MainMenuAct.INSTANCE.
                locationDao().getUsersByDate(id, date, date1);
        User u = MainMenuAct.INSTANCE.userDao().getUserById(id);

        for (GPSlocation ul : allLoct) {
            this.showLocation( u, ul.latitude, ul.longitude, ul.hour, ul.date);
        }
    }

    /**
     * Obtengo la localización de todos los usuarios en una hora específica
     *
     * @param hour
     */
    private void getUsersByHour(int id, String hour, String hour1) {
        List<GPSlocation> allLoct = MainMenuAct.INSTANCE.
                locationDao().getUserByHour(id, hour, hour1);
        User u = MainMenuAct.INSTANCE.userDao().getUserById(id);
        for (GPSlocation ul : allLoct) {

            this.showLocation(u, ul.latitude, ul.longitude, ul.hour, ul.date);
        }
    }

    private void showLocation(User user, double lat, double lon, String h, String d) {

        GeoPoint newCenter = new GeoPoint(lat, lon);
        mapController.setCenter(newCenter);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(newCenter);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle(user.fname + " "+ user.lname);
        startMarker.setSnippet("Last location");
        startMarker.setSubDescription("In date: " + d+" at " + h );
        this.map.getOverlays().add(startMarker);
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

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
