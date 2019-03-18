package com.uninorte.edu.co.tracku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.uninorte.edu.co.tracku.database.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HistoryLocation extends AppCompatActivity {


    List<User> userList;

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

        final ListView listView = (ListView) findViewById(R.id.histo_list);
        String[] a = getAllNames(MainActivity.getDatabase(this).userDao().getAllUsers());

        List<String> userList1 = new ArrayList<String>(Arrays.asList(a));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, userList1);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(4000);
                view.startAnimation(animation1);
            }
        });
    }

    private String[] getAllNames(List<User> userList) {
        //this.userList = MainActivity.getDatabase(this).userDao().getAllUsers();
        String[] a = new String[userList.size()+1];
        for (int i = 0; i < userList.size(); i++){
            a[i] = userList.get(i).fname+ " " +userList.get(i).fname;
        }
        return a;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
