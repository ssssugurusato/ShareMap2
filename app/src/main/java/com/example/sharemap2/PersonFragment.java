package com.example.sharemap2;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sharemap2.adapter.LocationsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class PersonFragment extends Fragment implements LocationsAdapter.OnLocationsSelectedListener {

    private RecyclerView mRecyclerView;
    private LocationsAdapter mAdapter;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private String TAG = "Firestore";
    private int LIMIT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        mRecyclerView = view.findViewById(R.id.LocationList);;

        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("roots")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(LIMIT)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        mQuery = mFirestore.collection("locations")
                                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                                .whereEqualTo("title", document.getString("title"))
                                .orderBy("created_at", Query.Direction.DESCENDING);

                        mAdapter = new LocationsAdapter(mQuery, com.example.sharemap2.PersonFragment.this) {
                            @Override
                            protected void onDataChanged() {
                                // Show/hide content if the query returns empty.
                                if (getItemCount() == 0) {
                                    mRecyclerView.setVisibility(View.GONE);
                                } else {
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                }
                            }
                        };
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

//        mQuery = mFirestore.collection("routes")
//                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
//                .orderBy("created_at", Query.Direction.DESCENDING)
//                .limit(LIMIT)
        ;


          /*  @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
//                Snackbar.make(findViewById(android.R.id.content),
//                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };
          */
//        mAdapterをmRecyclerViewにセットする

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onLocationsSelected(DocumentSnapshot locationData) {

        Intent intent = new Intent(getActivity(), LocationDetailActivity.class);
        intent.putExtra(LocationDetailActivity.KEY_LOCATION_ID, locationData.getId());

        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }
}