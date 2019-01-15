package com.example.user.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
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
import java.util.regex.Pattern;


public class AddDriver extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.lab44";
    private static String GET_URL = "https://faf-delivery.000webhostapp.com/getDriver.php";

    private EditText driverNameEdit;
    private EditText driverIDEdit;
    private EditText driverContactEdit;
    private EditText driverRatingEdit;

    private RequestQueue queue;
    private ProgressDialog pDialog;
    private List<driverDetail> driverList;
    public driverDetail driverInfo;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
        driverInfo = new driverDetail();
        driverList = new ArrayList<>();

        driverNameEdit = findViewById(R.id.driverNameEditView);
        driverIDEdit = findViewById(R.id.driverIDEditView);
        driverContactEdit = findViewById(R.id.driverContactEditView);
        driverRatingEdit = findViewById(R.id.driverRatingEditView);

        pDialog = new ProgressDialog(this);
        downloadDriver(getApplicationContext(), GET_URL);
    }

    public void addDriverOnClick(View view) {
        driverInfo.setDriverName(driverNameEdit.getText().toString());
        driverInfo.setDriverID(driverIDEdit.getText().toString());
        driverInfo.setDriverContact(driverContactEdit.getText().toString());
        driverInfo.setDriverRating(driverRatingEdit.getText().toString());

        if ((driverNameValidation(driverInfo.getDriverName()) == true
                && driverIDValidation(driverInfo.getDriverID()) == true
                && driverContactValidation(driverInfo.getDriverContact()) == true
                && driverRatingValidation(driverInfo.getDriverRating()) == true))
            try {
                //TODO: Please update the URL to point to your own server
                makeServiceCall(this, "https://faf-delivery.000webhostapp.com/driver.php", driverInfo);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

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
                            driverList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject ItemResponse = (JSONObject) response.get(i);
                                String driverName = ItemResponse.getString("driverName");
                                String driverID = ItemResponse.getString("driverID");
                                String driverRating = ItemResponse.getString("driverRating");
                                String driverContact = ItemResponse.getString("driverContact");
                                driverDetail driver = new driverDetail(driverName,driverID,driverRating,driverContact);
                                driverList.add(driver);
                            }
                            if (pDialog.isShowing())
                            {
                                pDialog.dismiss();

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


    public boolean driverNameValidation(String driverNameParameter)
    {
        if(driverNameParameter.isEmpty())
        {
            driverNameEdit.setError("Please Enter Driver Name");
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean driverIDValidation(String driverIDParameter)
    {
        for (int i = 0 ; i < driverList.size(); i ++)
        {
            if (driverIDParameter.equals(driverList.get(i).getDriverID()))
            {
                driverIDEdit.setError("Driver ID is already Existed");
                return false;
            }
        }

        if(driverIDParameter.isEmpty())
        {
            driverIDEdit.setError("Please Enter Driver ID");
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean driverContactValidation(String driverContactParameter)
    {
        if(driverContactParameter.isEmpty())
        {
            driverContactEdit.setError("Please Enter Driver Contact");
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean driverRatingValidation(String driverRatingParameter)
    {
        if(driverRatingParameter.isEmpty())
        {
            driverRatingEdit.setError("Please Enter Driver Rating");
            return false;
        }
        else
        {
            return true;
        }
    }

    public void makeServiceCall(Context context, String url, final driverDetail driverInfo) {
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
                                    finish();
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
                    params.put("DriverName",driverInfo.getDriverName());
                    params.put("DriverID",driverInfo.getDriverID());
                    params.put("DriverRating",driverInfo.getDriverRating());
                    params.put("DriverContact", driverInfo.getDriverContact());
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

}

