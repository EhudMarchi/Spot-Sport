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
import android.widget.ImageButton;
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

import java.util.ArrayList;

public class MapsFragment extends Fragment {
    private ClusterManager<SportSpotData> clusterManager;
    Thread addSpotsThread;
    private float radius = 0;
    SeekBar radiusSeekbar;
    TextView radiusText, openFilter, closeFilter;
    ArrayList<ImageButton> filters = new ArrayList<>();
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
                    clusterManager.addItems(SpotSportUtills.basketball);
                    clusterManager.addItems(SpotSportUtills.combine);
                    clusterManager.addItems(SpotSportUtills.dance);
                    clusterManager.addItems(SpotSportUtills.diving);
                    clusterManager.addItems(SpotSportUtills.general);
                    clusterManager.addItems(SpotSportUtills.gym);
                    clusterManager.addItems(SpotSportUtills.karate);
                    clusterManager.addItems(SpotSportUtills.outdoor);
                    clusterManager.addItems(SpotSportUtills.sea);
                    clusterManager.addItems(SpotSportUtills.skate);
                    clusterManager.addItems(SpotSportUtills.soccer);
                    clusterManager.addItems(SpotSportUtills.stadiumBig);
                    clusterManager.addItems(SpotSportUtills.stadiumMed);
                    clusterManager.addItems(SpotSportUtills.stadiumSmall);
                    clusterManager.addItems(SpotSportUtills.swim);
                    clusterManager.addItems(SpotSportUtills.tennis);
                    clusterManager.addItems(SpotSportUtills.volleyball);
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
        initializeFilters();
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
        openFilter = getActivity().findViewById(R.id.open_filter);
        closeFilter = getActivity().findViewById(R.id.close_filter);
        openFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(openFilter.getVisibility()== View.VISIBLE)
                {
                    openFilters();
                    closeFilter.setVisibility(View.VISIBLE);
                }

            }
        });

        closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(closeFilter.getVisibility()== View.VISIBLE)
                {
                    closeFilters();
                    openFilter.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void closeFilters() {
        closeFilter.setVisibility(View.GONE);
        for (ImageButton filter:filters) {
            filter.setVisibility(View.GONE);
        }
    }

    private void openFilters() {
        openFilter.setVisibility(View.GONE);
        for (ImageButton filter:filters) {
            filter.setVisibility(View.VISIBLE);
        }
    }
    private void initializeFilters() {
        filters.add(getActivity().findViewById(R.id.basketball_filter));
        filters.add(getActivity().findViewById(R.id.combine_filter));
        filters.add(getActivity().findViewById(R.id.dance_filter));
        filters.add(getActivity().findViewById(R.id.diving_filter));
        filters.add(getActivity().findViewById(R.id.general_filter));
        filters.add(getActivity().findViewById(R.id.gym_filter));
        filters.add(getActivity().findViewById(R.id.karate_filter));
        filters.add(getActivity().findViewById(R.id.outdoor_gym_filter));
        filters.add(getActivity().findViewById(R.id.sea_filter));
        filters.add(getActivity().findViewById(R.id.skate_filter));
        filters.add(getActivity().findViewById(R.id.soccer_filter));
        filters.add(getActivity().findViewById(R.id.stadium_big_filter));
        filters.add(getActivity().findViewById(R.id.staduim_medium_filter));
        filters.add(getActivity().findViewById(R.id.stadium_small_filter));
        filters.add(getActivity().findViewById(R.id.swim_filter));
        filters.add(getActivity().findViewById(R.id.tennis_filter));
        filters.add(getActivity().findViewById(R.id.volleyball_filter));
        filters.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(0).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.basketball) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(0).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.basketball);
                    filters.get(0).setAlpha(1f);
                }
            }
        });
        filters.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(1).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.combine) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(1).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.combine);
                    filters.get(1).setAlpha(1f);
                }
            }
        });
        filters.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(2).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.dance) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(2).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.dance);
                    filters.get(2).setAlpha(1f);
                }
            }
        });
        filters.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(3).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.diving) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(3).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.diving);
                    filters.get(3).setAlpha(1f);
                }
            }
        });
        filters.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(4).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.general) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(4).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.general);
                    filters.get(4).setAlpha(1f);
                }
            }
        });
        filters.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(5).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.gym) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(5).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.gym);
                    filters.get(5).setAlpha(1f);
                }
            }
        });
        filters.get(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(6).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.karate) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(6).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.karate);
                    filters.get(6).setAlpha(1f);
                }
            }
        });
        filters.get(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(7).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.outdoor) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(7).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.outdoor);
                    filters.get(7).setAlpha(1f);
                }
            }
        });
        filters.get(8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(8).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.sea) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(8).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.sea);
                    filters.get(8).setAlpha(1f);
                }
            }
        });
        filters.get(9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(9).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.skate) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(9).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.skate);
                    filters.get(9).setAlpha(1f);
                }
            }
        });
        filters.get(10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(10).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.soccer) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(10).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.soccer);
                    filters.get(10).setAlpha(1f);
                }
            }
        });
        filters.get(11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(11).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.stadiumBig) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(11).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.stadiumBig);
                    filters.get(11).setAlpha(1f);
                }
            }
        });
        filters.get(12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(12).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.stadiumMed) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(12).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.stadiumMed);
                    filters.get(12).setAlpha(1f);
                }
            }
        });
        filters.get(13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(13).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.stadiumSmall) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(13).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.stadiumSmall);
                    filters.get(13).setAlpha(1f);
                }
            }
        });
        filters.get(14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(14).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.swim) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(14).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.swim);
                    filters.get(14).setAlpha(1f);
                }
            }
        });
        filters.get(15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(15).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.tennis) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(15).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.tennis);
                    filters.get(15).setAlpha(1f);
                }
            }
        });
        filters.get(16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filters.get(16).getAlpha() == 1f)
                {
                    for (SportSpotData item:SpotSportUtills.volleyball) {
                        clusterManager.removeItem(item);
                    }
                    filters.get(16).setAlpha(0.5f);
                }
                else
                {
                    clusterManager.addItems(SpotSportUtills.volleyball);
                    filters.get(16).setAlpha(1f);
                }
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