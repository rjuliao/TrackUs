package com.uninorte.edu.co.tracku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.uninorte.edu.co.tracku.networking.WebServiceManager;
import com.uninorte.edu.co.tracku.networking.WebServiceManagerInterface;

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

    }
    public void registrarWS(){
        try{
            WebServiceManager.CallWebServiceOperation(this, "http","POST","Payload", "Estado");
            try{
                URL url=new URL("");
                String methodType= "POST";
                String datos= fname+","+lname+","+userName+","+password;
                HttpURLConnection httpURLConnection= (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod(methodType);
                httpURLConnection.getOutputStream();
                BufferedWriter w= new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
                w.write(datos);w.flush();w.close();
                int responseCode=httpURLConnection.getResponseCode();
                if(responseCode==HttpURLConnection.HTTP_OK){
                    InputStream in=httpURLConnection.getInputStream();
                    StringBuffer stringBuffer=new StringBuffer();
                    int charIn=0;
                    while((charIn=in.read())!=-1){
                        stringBuffer.append((char)charIn);
                    }
                    String line= stringBuffer.toString();
                }else{
                    //local :v
                }

            }catch (Exception error){

            }
        }catch (Exception e){

        }
    }




    @Override
    public void WebServiceMessageReceived(String userState, String message) {

    }
}
