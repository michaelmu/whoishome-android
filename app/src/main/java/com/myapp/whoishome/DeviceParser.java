package com.myapp.whoishome;

import com.myapp.whoishome.Device;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;


public class DeviceParser {

    ArrayList<Device> devices = null;
    ArrayList<String> macExcludes = null;
    HashMap deviceNames = null;

    //static final String DEVICE_URL = "http://13fxml.com.s3-website-us-east-1.amazonaws.com/whoishome/devices.json";
    static final String SERVER_PARAMS_URL = "http://172.19.131.146:8080/server_params.json";
    static final String DEVICE_URL = "http://172.19.131.146:8080/devices.json";

    public DeviceParser() {
        // Setup the server params (excludes and name
        this.setupParams(SERVER_PARAMS_URL);
    }

    public ArrayList<Device> fetchDevices(){
        JsonFetcher jf = new JsonFetcher();
        return this.compileDevices(jf.fetch(DEVICE_URL));
    }

    private void setupParams(String paramsURL){
        JsonFetcher jf = new JsonFetcher();
        JSONObject jsonObj = null;
        ArrayList<String> bl = new ArrayList<String>();
        HashMap nm = new HashMap();
        try {
            jsonObj = new JSONObject(jf.fetch(paramsURL));

            // Add entries to the exclude list
            for (int i = 0; i < jsonObj.getJSONArray("excludes").length(); i++) {
                bl.add(jsonObj.getJSONArray("excludes").get(i).toString());
            }
            this.macExcludes = bl;

            Log.i("compileParams", "jsonObj:" + jsonObj.toString());
            // Add entries to the device name map
            for (int i = 0; i < jsonObj.getJSONArray("names").length(); i++) {
                JSONObject dObj = jsonObj.getJSONArray("names").getJSONObject(i);
                nm.put(dObj.get("mac").toString(), dObj.get("name").toString());
            }
            this.deviceNames = nm;

        } catch (JSONException e) {
            Log.e("compileParams", e.toString());
        }
    }


    private ArrayList<Device> compileDevices(String jsonResults){
        devices = new ArrayList<Device>();
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(jsonResults);
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject deviceObj = jsonArr.getJSONObject(i);
                String mac = deviceObj.get("mac").toString();
                if (!this.macExcludes.contains(mac)) {
                    // Only include devices whose mac isnt in the exclude list
                    String deviceNameUpdated = this.NameLookup(mac, deviceObj.get("name").toString());
                    String timeOnline = this.parseTimeOnline(deviceObj.get("lease_time_remaining").toString());
                    Device currDevice = new Device(
                        deviceObj.get("mac").toString(),
                        deviceObj.get("ip").toString(),
                        deviceNameUpdated,
                        timeOnline);
                devices.add(currDevice);
                }
            }
        } catch (JSONException e) {
            Log.e("JSONArray", e.toString());
        }
        return devices;
    }

    private String NameLookup(String mac, String defaultName){
        // if we can't find the mac address, just return the default name
        if (this.deviceNames.containsKey(mac)) {
            return this.deviceNames.get(mac).toString();
        }
        else if (defaultName.isEmpty()) {
            return "Unknown";
        }
        else {
            return defaultName;
        }
    }

    // Parse the lease time remaining and return the time online
    private String parseTimeOnline(String lease_time_remaining) {

        return "15mins";
    }
}
