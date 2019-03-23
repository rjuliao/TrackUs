package com.uninorte.edu.co.tracku;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.uninorte.edu.co.tracku.database.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class HistoryLocation extends AppCompatActivity implements View.OnClickListener{

    private static final String CERO = "0";
    private static final String BARRA = "-";
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
    ListView users;

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

        users = (ListView) findViewById(R.id.users_lv);
        final ArrayList<String> usname =  showNames();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, usname);
        users.setAdapter(arrayAdapter);


        etDate = (EditText) findViewById(R.id.show_date_picker);
        ibGetDate = (ImageButton) findViewById(R.id.ib_get_date);

        etHour = (EditText) findViewById(R.id.show_hour_picker);
        ibGetHour = (ImageButton) findViewById(R.id.ib_get_hour);

        ibGetDate.setOnClickListener(this);
        ibGetHour.setOnClickListener(this);
        //((Button)findViewById(R.id.dh_btn_q)).setOnClickListener(this);

        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(HistoryLocation.this, "Click succesfull "+ i +
                        " "+ usname.get(i).toString(),Toast.LENGTH_SHORT ).show();
            }
        });

    }

    /**
     * Regresa el nombre del usuario para ponerlo en la lista
     * @return
     */
    private ArrayList<String> showNames(){
        ArrayList<String> usname = new ArrayList<>();
        List<User> users = MainMenuAct.INSTANCE.userDao().getAllUsers(); //Obtengo todos los usuarios de la base de datos.

        for (User u: users){
            usname.add(u.fname + " "+ u.lname);
        }
        return usname;
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

    /**
     * Comienzo una nueva actividad donde paso una fecha y/o
     * una hora para buscar personas en ese momento
     *
     */
    private void provideDateHour(){
        String dt = etDate.getText()+"";
        String hr = etHour.getText()+"";
        Intent dh_act = new Intent();

        //Si fecha y hora son nulos entonces muestra un mensaje de error
        if (dt.equals("") && hr.equals("") ){
            Toast.makeText(this,R.string.no_dh_msg,Toast.LENGTH_LONG).show();

            //Se escoge fecha, pero no una hora
        }else if (!(dt.equals("")) && hr.equals("")){
            dh_act.putExtra("callType", "Date_no_Hour");
            dh_act.putExtra("Date", dt);
            Toast.makeText(this,dt,Toast.LENGTH_LONG).show();
            dh_act.setClass(getApplicationContext(),DateHourLoc.class);
            startActivity(dh_act);

            //Se escoge hora, pero no fecha
        }else  if(dt.equals("") && !(hr.equals(""))){
            dh_act.putExtra("callType", "Hour_no_Date");
            dh_act.putExtra("Hour",hr);
            Toast.makeText(this,hr,Toast.LENGTH_LONG).show();
            dh_act.setClass(getApplicationContext(),DateHourLoc.class);
            startActivity(dh_act);

        }else{
            Toast.makeText(this,"KELLY PERO QUE MONDÁ",Toast.LENGTH_LONG).show();
        }

    }
    /**
     * Obtengo una fecha
     */
    private void getDate(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String dayFormat = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String monthFormat = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);

                etDate.setText(year + BARRA + monthFormat + BARRA + dayFormat);


            }

        },year, month, day);
        recogerFecha.show();

    }

    /**
     * Obtengo una hora
     */
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
