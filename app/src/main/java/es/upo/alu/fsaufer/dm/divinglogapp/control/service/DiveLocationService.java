package es.upo.alu.fsaufer.dm.divinglogapp.control.service;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.control.db.AppRepository;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.DiveLocation;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Servicio de gestión de localizaciones geográficas.
 */
public class DiveLocationService {

    private static final float MAX_DISTANCE_METERS = 5000; // Cinco kilómetros
    private static final float MIN_DISTANCE_METERS = 1000; // Un kilómetro
    private static final long MIN_TIME_MS = 360000; // Una hora

    private static LocationManager locationManager = null;
    private static Location currentGeoLocation = null;

    public static void init(Context context) {
        if (locationManager == null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PERMISSION_GRANTED) {
                locationManager =
                        (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                currentGeoLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                LocationListener locationListener = location -> {
                    synchronized (location) {
                        currentGeoLocation.setLongitude(location.getLongitude());
                        currentGeoLocation.setLatitude(location.getLatitude());
                    }
                };

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_MS, MIN_DISTANCE_METERS, locationListener);
            } else {
                Toast.makeText(context, R.string.geo_permission, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static synchronized boolean isLocationAvailable() {
        return currentGeoLocation != null;
    }

    public static synchronized Location getCurrentGeoLocation() {
        return currentGeoLocation;
    }

    public static String getNearestLocationName() {
        if (isLocationAvailable()) {
            String nearestLocationName = null;
            Location start = getCurrentGeoLocation();
            Location location = new Location(Constant.LOCATION_PROVIDER);
            float minDistanceTo = Float.MAX_VALUE;

            for (DiveLocation diveLocation : AppRepository.getLocationMap().values()) {
                location.setLongitude(diveLocation.getLocation().getLongitude());
                location.setLatitude(diveLocation.getLocation().getLatitude());
                float distance = start.distanceTo(location);
                if (distance < minDistanceTo) {
                    minDistanceTo = distance;

                    nearestLocationName = diveLocation.getName();
                }
            }

            if (minDistanceTo < MAX_DISTANCE_METERS) {
                return nearestLocationName;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}