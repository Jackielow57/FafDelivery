package com.example.user.map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Profile extends AppCompatActivity {

    private TextView username;
    private TextView email;
    private TextView phone;
    private TextView address;
    private ImageView profileImage;

    private static final int GET_IMAGE = 1;
    private Uri imageUri;
    private Button driverButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.name_profile);
        email = findViewById(R.id.email_profile);
        phone = findViewById(R.id.phone_profile);
        address = findViewById(R.id.address_profile);
        profileImage = findViewById(R.id.imageView8);

        driverButton = findViewById(R.id.AddDriverButton);

        getData();

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        String loginIDSp = sharedPreferences.getString("loginid","");
        loginIDSp = loginIDSp.toUpperCase();
        if(loginIDSp.equals("STAFF"))
            driverButton.setVisibility(View.VISIBLE);
        else
            driverButton.setVisibility(View.GONE);
    }

    public void getData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        String nameSP = sharedPreferences.getString("username","");
        String emailSP = sharedPreferences.getString("email","");
        String phoneSP = sharedPreferences.getString("phone","");
        String addressSP = sharedPreferences.getString("address","");
        String imageSp = sharedPreferences.getString("userimage","");

        username.setText(nameSP);
        email.setText(emailSP);
        phone.setText(phoneSP);
        address.setText(addressSP);

        if(!imageSp.isEmpty())
        {
            byte[] b = Base64.decode(imageSp, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            profileImage.setImageBitmap(bitmap);
        }
    }

    public void uploadImage(View view)
    {
        Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,GET_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(resultCode == RESULT_OK && requestCode == GET_IMAGE)
        {
            imageUri =  data.getData();
            profileImage.setImageURI(imageUri);

            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profileImage.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                byte[]b = baos.toByteArray();

                String encode = Base64.encodeToString(b,Base64.DEFAULT);
                editor.putString("userimage",encode);
                editor.apply();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void logOut(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoAddDriver(View view)
    {
        Intent intent2 = new Intent(this,AddDriver.class);
        startActivity(intent2);
    }

}
