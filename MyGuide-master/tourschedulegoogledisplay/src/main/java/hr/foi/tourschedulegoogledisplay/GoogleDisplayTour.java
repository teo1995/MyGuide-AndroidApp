package hr.foi.tourschedulegoogledisplay;

import hr.foi.core.tourschedulelocation.AppModule;
import hr.foi.core.tourschedulelocation.TourDisplayModule;

/**
 * Created by Mateo on 25.1.2018..
 */

public class GoogleDisplayTour implements TourDisplayModule {
    GoogleDisplayTourFragment googleDisplayTourFragment;
    AppModule appModule;
    double destinationLat;
    double destinationLng;
    String address;

    @Override
    public void showTour(double destinationLat, double destinationLng, String address) {
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
        this.address = address;
        googleDisplayTourFragment = new GoogleDisplayTourFragment();
        appModule.displayTourFragment(googleDisplayTourFragment);
    }

    @Override
    public void setAppModule(AppModule appModule) {
        this.appModule = appModule;
    }

    @Override
    public void fragmentShown() {
        googleDisplayTourFragment.setTourData(destinationLat, destinationLng, address);
    }

    @Override
    public void fragmentRemoved() {

    }
}
