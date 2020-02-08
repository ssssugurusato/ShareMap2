package com.example.sharemap2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

            findViewById(R.id.save).setOnClickListener(this);

            // Get restaurant ID from extras
            String restaurantId = getIntent().getExtras().getString(KEY_RESTAURANT_ID);
            if (restaurantId == null) {
                throw new IllegalArgumentException("Must pass extra " + KEY_RESTAURANT_ID);
            }

            // Initialize Firestore
            mFirestore = FirebaseFirestore.getInstance();
        }

        @Override
        public void onStart() {
            super.onStart();

            mRatingAdapter.startListening();
            mRestaurantRegistration = mRestaurantRef.addSnapshotListener(this);
        }

        @Override
        public void onStop() {
            super.onStop();

            mRatingAdapter.stopListening();

            if (mRestaurantRegistration != null) {
                mRestaurantRegistration.remove();
                mRestaurantRegistration = null;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.restaurant_button_back:
                    onBackArrowClicked(v);
                    break;
                case R.id.fab_show_rating_dialog:
                    onAddRatingClicked(v);
                    break;
            }
        }

        /**
         * Listener for the Restaurant document ({@link #mRestaurantRef}).
         */
        @Override
        public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w(TAG, "restaurant:onEvent", e);
                return;
            }

            onRestaurantLoaded(snapshot.toObject(Restaurant.class));
        }

        private void onRestaurantLoaded(Restaurant restaurant) {
            mNameView.setText(restaurant.getName());
            mRatingIndicator.setRating((float) restaurant.getAvgRating());
            mNumRatingsView.setText(getString(R.string.fmt_num_ratings, restaurant.getNumRatings()));
            mCityView.setText(restaurant.getCity());
            mCategoryView.setText(restaurant.getCategory());
            mPriceView.setText(RestaurantUtil.getPriceString(restaurant));

            // Background image
            Glide.with(mImageView.getContext())
                    .load(restaurant.getPhoto())
                    .into(mImageView);
        }

        private void hideKeyboard() {
            View view = getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

}
