package ehud.marchi.spotsport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

class ClusterIconRenderer extends DefaultClusterRenderer<SportSpotData>implements GoogleMap.OnCameraMoveListener {
    Context context;
    private GoogleMap mMap;
    private float currentZoomLevel, maxZoomLevel =13.8f;
    public ClusterIconRenderer(Context context, GoogleMap map,
                               ClusterManager<SportSpotData> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        this.mMap = map;
    }

    @Override
    public void setMinClusterSize(int minClusterSize) {
        super.setMinClusterSize(minClusterSize);
    }
    @Override
    public void onCameraMove() {
        currentZoomLevel = mMap.getCameraPosition().zoom;
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<SportSpotData> cluster) {
        // determine if superclass would cluster first, based on cluster size
        boolean superWouldCluster = super.shouldRenderAsCluster(cluster);

        // if it would, then determine if you still want it to based on zoom level
        if (superWouldCluster) {
            superWouldCluster = currentZoomLevel <= maxZoomLevel;
        }

        return superWouldCluster;
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