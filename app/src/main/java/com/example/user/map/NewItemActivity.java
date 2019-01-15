package com.example.user.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class NewItemActivity extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.lab44";
    private static String GET_URL = "https://faf-delivery.000webhostapp.com/getItem.php";
    List<AddItemToData> itemList;
    private RequestQueue queue;
    private ProgressDialog pDialog;

    private EditText itemID;
    private EditText itemName;
    private EditText itemDesp;

    public AddItemToData item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        itemID = findViewById(R.id.ItemIDEdit);
        itemName = findViewById(R.id.ItemNameEdit);
        itemDesp = findViewById(R.id.ItemDespEdit);

        item = new AddItemToData();
        itemList = new ArrayList<>();
        pDialog = new ProgressDialog(this);
        downloadItem(getApplicationContext(), GET_URL);

    }

    public void AddItemClick(View view)
    {
        item = new AddItemToData();
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        String icNumber = sharedPreferences.getString("icnumber","");
        item.setIcNumber(icNumber);
        item.setItemID(itemID.getText().toString());
        item.setItemName(itemName.getText().toString());
        item.setItemDesp(itemDesp.getText().toString());
        item.setItemStatus("In processing");

        if(((ItemIDValidation(item.getItemID())== true
                && ItemNameValidation(item.getItemName()) == true
                && ItemDespValidation(item.getItemDesp()) == true)))
        {

            try {
                //TODO: Please update the URL to point to your own server
                makeServiceCall(this, "https://faf-delivery.000webhostapp.com/item.php", item);
                Intent intent = new Intent(this,ItemActivity.class);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }



    }

    public boolean ItemIDValidation(String ItemIDParameter)
    {
        for (int i = 0 ; i < itemList.size(); i ++)
        {
            if (ItemIDParameter.equals(itemList.get(i).getItemID()))
            {
                itemID.setError("Item ID is already Existed");
                return false;
            }
        }


        if(ItemIDParameter.isEmpty())
        {
            itemID.setError("Please Enter Item ID");
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean ItemNameValidation(String ItemNameParameter)
    {
        if(ItemNameParameter.isEmpty())
        {
            itemName.setError("Please Enter Item Name");
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean ItemDespValidation(String ItemDespParameter)
    {
        if(ItemDespParameter.isEmpty())
        {
            itemDesp.setError("Please Enter Item Description");
            return false;
        }
        else
        {
            return true;
        }
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

    public void makeServiceCall(Context context, String url, final AddItemToData item) {
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
                                    //finish();

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
                    params.put("ICNumber", item.getIcNumber());
                    params.put("ItemCode",item.getItemID());
                    params.put("ItemName",item.getItemName());
                    params.put("ItemDescription",item.getItemDesp());
                    params.put("ItemStatus", item.getItemStatus());
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
