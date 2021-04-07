package ehud.marchi.spotsport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

public class MapsFragment extends Fragment {
    private ClusterManager<SportSpotData> clusterManager;
    Thread addSpotsThread;
    private float radius = 0;
    SeekBar radiusSeekbar;
    TextView radiusText;
    Circle circle;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            SpotSportUtills.mMap = googleMap;
            setUpClusterer();
            ClusterIconRenderer renderer = new ClusterIconRenderer(getContext(), SpotSportUtills.mMap, clusterManager);
            clusterManager.setRenderer(renderer);
            SpotSportUtills.mMap.setOnCameraMoveListener(renderer);
            addSpotsThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    for (SportSpotData spot : SpotSportUtills.spots) {
                            clusterManager.addItem(spot);
                    }
                }
            };
            addSpotsThread.start();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        radiusText = getActivity().findViewById(R.id.radius_text);
        radiusSeekbar = getActivity().findViewById(R.id.radius);
        radiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = radiusSeekbar.getProgress();
                if(circle!=null) {
                    circle.remove();
                }
                addCircle(radius);
                radiusText.setText("Radius: "+(int)radius+" meters");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setUpClusterer() {
        // Position the map.
        SpotSportUtills.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.78006283, 34.636054), 9.8f));
        // Initialize the manager with the context and the map.
        clusterManager = new ClusterManager<SportSpotData>(getContext(), SpotSportUtills.mMap);
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        SpotSportUtills.mMap.setOnCameraIdleListener(clusterManager);
        SpotSportUtills.mMap.setOnMarkerClickListener(clusterManager);
    }
    private void addCircle(float radius)
    {
        LatLng temp = new LatLng(SpotSportUtills.latitude, SpotSportUtills.longitude);
        circle = SpotSportUtills.mMap.addCircle(new CircleOptions().center(temp).radius(radius).strokeColor(R.color.red).fillColor(R.color.yellow_soft));
    }
}