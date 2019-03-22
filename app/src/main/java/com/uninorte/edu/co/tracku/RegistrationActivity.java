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
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationActivity extends Activity implements View.OnClickListener, WebServiceManagerInterface {
    String fname = "";
    String lname = "";
    String userName="";
    String password="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);
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
                //consumirServicio r= new consumirServicio();
                //r.execute();

            }
        });

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
    public boolean registrarWS(){

        String datos= fname+"/"+lname+"/"+userName+"/"+password;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://localhost:8080/WServices/webresources/web.user/insert");
        //post.setHeader("content-type", "application/json");
        try
        {
            //Construimos el objeto cliente en formato JSON
            JSONObject dato = new JSONObject();

            dato.put("firstName", fname);
            dato.put("lastName", lname);
            dato.put("email",userName);
            dato.put("password",password);

            StringEntity entity = new StringEntity(dato.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            //resultado = EntityUtils.toString(resp.getEntity());

        }
        catch(Exception ex) {
            return false;
        }

        return true;
    }




    @Override
    public void WebServiceMessageReceived(String userState, String message) {

    }


    private class consumirServicio extends AsyncTask<Void, Integer, Void>{
        private int progreso;

        @Override
        protected Void doInBackground(Void... voids) {
            registrarWS();
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            findViewById(R.id.reg_button).setClickable(true);
            Toast.makeText(RegistrationActivity.this,"Registro exitoso", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onPreExecute(){
            progreso=0;
            findViewById(R.id.reg_button).setClickable(false);

        }
        @Override
        protected void onProgressUpdate(Integer... values){


        }

    }
}
