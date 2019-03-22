package com.uninorte.edu.co.tracku;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        map = (MapView) findViewById(R.id.dh_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        mapController = map.getController();
        mapController.setZoom(9.5);


        this.checkForDatabase();
        this.uloc = new GPSlocation();

        String callType = getIntent().getStringExtra("callType");
        String date;
        String hour;
        if (callType.equals("Date_no_Hour")) {
            //Obtengo fecha pero no una hora
            date = getIntent().getStringExtra("Date");
            ((TextView) findViewById(R.id.q_tv)).setText("User in date: " + date + "");
            this.getUsersByDate(date);

        } else if (callType.equals("Hour_no_Date")) {
            //Obtengo la hora, pero no la fecha
            hour = getIntent().getStringExtra("Hour");
            ((TextView) findViewById(R.id.q_tv)).setText("User in date: " + hour + "");
            this.getUsersByHour(hour);
        }


    }


    /**
     * Obtengo la localización de todos los usuarios en fecha específica
     *
     * @param date
     */
    private void getUsersByDate(String date) {
        List<GPSlocation> allLoct = MainMenuAct.INSTANCE.locationDao().getUsersByDate(date);

        for (GPSlocation ul : allLoct) {
            MainMenuAct.INSTANCE.userDao().getUserById(ul.userId);//Obtengo el usuario
            this.showLocation(MainMenuAct.INSTANCE.userDao().getUserById(ul.userId),
                    ul.latitude, ul.longitude);
        }
    }

    /**
     * Obtengo la localización de todos los usuarios en una hora específica
     *
     * @param hour
     */
    private void getUsersByHour(String hour) {
        List<GPSlocation> allLoct = MainMenuAct.INSTANCE.locationDao().getUserByHour(hour);

        for (GPSlocation ul : allLoct) {
            MainMenuAct.INSTANCE.userDao().getUserById(ul.userId);//Obtengo el usuario
            this.showLocation(MainMenuAct.INSTANCE.userDao().getUserById(ul.userId),
                    ul.latitude, ul.longitude);
        }
    }

    private void showLocation(User user, double lat, double lon) {

        GeoPoint newCenter = new GeoPoint(lat, lon);
        mapController.setCenter(newCenter);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(newCenter);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle(user.fname + " was here");
        startMarker.setSnippet("");
        startMarker.setSubDescription("");
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
