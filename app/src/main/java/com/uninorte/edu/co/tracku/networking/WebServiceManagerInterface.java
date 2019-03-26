package com.uninorte.edu.co.tracku.networking;

import org.json.JSONArray;
import org.json.JSONObject;

public interface WebServiceManagerInterface {

    void WebServiceMessageReceived(String userState, String message);
    void WebServiceMessageReceived(String userState, JSONObject message);
    void WebServiceMessageReceived(String userState, JSONArray message);

}
