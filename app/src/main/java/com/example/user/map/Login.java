package com.example.user.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.lab44";
    private static String GET_URL = "https://faf-delivery.000webhostapp.com/login.php";
    private List<RegisterFormUserInfo> reList;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private EditText loginBar;
    private EditText passwordBar;

    private RegisterFormUserInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBar = findViewById(R.id.editTextLogin);
        passwordBar = findViewById(R.id.editTextPassword);

        pDialog = new ProgressDialog(this);
        info = new RegisterFormUserInfo();
        reList = new ArrayList<>();

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadCourse(getApplicationContext(), GET_URL);
    }

    public void registerOnClick(View view)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("databaseUserInfo", (Serializable) reList);
        Intent intent = new Intent(this,RegisterActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    private void downloadCourse(Context context, String url) {
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
                            reList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userInfoResponse = (JSONObject) response.get(i);
                                String userName = userInfoResponse.getString("userName");
                                String icNumber = userInfoResponse.getString("icNumber");
                                String loginID = userInfoResponse.getString("loginID");
                                String password = userInfoResponse.getString("password");
                                String email = userInfoResponse.getString("email");
                                String phone = userInfoResponse.getString("phone");
                                String address = userInfoResponse.getString("address");
                                RegisterFormUserInfo info = new RegisterFormUserInfo(userName,icNumber,loginID,password,email,phone,address);
                                reList.add(info);
                            }
                            if (pDialog.isShowing())
                                pDialog.dismiss();
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
   /* @Override
    protected void onPause() {
        super.onPause();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }*/

    private boolean checkID(String loginID)
    {
        if(loginID.isEmpty())
        {
            loginBar.setError("Empty Login ID");
            return false;
        }
        else
        {
            return true;
        }
    }
    private boolean checkPsw(String password)
    {
        if(password.isEmpty())
        {
            passwordBar.setError("Empty Password");
            return false;
        }
        else
        {
            return true;
        }
    }
    public void checkExistingAccount(View view)
    {
        boolean isFound = false;
        if(checkID(loginBar.getText().toString()) == true && checkPsw(passwordBar.getText().toString())==true)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for(int i = 0 ; i < reList.size(); i ++)
            {
                if(reList.get(i).getLoginID().equals(loginBar.getText().toString())==true && reList.get(i).getPassword().equals(passwordBar.getText().toString()) == true)
                {

                    editor.putString("username",reList.get(i).getUsername());
                    editor.putString("icnumber",reList.get(i).getIcNumber());
                    editor.putString("loginid",reList.get(i).getLoginID());
                    editor.putString("password",reList.get(i).getPassword());
                    editor.putString("email",reList.get(i).getEmail());
                    editor.putString("phone",reList.get(i).getPhoneNumber());
                    editor.putString("address",reList.get(i).getAddress());
                    editor.putString("userimage","");
                    editor.apply();
                    isFound = true;
                    Intent intent = new Intent(this,Profile.class);
                    startActivity(intent);
                    break;
                }
            }
            if(isFound == true)
                Toast.makeText(this, "Found It Successfully", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "ID Does Not Exist", Toast.LENGTH_LONG).show();
        }

    }
}
