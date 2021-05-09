package ehud.marchi.spotsport;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

public class SportSpotData implements ClusterItem, Serializable {
    String placeName;
    String city;
    String type;
    String spotName;
    double x, y;
    LatLng latLng;
    int drawableSport;

    public SportSpotData() {
    }
    public SportSpotData(String spotName, String placeName, String city , double x, double y) {
        this.spotName = spotName;
        this.placeName = placeName;
        this.city = city;
        this.latLng = new LatLng(x,y);
        generateDrawable();
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getDrawableSport() {
        return drawableSport;
    }

    public void setDrawableSport(int drawableSport) {
        this.drawableSport = drawableSport;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    private void generateDrawable()
    {
        if (this.spotName.contains("משולב")||this.placeName.contains("משולב"))
        {
            this.drawableSport = R.drawable.combine;
        }
        else if(this.spotName.contains("כדורסל")||this.placeName.contains("כדורסל"))
        {
            this.drawableSport = R.drawable.basketball;
        }

        else if (this.spotName.contains("ג'ודו")||this.placeName.contains("האבקות"))
        {
            this.drawableSport = R.drawable.karate;
        }
        else if (this.spotName.contains("שחייה")||this.spotName.contains("שחיה"))
        {
            this.drawableSport = R.drawable.swim;
        }
        else if (this.spotName.contains("כדורגל")||this.placeName.contains("כדורגל"))
        {
            this.drawableSport = R.drawable.soccer;
        }
        else if (this.spotName.contains("טניס")||this.placeName.contains("טניס"))
        {
            this.drawableSport = R.drawable.tennis;
        }
        else if (this.spotName.contains("כדורעף")||this.placeName.contains("כדורעף"))
        {
            this.drawableSport = R.drawable.volleyball;
        }
        else if (this.spotName.contains("פתוח")||this.placeName.contains("פתוח"))
        {
            this.drawableSport = R.drawable.outdoor_gym;
        }
        else if (this.spotName.contains("כושר"))
        {
            this.drawableSport = R.drawable.gym;
        }
        else if (this.spotName.contains("אולם"))
        {
            if (this.spotName.contains("קטן")) {
                this.drawableSport = R.drawable.stadium_small;
            }
            else if (this.spotName.contains("בינוני")) {
                this.drawableSport = R.drawable.staduim_medium;
            }
            else
            {
                this.drawableSport = R.drawable.stadium_big;
            }
        }
        else if (this.spotName.contains("מגרש")||this.placeName.contains("מגרש"))
        {
            this.drawableSport = R.drawable.combine;
        }
        else if (this.spotName.contains("סקייט")||this.placeName.contains("סקייט")||this.spotName.contains("אקסטרים")||this.placeName.contains("אקסטרים"))
        {
            this.drawableSport = R.drawable.skate;
        }
        else if (this.spotName.contains("מחול")||this.placeName.contains("מחול"))
        {
            this.drawableSport = R.drawable.dance;
        }
        else if (this.spotName.contains("צלילה")||this.placeName.contains("צלילה"))
        {
            this.drawableSport = R.drawable.diving;
        }
        else if (this.spotName.contains("ימי")||this.placeName.contains("ימי")||this.spotName.contains("ים")||this.placeName.contains("ים"))
        {
            this.drawableSport = R.drawable.sea;
        }
        else
        {
            this.drawableSport = R.drawable.general;
        }
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return spotName;
    }

    @Override
    public String getSnippet() {
        return placeName;
    }

}
