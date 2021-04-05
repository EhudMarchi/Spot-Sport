package ehud.marchi.spotsport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;
    private ClusterManager<SportSpotData> clusterManager;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {



        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            setUpClusterer();
            BitmapDescriptor tempIcon;
            int iconID;
            clusterManager.setRenderer(new ClusterIconRenderer(getContext(), mMap, clusterManager));             
            Thread thread = new Thread(){
                @Override
                public void run() {
                    super.run();

            for (SportSpotData spot : SpotSportUtills.spots) {
                //LatLng temp = spot.getLatLng();
            //tempIcon = BitmapFromVector(getContext(),spot.drawableSport);
            //mMap.addMarker(new MarkerOptions().position(temp).title(spot.getPlaceName()+" "+spot.getSpotName()).icon(tempIcon));
             //clusterManager.setRenderer(spot.drawableSport);

            clusterManager.addItem(spot);
            }
                }
            };
            thread.start();
            LatLng temp = new LatLng(31.78006283,34.636054);
            float zoomLevel = 10.0f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp,zoomLevel));
        }
    };
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
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
    }
    private void setUpClusterer() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = new ClusterManager<SportSpotData>(getContext(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

        // Add cluster items (markers) to the cluster manager.
    }

//    private void addItems() {
//
//        // Set some lat/lng coordinates to start with.
//        double lat = 51.5145160;
//        double lng = -0.1270060;
//
//        // Add ten cluster items in close proximity, for purposes of this example.
//        for (int i = 0; i < 10; i++) {
//            double offset = i / 60d;
//            lat = lat + offset;
//            lng = lng + offset;
//            SportSpotData offsetItem = new SportSpotData(lat, lng, "Title " + i, "Snippet " + i);
//            clusterManager.addItem(offsetItem);
//        }
//    }
}