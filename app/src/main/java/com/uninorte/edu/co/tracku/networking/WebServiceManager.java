package com.uninorte.edu.co.tracku.networking;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceManager {
    private static RequestQueue mRequestQueue;

    public static void CallWebServiceOperation(final WebServiceManagerInterface caller,
                                               final String  webServiceURL,
                                               final String userState, final Context context){

        try{
            mRequestQueue= Volley.newRequestQueue(context);
            switch (userState){
                case "Registro"://post
                    requestJSONObject(caller,webServiceURL,context,userState,0);
                    break;
                case "Login"://get
                    requestJSONObject(caller,webServiceURL,context,userState,0);
                    break;
                case "Live"://get
                    requestJSONArrayObject(caller,webServiceURL,context,userState,0);
                    break;
                case "History"://get
                    requestJSONArrayObject(caller,webServiceURL,context,userState,0);
                    break;
                case "SaveLocation"://post
                    requestJSONObject(caller,webServiceURL,context,userState,1);
                    break;
            }

        }catch (Exception error){

        }
    }

    public static void requestJSONObject(final WebServiceManagerInterface caller, String webServiceUrl,
                                         final Context context, final String userstate, int methodType){

        JsonObjectRequest request= new JsonObjectRequest(methodType,webServiceUrl,
                null,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    caller.WebServiceMessageReceived(userstate,response);
                }

            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error:"+error, Toast.LENGTH_LONG).show();
                }
            });
        mRequestQueue.add(request);
        System.out.println(":C");
    }
    public static void requestJSONArrayObject(final WebServiceManagerInterface caller, String webServiceUrl
            , final Context context, final String userstate, int methodType){

        JsonArrayRequest request= new JsonArrayRequest(methodType,
                    webServiceUrl, null,new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    caller.WebServiceMessageReceived(userstate,response);
                }
                }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error:"+error, Toast.LENGTH_LONG).show();
                }
        });

        mRequestQueue.add(request);

    }


}
