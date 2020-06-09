package com.application.dsiadminpanel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.application.dsiadminpanel.dataClass.EmployeeLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.application.dsiadminpanel.Common.Constants.DB;

public class trackEmployeeOnMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    String post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_employee_on_map);

        post = getIntent().getStringExtra("Post");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;


        DB.child("Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gMap.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        EmployeeLocation employeeLocation = ds.getValue(EmployeeLocation.class);
                        if (employeeLocation != null && employeeLocation.getPost().equals(post)) {
                            LatLng location = new LatLng(Double.parseDouble(employeeLocation.getLatitude()), Double.parseDouble(employeeLocation.getLongitude()));
                            gMap.addMarker(new MarkerOptions().position(location).title(employeeLocation.getName()));
                            gMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //When Network is not Present
            }
        });
    }
}
