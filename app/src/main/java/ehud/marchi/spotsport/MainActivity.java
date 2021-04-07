package ehud.marchi.spotsport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAccessorAdapter tabAccessorAdapter;
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private float radius;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabAccessorAdapter = new TabAccessorAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAccessorAdapter);

        Thread readSpotsThread = new Thread() {
            @Override
            public void run() {
                super.run();
                readData();
            }
        };
        readSpotsThread.start();

    }

    private void readData() {
        InputStream is = getResources().openRawResource(R.raw.sport_spots);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            reader.readLine();
            //for (int i = 0; i < 2250; i++) {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                line = reader.readLine();
                if (line != null) {
                    String[] tokens = line.split(",");
                    try {
                        SportSpotData spotData = new SportSpotData(tokens[1], tokens[2], tokens[0], Double.parseDouble(tokens[10]), Double.parseDouble(tokens[11]));
                        SpotSportUtills.spots.add(spotData);
                        Log.d("MainActivity", "Name: " + spotData.getPlaceName());
                        Log.d("MainActivity", "Name: " + spotData.getSpotName());
                        Log.d("MainActivity", "X: " + spotData.getX());
                        Log.d("MainActivity", "Y: " + spotData.getY());
                    } catch (Exception e) {
                        Log.d("MainActivity", "exeption: " + e.getMessage());
                    }
                }
            }
            //}
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            } else startLocation();
        } else startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Sorry can't work", Toast.LENGTH_SHORT).show();
            } else startLocation();
        }
    }

    private void startLocation() {

        client = LocationServices.getFusedLocationProviderClient(this);

        LocationCallback callback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location lastLocation = locationResult.getLastLocation();
                SpotSportUtills.latitude = lastLocation.getLatitude();
                SpotSportUtills.longitude = lastLocation.getLongitude();
                LatLng temp = new LatLng(SpotSportUtills.latitude, SpotSportUtills.longitude);
                float zoomLevel = 13.8f;
                SpotSportUtills.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp,zoomLevel));
                SpotSportUtills.mMap.addMarker(new MarkerOptions().position(temp).title("My Location").snippet(SpotSportUtills.getAddress(MainActivity.this,temp).getAddressLine(0)));
                Circle circle = SpotSportUtills.mMap.addCircle(new CircleOptions().center(temp).radius(1000).strokeColor(R.color.red).fillColor(R.color.yellow_soft));
                circle.remove();
            }
        };
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(Build.VERSION.SDK_INT>=23 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            client.requestLocationUpdates(request,callback,null);
        else if(Build.VERSION.SDK_INT<=22)
            client.requestLocationUpdates(request,callback,null);
    }
}