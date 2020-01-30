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

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sharemap2.model.LocationData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.location.LocationManager.GPS_PROVIDER;


public class UploadRouteFragment extends Fragment implements OnMapReadyCallback, LocationListener,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, GoogleMap.OnMarkerClickListener {

    private static LocationManager locationmanager1;
    private static GoogleMap mMap;
    private static final int LOCATION_CODE = 100;
    private static final String[] LOCATION_PERMISSION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    private List<LatLng> mRunList = new ArrayList<LatLng>();
    private LatLng latlong, latlong2;
    private static Location location1;
    private Button mButton, mButton2;
    private Marker userMark;
    private List<Marker> mMakerList = new ArrayList<Marker>();

    private ListView mapInfoLayout;
    String provider;
    private MapsActivity mactivity = null;
    double lat;
    double lon;
    private static final String[] names = {
            "Yuka",
            "Kurumi",
            "Tomoya"
    };

    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private String startDate;
    private double accuracy;
    private String created_at;
    private String TAG1 = "Recording current location";
    private String TAG2 = "Recording root";

    @Deprecated
    @CallSuper
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MapsActivity) {
            Log.d("aaa", "OKOKOKOKOKOK");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload_route, container, false);
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
            Log.d("tag", "Null-bbbbbbbbbbbbbbbbbbbbbbbbbb");
            // 許可を得られたことを確認できた段階で初めてsetContentView()を呼ぶ
            // onMapReady()が走るのはこれ以後になる
            //FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            // ListViewのインスタンスを生成
            ListView listView = getActivity().findViewById(R.id.mapInfoLayout);
            // BaseAdapter を継承したadapterのインスタンスを生成
            // レイアウトファイル list_items.xml を
            // activity_main.xml に inflate するためにadapterに引数として渡す
            BaseAdapter adapter = new EditListAdapter(getContext(),
                    R.layout.list_items, names);
            // ListViewにadapterをセット
            listView.setAdapter(adapter);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);


        if (ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[0]) == GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[1]) == GRANTED) {


            Criteria criteria = new Criteria();
            //以下位置情報の精度を設定、高精度にした
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            //基準を満たすプロバイダ名を取得する
            provider = locationmanager1.getBestProvider(criteria, true);
            // 最後の位置情報取得
            location1 = locationmanager1.getLastKnownLocation(provider);


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngGet(location1), 19));

            mButton = (Button) getActivity().findViewById(R.id.button);
            mButton2 = (Button) getActivity().findViewById(R.id.button2);
            mButton.setOnClickListener(this);
            mButton2.setOnClickListener(this);

            //プロバイダーに基づいた リスナー を登録する
            //最低0秒、最低0mで発火、これより細かい更新はされない


        }

    }

    private LatLng LatLngGet(Location location){
        if (ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[0]) == GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[1]) == GRANTED) {
            location = locationmanager1.getLastKnownLocation(provider);
            lat = location.getLatitude();
            lon = location.getLongitude();
            latlong = new LatLng(lat, lon);

        }
        return latlong;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[0]) == GRANTED ||
                        ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[1]) == GRANTED) {
                    locationmanager1.requestLocationUpdates(GPS_PROVIDER, 5000, 0, this);
                    locationmanager1.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
                    startDate = getNowDate();
                }
                break;
            case R.id.button2:
                locationmanager1.removeUpdates(this);
                mapInfoLayout = getActivity().findViewById(R.id.mapInfoLayout);
                mapInfoLayout.setVisibility(View.VISIBLE);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngGet(locationmanager1.getLastKnownLocation(provider)), 10));

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
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        String currentTime = sdf.format(location.getTime());
        created_at = currentTime;

        writeToDatabase(latlong2, accuracy, created_at);


        //locationが変わるごとにマークをついか
        userMark = mMap.addMarker(new MarkerOptions()
                .position(latlong2)
                .title("user point")
                .draggable(true));
        //userMark.setTag(0);

        mMakerList.add(userMark);
        Log.d("count", "mMarkerList-commitしたよ");
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
        Log.d("Tag", "enable");
    }

    //ロケーションプロバイダが利用不可能になるとコールバックされる
    @Override
    public void onProviderDisabled(String s) {
        Log.d("Tag", "provider,disable");
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

    private void writeToDatabase(LatLng latlng, double accuracy, String created_at) {
        final String title = startDate;
        final String uid = getUid();

        LocationData location = new LocationData(title, latlong, accuracy, created_at, uid);

// Add a new document with a generated ID
        mDatabase.collection("locations")
                .add(location)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG1, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG1, "Error adding document", e);
                    }
                });

        mDatabase.collection("roots").document(uid)
                .collection(title)
                .add(location)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG2, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG2, "Error adding document", e);
                    }
                });


    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
