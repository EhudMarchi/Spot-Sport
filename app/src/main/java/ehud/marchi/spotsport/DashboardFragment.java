package ehud.marchi.spotsport;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private RecyclerView spotsRecyclerView;
    private SpotAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    ArrayList<SportSpotData> spots;

    public DashboardFragment() {
        // Required empty public constructor
    }
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSpotsRecyclerView();
    }

    private void setUpSpotsRecyclerView() {
        spotsRecyclerView = getView().findViewById(R.id.spots_recyclerview);
        spotsRecyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAdapter = new SpotAdapter(getContext(),SpotSportUtills.spots);
        spotsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        spotsRecyclerView.setAdapter(recyclerViewAdapter);
    }
}