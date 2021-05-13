package ehud.marchi.spotsport;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> implements Filterable {
    private ArrayList<SportSpotData> spots;
    private SportSpotData selectedSpot = null;
    Context m_Context;
    int m_SelectedItemIndex = 0;
    private ArrayList<SportSpotData> m_Allm_Spots;
    public class SpotViewHolder extends RecyclerView.ViewHolder {
        public ImageView sportImage;
        public TextView spotName,spotCity, spotType;
        View rowView;
        public SpotViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            sportImage = itemView.findViewById(R.id.sport_image);
            spotName = itemView.findViewById(R.id.name);
            spotCity = itemView.findViewById(R.id.city);
            spotType = itemView.findViewById(R.id.type);
            rowView = itemView;
        }
    }

    public SpotAdapter(Context context, ArrayList<SportSpotData> spotsList) {
        spots = spotsList;
        m_Allm_Spots = new ArrayList<>(spotsList);
        this.m_Context = context;
    }

    @Override
    public SpotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View gamesView = inflater.inflate(R.layout.spot_item, parent, false);
        SpotViewHolder viewHolder = new SpotViewHolder(gamesView);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final SpotViewHolder holder, final int position) {
        SportSpotData currentSpot = spots.get(position);
        Glide.with(m_Context).load(currentSpot.getDrawableSport()).into(holder.sportImage);
        holder.spotCity.setText(currentSpot.getCity());
        holder.spotName.setText(currentSpot.getPlaceName());
        holder.spotType.setText(currentSpot.getSpotName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(m_Context);
                dialog.setContentView(R.layout.dialog_spot);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView name = dialog.findViewById(R.id.name);
                TextView type = dialog.findViewById(R.id.type);
                TextView distance = dialog.findViewById(R.id.distance);
                ImageView icon = dialog.findViewById(R.id.icon);
                name.setText(currentSpot.getPlaceName());
                type.setText(currentSpot.getSpotName());
                // displaying location distance from user
                distance.setText(new DecimalFormat("##.##").format(SpotSportUtills.getKmFromLatLong(currentSpot.getLatLng().latitude,currentSpot.getLatLng().longitude,SpotSportUtills.latitude,SpotSportUtills.longitude))+" Km away from you");
                Glide.with(m_Context).load(currentSpot.getDrawableSport()).into(icon);
                // displaying full address
                TextView addressTextView = dialog.findViewById(R.id.address);
                Address address = SpotSportUtills.getAddress(m_Context,currentSpot.getLatLng());
                addressTextView.setText(address.getAddressLine(0));
                Button cancel =dialog.findViewById(R.id.cancel_btn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button yes =dialog.findViewById(R.id.yes_btn);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SpotSportUtills.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentSpot.getLatLng(),17.8f));//zoom in location
                        MainActivity.viewPager.setCurrentItem(1);// switch to maps fragment
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }
    @Override
    public Filter getFilter() {
        return spotsFilter;
    }
    private Filter spotsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SportSpotData> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(m_Allm_Spots);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SportSpotData spotData: m_Allm_Spots) {
                    if ((spotData.getCity()+" "+spotData.getSpotName()+" "+spotData.getPlaceName()).toLowerCase().contains(filterPattern)) {
                        filteredList.add(spotData);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            spots.clear();
            spots.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
