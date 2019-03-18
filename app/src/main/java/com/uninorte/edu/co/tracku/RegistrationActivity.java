package com.uninorte.edu.co.tracku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

public class RegistrationActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);

        findViewById(R.id.reg_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToBeCalled=new Intent();
                String fname = ((EditText)findViewById(R.id.reg_fname_value)).getText()+"";
                String lname = ((EditText)findViewById(R.id.reg_lname_value)).getText()+"";
                String userName=((EditText)findViewById(R.id.reg_username_value)).getText()+"";
                String password=((EditText)findViewById(R.id.reg_password_value)).getText()+"";
                String passwordConfirmation=((EditText)findViewById(R.id.reg_password_confirmation_value)).getText()+"";

                if(password.equals(passwordConfirmation)) {
                    intentToBeCalled.putExtra("callType", "userRegistration");
                    intentToBeCalled.putExtra("fName",fname);
                    intentToBeCalled.putExtra("lName",lname);
                    intentToBeCalled.putExtra("userName", userName);
                    intentToBeCalled.putExtra("password", password);
                    intentToBeCalled.setClass(getApplicationContext(), OsmActivity.class);
                    startActivity(intentToBeCalled);
                }
            }
        });

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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

    }
}
