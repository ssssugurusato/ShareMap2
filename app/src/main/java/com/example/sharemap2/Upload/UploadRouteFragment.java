package com.example.sharemap2.Upload;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.example.sharemap2.R;
import com.example.sharemap2.model.LocationData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private LatLng latlng, latlng2;
    private static Location location1;
    private Button mButton, mButton2;
    private Marker userMark;
    // ListViewに表示する項目を生成
    private ArrayList<Marker> mMarkerList = new ArrayList<>();
    public static ArrayList<String> commentList;
    private PolylineOptions polyOptions;

    private ListView mapInfoLayout;
    String provider;
    double lat;
    double lon;


    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private String startDate;
    private double accuracy;
    private String created_at;
    private String TAG1 = "Recording current location";
    private String TAG2 = "Recording root";
    private String TAG3 = "Firestore";
    private EditWindowFragment mEditWindowFragment;
    int num = 1;
    private List<String> itemtitlelist=new ArrayList<>();
    private Toolbar toolbar;

    //Activityに実装
    public interface AdapterInActivity{
        void AdapterSet(List<String> itemtitlelist);
    }
    private AdapterInActivity mAdapterInActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //contextはAdapterInActivityインタフェースのインスタンスがあるか
        if(context instanceof AdapterInActivity) {
            mAdapterInActivity= (AdapterInActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_route, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("目的地までの経路をアップ！");
        return view;
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
            // 許可を得られたことを確認できた段階で初めてsetContentView()を呼ぶ
            // onMapReady()が走るのはこれ以後になる
            //FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.6511494145129, 139.53881523584), 19));

            // 最後の位置情報取得
            mButton = (Button) getActivity().findViewById(R.id.button);
            mButton2 = (Button) getActivity().findViewById(R.id.button2);
            mButton.setOnClickListener(this);
            mButton2.setOnClickListener(this);

            //プロバイダーに基づいた リスナー を登録する
            //最低0秒、最低0mで発火、これより細かい更新はされない


        }

    }

    private LatLng LatLngGet(Location location) {
        if (ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[0]) == GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSION[1]) == GRANTED) {

            lat = location.getLatitude();
            lon = location.getLongitude();
            latlng = new LatLng(lat, lon);
            return latlng;
        }
        return null;
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
                toolbar.setVisibility(View.GONE);
                mButton.setVisibility(View.GONE);
                mButton2.setVisibility(View.GONE);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.list_frame, RouteListFragment.createInstance());
                transaction.addToBackStack(null);
                transaction.commit();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(curr));


        //位置情報とそれに関連する情報の取得
        latlng2 = LatLngGet(location);
        accuracy = location.getAccuracy();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        String currentTime = sdf.format(location.getTime());
        created_at = currentTime;

        //データベースへの書き込みを実行
        writeToDatabase(latlng2.latitude, latlng2.longitude, accuracy, created_at);

        mRunList.add(latlng2);
        //locationが変わるごとにマークをついか
        userMark = mMap.addMarker(new MarkerOptions()
                .position(latlng2)
                .title("経路" + num)
                .draggable(true));
        //userMark.setTag(0);
        mMarkerList.add(userMark);
        itemtitlelist.add(num+"個目の位置情報");
        num = num + 1;

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        return true;
    }


    //mRunListの緯度経度のリストをポリラインオプションの要素に加える
    private void drawTrace(LatLng latlng) {

        // Set a listener for marker click.
        //mMap.setOnMarkerClickListener(this);
        polyOptions = new PolylineOptions();
        //mRunListの要素である緯度経度つまりLatLngをポリラインの要素として登録
        for (LatLng polyLatLng : mRunList) {
            polyOptions.add(polyLatLng);
        }
        polyOptions.color(Color.BLUE);
        polyOptions.width(4);
        polyOptions.geodesic(false);
        mMap.addPolyline(polyOptions);
    }

    /*------------------------------------------------------------------------------------
      以下はメインスレッドとはあまり関係ない処理!!!!!!!!!!!!!
     -------------------------------------------------------------------------------------*/

    // permissionが未設定時のコールバック関数
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

    //以下はロケーションリスナーインタフェースのonLocationChanged以外のあまり使用しないけど書かなきゃいけないやつまとめ
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

     /*------------------------------------------------------------------------------------
      以上がメインスレッドとはあまり関係ない処理!!!!!!!!!!!!!!!!!!!!!
     -------------------------------------------------------------------------------------*/

    private void writeToDatabase(double latitude, double longitude, double accuracy, String created_at) {
        final String title = startDate;
        final String uid = getUid();

        LocationData location = new LocationData(title, latitude, longitude, accuracy, created_at, uid);

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

        mDatabase.collection("locations")
                .whereEqualTo("title", startDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG3, document.getId() + " => " + document.getData());

                                final String uid = getUid();
                                final String lid = document.getId();

                                mDatabase.collection("roots").document(uid)
                                        .collection(document.getString("title")).document(lid)
                                        .set(document.getData());
                            }
                        } else {
                            Log.d(TAG3, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    public static String getNowDate() {
        final DateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }




}

