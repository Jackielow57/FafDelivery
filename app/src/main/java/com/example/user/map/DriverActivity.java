package com.example.user.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity {
    public static final String TAG = "my.edu.tarc.lab44";
    private static String GET_URL = "https://faf-delivery.000webhostapp.com/getDriver.php";
    List<driverDetail> driverDetailList;
    private RequestQueue queue;
    private ProgressDialog pDialog;

    private TextView driverNameTextView;
    private TextView driverIDTextView;
    private TextView driverRatingTextView;
    private TextView driverContactTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        driverNameTextView=findViewById(R.id.DriverNameText);
        driverIDTextView=findViewById(R.id.DriverID);
        driverRatingTextView=findViewById(R.id.CurrentRatings);
        driverContactTextView=findViewById(R.id.DriverContact);

        driverDetailList = new ArrayList<>();
        pDialog = new ProgressDialog(this);

        downloadDriver(getApplicationContext(), GET_URL);
    }

    private void downloadDriver(Context context, String url) {
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
                            if (pDialog.isShowing())
                            {
                                pDialog.dismiss();
                                showDriverDetail();
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

    private void showDriverDetail()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("deliver",MODE_PRIVATE);
        String targetDriverName = sharedPreferences.getString("driverName","");
        for (int i=0;i<driverDetailList.size();i++)
        {
            if (driverDetailList.get(i).getDriverName().equals(targetDriverName))
            {
                driverNameTextView.setText(driverDetailList.get(i).getDriverName());
                driverIDTextView.setText(driverDetailList.get(i).getDriverID());
                driverRatingTextView.setText(driverDetailList.get(i).getDriverRating().toString());
                driverContactTextView.setText(driverDetailList.get(i).getDriverContact());
                break;
            }
        }
    }
}
