package com.example.asus.baidumaptest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationManager locationManager;
    private  String provider;
    private boolean isFirstLocate =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("test", "2");
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mapView=(MapView)findViewById(R.id.map_view);
        baiduMap =mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList =locationManager.getProviders(true);
        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            Log.d("test","gps");
            provider = LocationManager.GPS_PROVIDER;
            Log.d("test",provider);
        }
        else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
            Log.d("test","net");
            provider=LocationManager.NETWORK_PROVIDER;
            Log.d("test",provider);
        }
        else {
            Toast.makeText(this,"No location provider to use",Toast.LENGTH_SHORT).show();
            Log.d("test","meiyou");
            return;
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Log.d("test","4");
            Location location = locationManager.getLastKnownLocation(provider);
            location=null;
           // Log.d("test","1 "+location.getLatitude()+"2 "+location.getLongitude());
          /*  while(location == null){
                locationManager.requestLocationUpdates(provider, 60000, 1, locationListener);
                location=locationManager.getLastKnownLocation(provider);
   location=null;
           Log.d("test","lalalala");
            }*/
          //  location=locationManager.getLastKnownLocation(provider);
            if(location!=null)
        {
            Log.d("test","5");
            navigateTo(location);
        }

                locationManager.requestLocationUpdates(provider, 8000, 1, locationListener);

    }
    }

    private void navigateTo(Location location)
    {
        Log.d("test","aaaa");
//        if(isFirstLocate)
//        {

            LatLng sourceLatLng=new LatLng(location.getLatitude(),location.getLongitude());
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
// sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng ll = converter.convert();
        Log.d("test","1 "+location.getLatitude()+" 2 "+location.getLongitude());
            MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update=MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
           // isFirstLocate=false;
       // }
        MyLocationData.Builder locationBuilder =new MyLocationData.Builder();
        locationBuilder.latitude(ll.latitude);
        locationBuilder.longitude(ll.longitude);
        MyLocationData locationData =locationBuilder.build();
        baiduMap.setMyLocationData(locationData);

    }
    LocationListener locationListener =new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location!=null)
            {
                Log.d("test","inin");
                navigateTo(location);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(locationListener);

        }}
    }
    @Override
    protected  void onPause()
    {
        super.onPause();
        mapView.onPause();
    }
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }
}