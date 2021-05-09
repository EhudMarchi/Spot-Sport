package ehud.marchi.spotsport;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class SpotSportUtills {
    public static ArrayList<SportSpotData> spots = new ArrayList<>();
    public static ArrayList<SportSpotData> favSpots = new ArrayList<>();
    public static ArrayList<SportSpotData> basketball = new ArrayList<>();
    public static ArrayList<SportSpotData> combine = new ArrayList<>();
    public static ArrayList<SportSpotData> dance = new ArrayList<>();
    public static ArrayList<SportSpotData> diving = new ArrayList<>();
    public static ArrayList<SportSpotData> general = new ArrayList<>();
    public static ArrayList<SportSpotData> gym = new ArrayList<>();
    public static ArrayList<SportSpotData> karate = new ArrayList<>();
    public static ArrayList<SportSpotData> outdoor = new ArrayList<>();
    public static ArrayList<SportSpotData> sea = new ArrayList<>();
    public static ArrayList<SportSpotData> skate = new ArrayList<>();
    public static ArrayList<SportSpotData> soccer = new ArrayList<>();
    public static ArrayList<SportSpotData> stadiumBig = new ArrayList<>();
    public static ArrayList<SportSpotData> stadiumSmall = new ArrayList<>();
    public static ArrayList<SportSpotData> stadiumMed = new ArrayList<>();
    public static ArrayList<SportSpotData> swim = new ArrayList<>();
    public static ArrayList<SportSpotData> tennis = new ArrayList<>();
    public static ArrayList<SportSpotData> volleyball = new ArrayList<>();
    public  static GoogleMap mMap;
    public  static double latitude, longitude;

    public static double getKmFromLatLong(double lat1, double lng1, double lat2, double lng2) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        double distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters / 1000;
    }
    public static Address getAddress(Context context, LatLng latLng)
    {
        Address address = null;
        List<Address> addressList = null;
        if (latLng != null) {
            Geocoder geocoder = new Geocoder(context, new Locale("iw"));
            try {
                addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            address = addressList.get(0);
        }

        return address;
    }

}
