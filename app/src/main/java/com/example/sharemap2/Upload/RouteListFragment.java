package com.example.sharemap2.Upload;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.sharemap2.MainActivity;
import com.example.sharemap2.R;
import com.example.sharemap2.sqlite.LocationAdapter;
import com.example.sharemap2.sqlite.LocationContract;
import com.example.sharemap2.sqlite.LocationOpenHelper;
import com.google.firebase.firestore.core.Query;


public class RouteListFragment extends Fragment {

    public  static RouteListFragment createInstance() {
        return new RouteListFragment();
    }

    private ListView listView;
    private RecyclerView mRecyclerView;
    private LocationAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.route_list, container, false);
        mRecyclerView = v.findViewById(R.id.UpdateLocationList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocationOpenHelper locationOpenHelper = new LocationOpenHelper(getActivity());
        //データベースファイルの削除
        //SQLiteDatabase.deleteDatabase(context.getDatabasePath(locationOpenHelper.getDatabaseName()));
        SQLiteDatabase db = locationOpenHelper.getWritableDatabase();
        mAdapter = new LocationAdapter(getContext(),getAllItems(db));
        mRecyclerView.setAdapter(mAdapter);
    }

    private Cursor getAllItems(SQLiteDatabase db) {
        return db.query(
                LocationContract.Locations.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                LocationContract.Locations._ID + " desc"
        );

    }

}
