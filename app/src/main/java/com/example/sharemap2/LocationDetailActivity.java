package com.example.sharemap2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharemap2.model.LocationData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LocationDetailActivity extends AppCompatActivity implements
            View.OnClickListener,
            EventListener<DocumentSnapshot> {

        private static final String TAG = "LocationDetail";

        public static final String KEY_RESTAURANT_ID = "key_location_id";

        private TextView timeView;
        private TextView accuracyView;
        private TextView latitudeView;
        private TextView longitudeView;

        private FirebaseFirestore mFirestore;
        private DocumentReference mLocationRef;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_location_detail);

            timeView = findViewById(R.id.created_at);
            accuracyView = findViewById(R.id.accuracy);
            latitudeView = findViewById(R.id.lat);
            longitudeView = findViewById(R.id.lng);

            findViewById(R.id.saveFab).setOnClickListener(this);

            // Get restaurant ID from extras
            String locationsId = getIntent().getExtras().getString(KEY_RESTAURANT_ID);
            if (locationsId == null) {
                throw new IllegalArgumentException("Must pass extra " + KEY_RESTAURANT_ID);
            }

            // Initialize Firestore
            mFirestore = FirebaseFirestore.getInstance();
        }

        @Override
        public void onStart() {
            super.onStart();

        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.saveFab:
                    onSaveFabClicked(v);
                    break;
                case R.id.deleteFab:
                    onDeleteClicked(v);
                    break;
            }
        }

        private void onSaveFabClicked(View v) {
            UpdateLocationData();
        }

        private void onDeleteClicked(View v) {
            RemoveLocationData();
        }

        private void UpdateLocationData() {

        }

        private void RemoveLocationData() {

        }

    /**
         * Listener for the location document ({@link #mLocationRef}).
     **/
        @Override
        public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w(TAG, "location:onEvent", e);
                return;
            }

            onLocationDataLoaded(snapshot.toObject(LocationData.class));
        }

        private void onLocationDataLoaded(LocationData locationData) {
            timeView.setText(locationData.getCreated_at());
            accuracyView.setText("|精度：" + locationData.getAccuracy());
            latitudeView.setText("|緯度："+ locationData.getLatitude());
            longitudeView.setText("|経度："+ locationData.getLongitude());

        }

        private void hideKeyboard() {
            View view = getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

}
