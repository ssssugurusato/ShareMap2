package com.example.sharemap2;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private fragment_person mfragment_person;
    private fragment_search_root mfragment_search_root;
    private fragment_upload_root mfragment_upload_root;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
            switch(Item.getItemId()){
                case R.id.navigation_upload:
                    FragmentTransaction transaction1=getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.map,mfragment_upload_root);
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                    return true;
                case R.id.navigation_search:
                    FragmentTransaction transaction2=getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.map,mfragment_search_root);
                    transaction2.addToBackStack(null);
                    transaction2.commit();
                    return true;
                case R.id.navigation_person:
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

        mfragment_upload_root=new fragment_upload_root();
        mfragment_search_root=new fragment_search_root();
        mfragment_person=new fragment_person();

        setContentView(R.layout.layout_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BottomNavigationView navView=findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
    public  void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
    }




}
