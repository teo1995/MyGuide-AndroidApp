package hr.foi.tourschedulebasicdisplay;
import hr.foi.core.tourschedulelocation.AppModule;
import hr.foi.core.tourschedulelocation.TourDisplayModule;
/**
 * Created by Mateo on 25.1.2018..
 */

public class BasicDisplayTour implements TourDisplayModule {
    BasicDisplayTourFragment basicDisplayTourFragment;
    AppModule appModule;
    double destinationLat;
    double destinationLng;
    String address;

    @Override
    public void showTour(double destinationLat, double destinationLng, String address) {
        this.destinationLat = destinationLat;
        this.destinationLng  = destinationLng;
        this.address = address;
        basicDisplayTourFragment = new BasicDisplayTourFragment();
        //appModule.removeDisplayTourFragment();
        appModule.displayTourFragment(basicDisplayTourFragment);
    }

    @Override
    public void setAppModule(AppModule appModule) {
        this.appModule = appModule;
    }

    @Override
    public void fragmentShown() {
        basicDisplayTourFragment.setTourData(destinationLat, destinationLng, address);
    }

    @Override
    public void fragmentRemoved() {

    }
}

