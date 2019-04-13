package hr.foi.core.tourschedulelocation;

import java.util.HashMap;

/**
 * Created by Mateo on 24.1.2018..
 */

public interface TourDisplayModule {
    void showTour(double destinationLat, double destinationLng, String address);
    void setAppModule(AppModule appModule);
    void fragmentShown();
    void fragmentRemoved();
}
