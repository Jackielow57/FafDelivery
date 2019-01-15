package com.example.user.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TraceActivity extends AppCompatActivity {
    public static final String TAG = "my.edu.tarc.lab44";
    private static String GET_URL = "https://faf-delivery.000webhostapp.com/getItem.php";
    private static String GET_URL1 = "https://faf-delivery.000webhostapp.com/getTrack.php";
    private static String GET_URL2 = "https://faf-delivery.000webhostapp.com/getDriver.php";
    List<AddItemToData> itemList;
    List<AddTrackData>trackList;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private ProgressDialog pDialog1;
    private ProgressDialog pDialog2;
    public AddTrackData trackData;
    TextView track,item,driver,location,status;
    TableLayout trackTable;
    TableRow trackRow;
    List<driverDetail> driverDetailList;
    int randomDriver;
    public String Driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);

        trackTable=(TableLayout)findViewById(R.id.trackTable);
        trackTable.setColumnStretchable(0,true);
        trackTable.setColumnStretchable(1,true);
        trackTable.setColumnStretchable(2,true);
        trackTable.setColumnStretchable(3,true);
        trackTable.setColumnStretchable(4,true);
        itemList = new ArrayList<>();
        trackList=new ArrayList<>();
        driverDetailList=new ArrayList<>();
        pDialog = new ProgressDialog(this);
        pDialog1 = new ProgressDialog(this);
        pDialog2 = new ProgressDialog(this);
        downloadDriver(getApplicationContext(),GET_URL2);


        SharedPreferences sharedPreferences = getSharedPreferences("map",MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString("firstLocation","Kelana Jaya SS7");
        editor.putString("x1","3.100835");
        editor.putString("y1","101.598043");
        editor.putString("x2","3.14388");
        editor.putString("y2","101.693925");
        editor.putString("x3","3.19629");
        editor.putString("y3","101.743025");
        editor.apply();
    }

    public void makeServiceCall(Context context, String url, final AddTrackData trackData) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success==0) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("TrackingID",trackData.getTrackid());
                    params.put("ItemCode",trackData.getItemcode());
                    params.put("Status", trackData.getStatus());
                    params.put("DriverName",trackData.getDriver());
                    params.put("ItemLocation",trackData.getItemlocation());
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addTable(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        String icNumber = sharedPreferences.getString("icnumber","");
      for(int i=0;i<trackList.size();i++) {
          for(int j=0;j<itemList.size();j++){
              if(trackList.get(i).getItemcode().equals(itemList.get(j).getItemID())&&itemList.get(j).getIcNumber().equals(icNumber)){
              trackRow = new TableRow(this);
              track = new TextView(this);
              item = new TextView(this);
              driver = new TextView(this);
              location = new TextView(this);
              status = new TextView(this);
              track.setText(trackList.get(i).getTrackid());

              item.setText(trackList.get(i).getItemcode());

              driver.setText(trackList.get(i).getDriver());
              Driver=trackList.get(i).getDriver();
              location.setText(trackList.get(i).getItemlocation());

              status.setText(trackList.get(i).getStatus());

              trackRow.addView(track);
              trackRow.addView(item);
              trackRow.addView(driver);
              trackRow.addView(location);
              trackRow.addView(status);
              trackTable.addView(trackRow);
              }
          }

      }
    }
    public void addData(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        String icNumber = sharedPreferences.getString("icnumber","");
        boolean isExist = false;
        int random;

        randomDriver=new Random().nextInt(driverDetailList.size());
        for (int i =0;i<itemList.size();i++){
            if(itemList.get(i).getIcNumber().equals(icNumber)){
                for(int j = 0 ;  j <trackList.size();j++)
                {
                    if(trackList.get(j).getItemcode().equals(itemList.get(i).getItemID()))
                    {
                        isExist=true;
                        break;
                    }
                }
                if(isExist == false)
                {
                    trackData = new AddTrackData();
                    trackData.setTrackid(checkTrackingID());
                    trackData.setItemcode(itemList.get(i).getItemID());
                    random= new Random().nextInt(3);
                    if(random==0){
                        trackData.setItemlocation("Kelana Jaya");
                    }else if(random==1){
                        trackData.setItemlocation("HQ PosLaju");
                    }else if(random==2){
                        trackData.setItemlocation("Wangsa Maju");
                    }
                    trackData.setDriver(driverDetailList.get(randomDriver).getDriverName());
                    trackData.setStatus(itemList.get(i).getItemStatus());
                    makeServiceCall(this, "https://faf-delivery.000webhostapp.com/track.php", trackData);
                }
                else
                {
                    //Toast.makeText(this,"True",Toast.LENGTH_LONG).show();
                    isExist = false;
                }

            }
        }
        addTable();
    }

    public String checkTrackingID(){
        int random;
        random= new Random().nextInt(89999)+10000;
        String randomStringNumber = Integer.toString(random);
        for(int i=0;i<trackList.size();i++){
            if(randomStringNumber.equals(trackList.get(i).getTrackid()))
            {
                i=0;
                random= new Random().nextInt(89999)+10000;
                randomStringNumber = Integer.toString(random);
            }
        }
        return randomStringNumber;
    }

    private void downloadItem(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        if (!pDialog.isShowing())
            pDialog.setMessage("Syn with server...");
        pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            itemList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject ItemResponse = (JSONObject) response.get(i);
                                String icNumber = ItemResponse.getString("icNumber");
                                String itemID = ItemResponse.getString("itemID");
                                String itemName = ItemResponse.getString("itemName");
                                String itemDesp = ItemResponse.getString("itemDesp");
                                String itemStatus = ItemResponse.getString("itemStatus");
                                AddItemToData item = new AddItemToData(icNumber,itemID,itemName,itemDesp,itemStatus);
                                itemList.add(item);
                            }
                            if (pDialog.isShowing())
                            {
                                pDialog.dismiss();
                                addData();
                                //addTable();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    private void downloadTrack(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        if (!pDialog1.isShowing())
            pDialog1.setMessage("Syn with server...");
        pDialog1.show();

        JsonArrayRequest jsonObjectRequest2 = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            trackList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject ItemResponse = (JSONObject) response.get(i);
                                String trackid = ItemResponse.getString("trackid");
                                String itemcode = ItemResponse.getString("itemcode");
                                String status = ItemResponse.getString("status");
                                String driver = ItemResponse.getString("driver");
                                String itemlocation = ItemResponse.getString("itemlocation");
                                AddTrackData track= new AddTrackData(trackid,itemcode,driver,status,itemlocation);
                                trackList.add(track);
                            }
                            if (pDialog1.isShowing())
                            {
                                pDialog1.dismiss();
                                downloadItem(getApplicationContext(), GET_URL);
                                //checkTrackingID();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog1.isShowing())
                            pDialog1.dismiss();
                    }
                });

        // Set the tag on the request.
        jsonObjectRequest2.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest2);

    }

    public void gotoDriverDetail(View view)
    {
        //TextView buttonName = findViewById(R.id.showDriver);
        SharedPreferences sharedPreferences = getSharedPreferences("deliver",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("driverName",Driver);
        editor.apply();
        Intent intent = new Intent(this,DriverActivity.class);
        startActivity(intent);
    }

    public void gotoMap(View view)
    {
        Intent intent =  new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
    private void downloadDriver(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        if (!pDialog2.isShowing())
            pDialog2.setMessage("Syn with server...");
        pDialog2.show();

        JsonArrayRequest jsonObjectRequest3 = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            driverDetailList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject ItemResponse = (JSONObject) response.get(i);
                                String driverName = ItemResponse.getString("driverName");
                                String driverID = ItemResponse.getString("driverID");
                                String driverRating = ItemResponse.getString("driverRating");
                                String driverContact = ItemResponse.getString("driverContact");
                                driverDetail driver = new driverDetail(driverName,driverID,driverRating,driverContact);
                                driverDetailList.add(driver);
                            }
                            if (pDialog2.isShowing())
                            {
                                pDialog2.dismiss();
                                downloadTrack(getApplicationContext(),GET_URL1);
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog2.isShowing())
                            pDialog2.dismiss();
                    }
                });

        // Set the tag on the request.
        jsonObjectRequest3.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest3);

    }

}
