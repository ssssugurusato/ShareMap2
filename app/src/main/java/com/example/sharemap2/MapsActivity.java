package com.example.sharemap2;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapsActivity extends FragmentActivity
        {

    private PersonFragment mfragment_person;
    private SearchRouteFragment mfragment_search_root;
    private UploadRouteFragment mfragment_upload_root;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
            switch(Item.getItemId()){
                case R.id.navigation_upload:
                    mfragment_upload_root=new UploadRouteFragment();
                    FragmentTransaction transaction1=getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.fragment,mfragment_upload_root);
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                    return true;
                case R.id.navigation_search:
                    mfragment_search_root=new SearchRouteFragment();
                    FragmentTransaction transaction2=getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.fragment,mfragment_search_root);
                    transaction2.addToBackStack(null);
                    transaction2.commit();
                    return true;
                case R.id.navigation_person:
                    mfragment_person=new PersonFragment();
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
        mfragment_upload_root=new UploadRouteFragment();
        FragmentTransaction transaction1=getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.fragment,mfragment_upload_root);
        transaction1.commit();
        //ナビゲーションバー
        BottomNavigationView navView=findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
