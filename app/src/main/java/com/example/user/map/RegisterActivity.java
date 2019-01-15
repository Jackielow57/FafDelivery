package com.example.user.map;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    private final static Pattern IC_PATTERN = Pattern.compile("(?=\\S+$)"+".{12,12}"+"$");
    private final static Pattern PASSWORD_PATTERN = Pattern.compile("(?=\\S+$)"+".{8,}"+"$");
    private final static Pattern PHONE_PATTERN  =  Pattern.compile("(?=\\S+$)"+".{10,12}"+"$");

    private EditText userName;
    private EditText icNumber;
    private EditText loginID;
    private EditText password;
    private EditText email;
    private EditText phoneNumber;
    private EditText address;

    private List<RegisterFormUserInfo> reListRegister;
    public RegisterFormUserInfo info;
    private Bundle registerBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        info = new RegisterFormUserInfo();
        reListRegister = new ArrayList<>();
        registerBundle = new Bundle();

        registerBundle = getIntent().getExtras();
        reListRegister = (ArrayList<RegisterFormUserInfo>)registerBundle.getSerializable("databaseUserInfo");

        userName = findViewById(R.id.userNameEditTextRegister);
        icNumber = findViewById(R.id.icEditTextRegister);
        loginID = findViewById(R.id.loginIDEditTextRegister);
        password = findViewById(R.id.passwordEditTextRegister);
        email = findViewById(R.id.emailEditTextRegister);
        phoneNumber = findViewById(R.id.phoneEditTextRegister);
        address = findViewById(R.id.addressEditTextRegister);

    }
    public void registerPageClick(View view)
    {
        info.setUsername(userName.getText().toString());
        info.setIcNumber(icNumber.getText().toString());
        info.setLoginID(loginID.getText().toString());
        info.setPassword(password.getText().toString());
        info.setEmail(email.getText().toString());
        info.setPhoneNumber(phoneNumber.getText().toString());
        info.setAddress(address.getText().toString());

        if(((userNameValidation(info.getUsername())== true
                && icNumberValidation(info.getIcNumber()) == true
                && loginIDValidation(info.getLoginID()) == true
                && passwordValidation(info.getPassword()) == true
                && emailValidation(info.getEmail())==true
                && phoneNumberValidation(info.getPhoneNumber())==true
                && addressValidation(info.getAddress())==true)))
        {
            try {
                //TODO: Please update the URL to point to your own server
                makeServiceCall(this, "https://faf-delivery.000webhostapp.com/userInfo.php", info);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }


    }
    public boolean userNameValidation(String userNameParameter)
    {
        if(userNameParameter.isEmpty())
        {
            userName.setError("Please Enter Your Name");
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean icNumberValidation(String icNumberParameter)
    {
        for(int i = 0; i <reListRegister.size() ; i++)
        {
            if(reListRegister.get(i).getIcNumber().equals(icNumberParameter))
            {
                icNumber.setError("This IC Has Been Exist ");
                return false;
            }
        }
        if(icNumberParameter.isEmpty())
        {
            icNumber.setError("Please Enter Your IC Number");
            return false;
        }
        else if(!IC_PATTERN.matcher(icNumberParameter).matches())
        {
            icNumber.setError("Please Enter You IC Number Correctly (12 Values)");
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean loginIDValidation(String loginIDParameter)
    {
        for(int i = 0; i <reListRegister.size() ; i++)
        {
            if(reListRegister.get(i).getLoginID().equals(loginIDParameter))
            {
                loginID.setError("This Login ID Has Been Exist ");
                return false;
            }
        }
        if(loginIDParameter.isEmpty())
        {
            loginID.setError("Please Enter Your Login ID");
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean passwordValidation(String passwordParameter)
    {
        if(passwordParameter.isEmpty())
        {
            password.setError("Please Enter Your Password");
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(passwordParameter).matches())
        {
            password.setError("Please Enter Atleast 8 value");
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean emailValidation(String emailParameter)
    {
        for(int i = 0; i <reListRegister.size() ; i++)
        {
            if(reListRegister.get(i).getEmail().equals(emailParameter))
            {
                email.setError("This Email Has Been Exist ");
                return false;
            }
        }
        if(emailParameter.isEmpty())
        {
            email.setError("Please Enter Your Email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailParameter).matches())
        {
            email.setError("Please Follow The Email Format");
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean phoneNumberValidation(String phoneNumberParameter)
    {
        if(phoneNumberParameter.isEmpty())
        {
            phoneNumber.setError("Please Enter Your Phone Number");
            return false;
        }
        else if(!PHONE_PATTERN.matcher(phoneNumberParameter).matches())
        {
            phoneNumber.setError("Please Enter Correct Phone Number (10-12 values)");
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean addressValidation(String addressParameter)
    {
        if(addressParameter.isEmpty())
        {
            address.setError("Please Enter Your Address");
            return false;
        }
        else{
            return true;
        }
    }
    public void makeServiceCall(Context context, String url, final RegisterFormUserInfo info) {
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
                    params.put("Username",info.getUsername());
                    params.put("ICNumber",info.getIcNumber());
                    params.put("LoginID",info.getLoginID());
                    params.put("Password", info.getPassword());
                    params.put("Email",info.getEmail());
                    params.put("Phone",info.getPhoneNumber());
                    params.put("Address",info.getAddress());
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
