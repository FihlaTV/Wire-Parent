package com.shubzz.wireparent;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;
    private GoogleMap mMap;
    private String latitude = "0";
    private String longitude = "0";
    private ImageButton speedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        latitude = intent.getStringExtra("Latitude");
        longitude = intent.getStringExtra("Longitude");
        speedButton = findViewById(R.id.speedButton);
        //Log.d("requestLL",latitude+" "+longitude);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpeed();
                //Toast.makeText(MapsActivity.this,"Clicked",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showSpeed() {

//        Intent intent = new Intent(this, SpeedActivity.class);
//        startActivity(intent);
        SpeedDialog speedDialog = new SpeedDialog();
        speedDialog.setContext(this);
        speedDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Add a marker in Sydney and move the camera
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map);
        mMap.setMapStyle(mapStyleOptions);
        LatLng mark = new LatLng(Double.parseDouble(longitude), Double.parseDouble(latitude));
        mMap.addMarker(new MarkerOptions().position(mark).title("Marker")).setFlat(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        //animateCamera(-34.0,151.0);
    }

    private void animateCamera(Double Latitude, Double Longitude) {
        LatLng latLng = new LatLng(Latitude, Longitude);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));
    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Permission denied by uses", Toast.LENGTH_SHORT).show();
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCurrentLocationUpdates();
        }
    }

    private void startCurrentLocationUpdates() {
        Toast.makeText(this, "Permission Grant", Toast.LENGTH_SHORT).show();
    }
}
