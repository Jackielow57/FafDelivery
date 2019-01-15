package com.example.user.map;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
,GoogleMap.OnMarkerClickListener {


 //   SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
 //   SharedPreferences.Editor edt = pref.edit();

 //   edt.putDouble("longitude", 101.598043);
 //    edt.putString("latitude", 3.100835);

 //   edt.commit();


    private GoogleMap mMap;

    private LatLng KJPOSLaju;
    private LatLng HQPOSLaju;
    private LatLng WMPOSLaju;

    private Marker mKJPOSLaju;
    private Marker mHQPOSLaju;
    private Marker mWMPOSLaju;

    private String locationOne;

    LocationManager locationManager;

    //Save the users coordinates
 //   public void saveInfo(View view){
//        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

//        SharedPreferences.Editor editor = sharedPref.edit();


 //   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

     //    PERTH = new LatLng();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getData();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //checking if the network provider is enabled, then run
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Geo Coder
                    //get the latitude
                    double latitude = location.getLatitude();
                    //get the longitude
                    double longitude = location.getLongitude();
                    //instantiate the class, LatLng
                    LatLng latLng = new LatLng(latitude,longitude);
                    //Instantiate the class, Geocoder
                    Geocoder geocoder = new Geocoder(getApplicationContext());

                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() +",";
                        str += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));


                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10),5000,null);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Geo Coder
                    //get the latitude
                    double latitude = location.getLatitude();
                    //get the longitude
                    double longitude = location.getLongitude();
                    //instantiate the class, LatLng
                    LatLng latLng = new LatLng(latitude,longitude);
                    //Instantiate the class, Geocoder
                    Geocoder geocoder = new Geocoder(getApplicationContext());

                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality() +",";
                        str += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
             //           mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18),5000,null);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Empty array list to put markers
        List<Marker> markerList  = new ArrayList<>();

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        mKJPOSLaju = mMap.addMarker(new MarkerOptions()
        .position(KJPOSLaju)
        .title(locationOne)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mKJPOSLaju.setTag(0);
        markerList.add(mKJPOSLaju);

        mKJPOSLaju = mMap.addMarker(new MarkerOptions()
                .position(HQPOSLaju)
                .title("HeadQuaters Pos Laju")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mKJPOSLaju.setTag(0);
        markerList.add(mHQPOSLaju);

        mKJPOSLaju = mMap.addMarker(new MarkerOptions()
                .position(WMPOSLaju)
                .title("Wangsa Maju Pos Laju")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mKJPOSLaju.setTag(0);
        markerList.add(mWMPOSLaju);

        mMap.setOnMarkerClickListener(this);    //Register onclick listener

        /*for(Marker m : markerList){

            //Object to pass later
            LatLng latLng = new LatLng(m.getPosition().latitude,m.getPosition().longitude);

            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            //Log.d("Marker:", m.getTitle());
        }*/



        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer clickCount = (Integer)marker.getTag();
        if(clickCount!=null){
            clickCount = clickCount +1;

            marker.setTag(clickCount);
            Toast.makeText(this,marker.getTitle()+"has been clicked"+
            clickCount +"times", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void getData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("map",MODE_PRIVATE);
        locationOne = sharedPreferences.getString("firstLocation","");
        double x1 = Double.parseDouble(sharedPreferences.getString("x1",""));
        double y1 = Double.parseDouble(sharedPreferences.getString("y1",""));
        KJPOSLaju = new LatLng(x1, y1);
        double x2 = Double.parseDouble(sharedPreferences.getString("x2",""));
        double y2 = Double.parseDouble(sharedPreferences.getString("y2",""));
        HQPOSLaju = new LatLng(x2, y2);
        double x3 = Double.parseDouble(sharedPreferences.getString("x3",""));
        double y3 = Double.parseDouble(sharedPreferences.getString("y3",""));
        WMPOSLaju = new LatLng(x3, y3);
    }

}

