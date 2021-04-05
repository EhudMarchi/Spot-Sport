package ehud.marchi.spotsport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {
    private ArrayList<SportSpotData> spots;
    private SportSpotData selectedSpot = null;
    Context m_Context;
    int m_SelectedItemIndex = 0;

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
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }


}
