package com.example.sharemap2;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, LocationListener, CompoundButton.OnCheckedChangeListener,
        View.OnClickListener{

    private static GoogleMap mMap;
    private fragment_person mfragment_person;
    private fragment_search_root mfragment_search_root;
    private fragment_upload_root mfragment_upload_root;

    //以下ジョギングアプリの引用
    private static final int ADDRESSLOADER_ID = 0;
    private List<LatLng> mRunList = new ArrayList<LatLng>();
    private double mMeter = 0.0;
    private boolean mStart = false;
    private boolean mFirst = false;
    private boolean mStop = false;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss.SS");

    //以下パーミッションの設定と現在地取得のための定義
    private LatLng latlong, latlong2;
    private static Location location1;
    private LocationManager locationmanager1;
    private static final int LOCATION_CODE = 100;
    private static final String[] LOCATION_PERMISSION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
            switch(Item.getItemId()){
                case R.id.navigation_upload:
                    mfragment_upload_root=new fragment_upload_root();
                    FragmentTransaction transaction1=getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.map,mfragment_upload_root);
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                    return true;
                case R.id.navigation_search:
                    mfragment_search_root=new fragment_search_root();
                    FragmentTransaction transaction2=getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.map,mfragment_search_root);
                    transaction2.addToBackStack(null);
                    transaction2.commit();
                    return true;
                case R.id.navigation_person:
                    mfragment_person=new fragment_person();
                    FragmentTransaction transaction3=getSupportFragmentManager().beginTransaction();
                    transaction3.replace(R.id.map,mfragment_person);
                    transaction3.addToBackStack(null);
                    transaction3.commit();
                    return true;
            }
            return false;
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        locationmanager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, LOCATION_PERMISSION[0]) != GRANTED &&
                    ActivityCompat.checkSelfPermission(this, LOCATION_PERMISSION[1]) != GRANTED) {
                // ↓「アクセスを許可しますか？」
                requestPermissions(LOCATION_PERMISSION, LOCATION_CODE);
            } else {
                startLocation();
        }
    }

    //初めにするマップの配置
   @Override
    public  void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
       double lat = location1.getLatitude();
       double lon = location1.getLongitude();
       latlong = new LatLng(lat, lon);
       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 19));
       locationmanager1.removeUpdates(this);
       findViewById(R.id.button).setOnClickListener(this);
   }

    public void onClick(View view){

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
                finish();
            }
        }

    }

   //Permissionが許可されたときに呼び出される
    void startLocation() {
        if (ActivityCompat.checkSelfPermission(this, LOCATION_PERMISSION[0]) == GRANTED ||
                ActivityCompat.checkSelfPermission(this, LOCATION_PERMISSION[1]) == GRANTED) {

            // 許可を得られたことを確認できた段階で初めてsetContentView()を呼ぶ
            // onMapReady()が走るのはこれ以後になる
            setContentView(R.layout.layout_main);
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            BottomNavigationView navView=findViewById(R.id.nav_view);
            navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            Criteria criteria = new Criteria();
            //以下位置情報の精度を設定、高精度にした
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            //基準を満たすプロバイダ名を取得する
            String provider = locationmanager1.getBestProvider(criteria, true);
            //プロバイダーに基づいた リスナー を登録する
            //最低0秒、最低0mで発火、これより細かい更新はされない
            locationmanager1.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationmanager1.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            // 最後の位置情報取得
            location1 = locationmanager1.getLastKnownLocation(provider);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationmanager1.removeUpdates(this);
    }

   //
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
        double lat = location1.getLatitude();
        double lon = location1.getLongitude();
        latlong2 = new LatLng(lat, lon);

        drawTrace(latlong2);
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    //ロケーションプロバイダが利用不可能になるとコールバックされる
    @Override
    public void onProviderDisabled(String s) {
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    //mRunListの緯度経度のリストをポリラインオプションの要素に加える
    private void drawTrace(LatLng latlng) {
        mRunList.add(latlng);
        if(mRunList.size() > 1) {
            PolylineOptions polyOptions = new PolylineOptions();
            for (LatLng polyLatLng : mRunList) {
                polyOptions.add(polyLatLng);
            }
            polyOptions.color(Color.BLUE);
            polyOptions.width(3);
            polyOptions.geodesic(false);
            mMap.addPolyline(polyOptions);
        }
    }


}
