package com.example.sharemap2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class fragment_upload_root extends Fragment implements OnMapReadyCallback, LocationListener,
        View.OnClickListener,CompoundButton.OnCheckedChangeListener, GoogleMap.OnMarkerClickListener {

    private static LocationManager locationmanager1;
    private static GoogleMap mMap;
    private MapView mapView;
    private static final int LOCATION_CODE = 100;
    private static final String[] LOCATION_PERMISSION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    private List<LatLng> mRunList = new ArrayList<LatLng>();
    private LatLng latlong, latlong2;
    private static Location location1;
    private static Button mButton, mButton2;
    private Marker userMark;
    private List<Marker> mMakerList=new ArrayList<Marker>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locationmanager1 = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);



        if (ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[0]) != GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[1]) != GRANTED) {
            // ↓「アクセスを許可しますか？」
            requestPermissions(LOCATION_PERMISSION, LOCATION_CODE);
        } else {
            //許可されたら位置取得
            //下の行にNull

             startLocation();


        }

    }

    //許可されたら位置取得、のところ
    private void startLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[0]) == GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[1]) == GRANTED) {
            Log.d("tag","Null-bbbbbbbbbbbbbbbbbbbbbbbbbb");
            // 許可を得られたことを確認できた段階で初めてsetContentView()を呼ぶ
            // onMapReady()が走るのはこれ以後になる

            //FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            SupportMapFragment mapFragment =(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            //この行にNul
        }
    }

    @Override
    public  void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        Log.d("a","OKabc");

        if (ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[0]) == GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[1]) == GRANTED) {


            Criteria criteria = new Criteria();
            //以下位置情報の精度を設定、高精度にした
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            //基準を満たすプロバイダ名を取得する
            String provider = locationmanager1.getBestProvider(criteria, true);
            // 最後の位置情報取得
            location1 = locationmanager1.getLastKnownLocation(provider);

            double lat =  location1.getLatitude();
            double lon =  location1.getLongitude();
            latlong = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 19));

            mButton=(Button)getActivity().findViewById(R.id.button) ;
            mButton2=(Button)getActivity().findViewById(R.id.button2) ;
            mButton.setOnClickListener(this);
            mButton2.setOnClickListener(this);

            //プロバイダーに基づいた リスナー を登録する
            //最低0秒、最低0mで発火、これより細かい更新はされない

            //locationmanager1.removeUpdates(this);

        }

    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button:
                if (ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[0]) == GRANTED ||
                        ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[1]) == GRANTED) {
                    locationmanager1.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
                    locationmanager1.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
                }
            case R.id.button2:
                locationmanager1.removeUpdates(this);
                break;
        }
    }

    // アクセス許可のダイアログで操作を行ったときに呼ばれるメソッド
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_CODE) {
            if (grantResults[0] == GRANTED) {
                startLocation();
            } else {
                // 拒否されたのならアプリ続行不可能
                getActivity().finish();
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        locationmanager1.removeUpdates(this);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(curr));

        double lat = location.getLatitude();
        double lon = location.getLongitude();
        latlong2 = new LatLng(lat, lon);

        TextView text=(TextView)getActivity().findViewById(R.id.textView1);
        text.setText(latlong2.toString());
        //locationが変わるごとにマークをついか
        userMark = mMap.addMarker(new MarkerOptions()
                .position(latlong2)
                .title("user point"));
        //userMark.setTag(0);
        mMakerList.add(userMark);
        Log.d("count", "mMarkerList.length.toString");
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        drawTrace(latlong2);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        return true;
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Tag","enable");
    }

    //ロケーションプロバイダが利用不可能になるとコールバックされる
    @Override
    public void onProviderDisabled(String s) {
        Log.d("Tag","provider,disable");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    //mRunListの緯度経度のリストをポリラインオプションの要素に加える
    private void drawTrace(LatLng latlng) {
        mRunList.add(latlng);
        PolylineOptions polyOptions = new PolylineOptions();
        for (LatLng polyLatLng : mRunList) {
            polyOptions.add(polyLatLng);
        }

        polyOptions.color(Color.BLUE);
        polyOptions.width(4);
        polyOptions.geodesic(false);
        mMap.addPolyline(polyOptions);

    }

}
