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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity
        {


    private fragment_person mfragment_person;
    private fragment_search_root mfragment_search_root;
    private fragment_upload_root mfragment_upload_root;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
            switch(Item.getItemId()){
                case R.id.navigation_upload:
                    mfragment_upload_root=new fragment_upload_root();
                    FragmentTransaction transaction1=getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.fragment,mfragment_upload_root);
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                    return true;
                case R.id.navigation_search:
                    mfragment_search_root=new fragment_search_root();
                    FragmentTransaction transaction2=getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.fragment,mfragment_search_root);
                    transaction2.addToBackStack(null);
                    transaction2.commit();
                    return true;
                case R.id.navigation_person:
                    mfragment_person=new fragment_person();
                    FragmentTransaction transaction3=getSupportFragmentManager().beginTransaction();
                    transaction3.replace(R.id.fragment,mfragment_person);
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
        setContentView(R.layout.layout_main);
        mfragment_upload_root=new fragment_upload_root();
        FragmentTransaction transaction1=getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.fragment,mfragment_upload_root);
        transaction1.commit();
        //ナビゲーションバー
        BottomNavigationView navView=findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



}
