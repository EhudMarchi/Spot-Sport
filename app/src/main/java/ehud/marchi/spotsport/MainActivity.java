package ehud.marchi.spotsport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabAccessorAdapter = new TabAccessorAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAccessorAdapter);

        Thread readSpotsThread = new Thread(){
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
                        SportSpotData spotData = new SportSpotData(tokens[1],tokens[2],tokens[0], Double.parseDouble(tokens[10]), Double.parseDouble(tokens[11]));
                        //spotData.setX(Double.parseDouble(tokens[10]));
                       //spotData.setY(Double.parseDouble(tokens[11]));
                        SpotSportUtills.spots.add(spotData);
                        Log.d("MainActivity", "Name: " + spotData.getPlaceName());
                        Log.d("MainActivity", "Name: " + spotData.getSpotName());
                        Log.d("MainActivity", "X: " + spotData.getX());
                        Log.d("MainActivity", "Y: " + spotData.getY());
                    }
                    catch (Exception e)
                    {
                        Log.d("MainActivity", "exeption" + e.getMessage());
                    }
                }
            }
            //}
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }
    }
}