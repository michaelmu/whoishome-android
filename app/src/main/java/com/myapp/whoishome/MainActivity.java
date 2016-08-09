package com.myapp.whoishome;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Device> devices = null;

    ListView mainListView;
    Button Btngetdata;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Btngetdata = (Button) findViewById(R.id.getdata);
        Btngetdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new LoadDevices().execute();
            }
        });
    }

    protected class LoadDevices extends AsyncTask<String, Void, List<Device>>{
        @Override
        protected List<Device> doInBackground(String... args){
            DeviceParser parser = new DeviceParser();
            devices = parser.fetchDevices();
            return devices;
        }

        @Override
        protected void onPostExecute(List<Device> devices){
            // Locate mainListView in activity_list_view.xml
            mainListView = (ListView)findViewById(R.id.mainListView);

            ArrayAdapter<Device> adapter = new ArrayAdapter<Device>(getBaseContext(), R.layout.list_item, devices);
            mainListView.setAdapter(adapter);
        }
    }

}