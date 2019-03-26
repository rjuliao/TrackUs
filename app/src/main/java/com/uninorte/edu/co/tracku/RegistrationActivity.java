package com.uninorte.edu.co.tracku;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.uninorte.edu.co.tracku.networking.WebServiceManager;
import com.uninorte.edu.co.tracku.networking.WebServiceManagerInterface;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.uninorte.edu.co.tracku.networking.WebServiceManager.CallWebServiceOperation;

public class RegistrationActivity extends Activity implements View.OnClickListener, WebServiceManagerInterface {
    String fname = "";
    String lname = "";
    String userName="";
    String password="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);
        /*
        findViewById(R.id.reg_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentToBeCalled=new Intent();
                fname = ((EditText)findViewById(R.id.reg_fname_value)).getText()+"";
                lname = ((EditText)findViewById(R.id.reg_lname_value)).getText()+"";
                userName=((EditText)findViewById(R.id.reg_username_value)).getText()+"";
                password=((EditText)findViewById(R.id.reg_password_value)).getText()+"";
                String passwordConfirmation=((EditText)findViewById(R.id.reg_password_confirmation_value)).getText()+"";

                if(password.equals(passwordConfirmation)) {
                    intentToBeCalled.putExtra("callType", "userRegistration");
                    intentToBeCalled.putExtra("fName",fname);
                    intentToBeCalled.putExtra("lName",lname);
                    intentToBeCalled.putExtra("userName", userName);
                    intentToBeCalled.putExtra("password", password);
                    intentToBeCalled.setClass(getApplicationContext(), MainMenuAct.class);
                    startActivity(intentToBeCalled);

                }

            }
        });*/


    }
    public void empanada(View view){
        Intent intentToBeCalled=new Intent();
        fname = ((EditText)findViewById(R.id.reg_fname_value)).getText()+"";
        lname = ((EditText)findViewById(R.id.reg_lname_value)).getText()+"";
        userName=((EditText)findViewById(R.id.reg_username_value)).getText()+"";
        password=((EditText)findViewById(R.id.reg_password_value)).getText()+"";
        String passwordConfirmation=((EditText)findViewById(R.id.reg_password_confirmation_value)).getText()+"";
        
        WebServiceManager.CallWebServiceOperation(this,
                "http://192.168.1.5:8080/WebServices/webresources/web.user/insert/"
                        +fname +"/"+lname+"/"+userName +"/"+password,
                "Registro",getApplicationContext());
        System.out.println(":c");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((EditText)findViewById(R.id.reg_fname_value)).setText("");
        ((EditText)findViewById(R.id.reg_lname_value)).setText("");
        ((EditText)findViewById(R.id.reg_username_value)).setText("");
        ((EditText)findViewById(R.id.reg_password_value)).setText("");
        ((EditText)findViewById(R.id.reg_password_confirmation_value)).setText("");
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
        Toast.makeText(this,"Iniciando registro", Toast.LENGTH_SHORT).show();

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
}
