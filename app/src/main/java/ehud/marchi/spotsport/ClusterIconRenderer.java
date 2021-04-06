package ehud.marchi.spotsport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

class ClusterIconRenderer extends DefaultClusterRenderer<SportSpotData> {
    Context context;
    public ClusterIconRenderer(Context context, GoogleMap map,
                               ClusterManager<SportSpotData> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }


    @Override
    protected void onBeforeClusterItemRendered(SportSpotData item, MarkerOptions markerOptions) {
        BitmapDescriptor tempIcon = BitmapFromVector(context,item.drawableSport);
        markerOptions.icon(tempIcon);
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // create a bitmap for our drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // draw our vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}