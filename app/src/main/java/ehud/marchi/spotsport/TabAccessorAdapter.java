package ehud.marchi.spotsport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessorAdapter extends FragmentPagerAdapter {

    public TabAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case  0:
                DashboardFragment dashboardFragment = new DashboardFragment();
                return  dashboardFragment;
            case  1:
                MapsFragment mapsFragment = new MapsFragment();
                return  mapsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case  0:
                return  "Dashboard";
            case  1:
                return  "Maps";
            default:
                return null;
        }
    }
}
