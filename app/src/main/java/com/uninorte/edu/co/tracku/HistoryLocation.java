package com.uninorte.edu.co.tracku;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import com.uninorte.edu.co.tracku.database.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class HistoryLocation extends AppCompatActivity implements View.OnClickListener{

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    public final Calendar c = Calendar.getInstance();

    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    final int year = c.get(Calendar.YEAR);
    final int hour = c.get(Calendar.HOUR_OF_DAY);
    final int minute = c.get(Calendar.MINUTE);

    EditText etDate;
    ImageButton ibGetDate;
    EditText etHour;
    ImageButton ibGetHour;


    /**
     *
     */
    private void checkForDatabase() {
        if (MainActivity.INSTANCE == null) {
            MainActivity.getDatabase(this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_location);
        checkForDatabase();


        etDate = (EditText) findViewById(R.id.show_date_picker);
        ibGetDate = (ImageButton) findViewById(R.id.ib_get_date);

        etHour = (EditText) findViewById(R.id.show_hour_picker);
        ibGetHour = (ImageButton) findViewById(R.id.ib_get_hour);

        ibGetDate.setOnClickListener(this);
        ibGetHour.setOnClickListener(this);
    }




    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_get_date:
                getDate();
                break;
            case R.id.ib_get_hour:
                getHour();
                break;
        }
    }

    private void getDate(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String dayFormat = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String monthFormat = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);

                etDate.setText(dayFormat + BARRA + monthFormat + BARRA + year);


            }

        },year, month, day);
        recogerFecha.show();

    }

    private void getHour(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hourFormat =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minuteFormat = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);

                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                etHour.setText(hourFormat + DOS_PUNTOS + minuteFormat + " " + AM_PM);
            }

        }, hour, minute, false);

        recogerHora.show();
    }
}
