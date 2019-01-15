package com.example.user.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.lab44";
    private static String GET_URL = "https://faf-delivery.000webhostapp.com/getItem.php";
    List<AddItemToData> itemList;
    private TextView itmID, itmName,itmDesp,itmStatus;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private TableLayout tableLayout;
    private TableRow tableRow;
    // private AddItemToData item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        tableLayout = findViewById(R.id.itemTableLayout);

        tableLayout.setColumnStretchable(0,true);
        tableLayout.setColumnStretchable(1,true);
        tableLayout.setColumnStretchable(2,true);
        tableLayout.setColumnStretchable(3,true);

        itemList = new ArrayList<>();
        pDialog = new ProgressDialog(this);

        downloadItem(getApplicationContext(), GET_URL);

    }

    protected void onStart()
    {
        super.onStart();
        downloadItem(getApplicationContext(),GET_URL);
    }

    public void AddItemOnClick(View view)
    {
        Intent intent2 = new Intent(this,NewItemActivity.class);
        startActivity(intent2);
        finish();
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
                                addItemToTable();
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

    private void addItemToTable()
    {
        //int tableRowData = 1;
        //int [] table = {R.id.ItemRow1,R.id.ItemRow2,R.id.ItemRow3,R.id.ItemRow4,R.id.ItemRow5,R.id.ItemRow6,R.id.ItemRow7,R.id.ItemRow8};

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        String icNumber = sharedPreferences.getString("icnumber","");

        for (int i =0;i<itemList.size();i++)
        {
            if(itemList.get(i).getIcNumber().equals(icNumber))
            {
                tableRow = new TableRow(this);

                itmID = new TextView(this);
                itmID.setText(itemList.get(i).getItemID());
                itmID.setTextSize(20);

                itmName = new TextView(this);
                itmName.setText(itemList.get(i).getItemName());
                itmName.setTextSize(20);

                itmDesp = new TextView(this);
                itmDesp.setText(itemList.get(i).getItemDesp());
                itmDesp.setTextSize(20);

                itmStatus = new TextView(this);
                itmStatus.setText(itemList.get(i).getItemStatus());
                itmStatus.setTextSize(20);

                tableRow.addView(itmID);
                tableRow.addView(itmName);
                tableRow.addView(itmDesp);
                tableRow.addView(itmStatus);
                tableLayout.addView(tableRow);

                /*String itemIDtextView = "ItemID" + tableRow;
                String itemNametextView = "ItemName" + tableRow;
                String itemDespextView = "ItemDesp" + tableRow;
                String itemStatustextView = "ItemStatus" + tableRow;

                TextView itemID = findViewById(table[i]);
                TextView itemName = findViewById(R.id.ItemName1);
                TextView itemDesp = findViewById(R.id.ItemDesp1);
                TextView itemStatus = findViewById(R.id.ItemStatus1);

                itemID.setText(itemList.get(i).getItemID());
                itemName.setText(itemList.get(i).getItemName());
                itemDesp.setText(itemList.get(i).getItemDesp());
                itemStatus.setText(itemList.get(i).getItemStatus());

                tableRowData++;*/
            }
        }
    }
}
