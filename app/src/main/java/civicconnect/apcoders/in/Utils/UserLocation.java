package civicconnect.apcoders.in.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import es.dmoral.toasty.Toasty;

public class UserLocation {
    public interface LocationCallback {
        void onLocationReceived(double latitude, double longitude);

        void onLocationError(String errorMessage);
    }

    public UserLocation() {

    }

    public static void GetCurrentLocation(Context context, LocationCallback callback) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                callback.onLocationReceived(latitude, longitude);
                                Log.d("Current Location : ", "Latitude: " + latitude + ", Longitude: " + longitude);
//                                Toast.makeText(context,
//                                        "Latitude: " + latitude + ", Longitude: " + longitude,
//                                        Toast.LENGTH_LONG).show();
                            } else {
                                String errorMessage = "Location not found. Make sure location is enabled on the device.";
                                callback.onLocationError(errorMessage);
                                Toasty.info(context,
                                        "Location not found. Make sure location is enabled on the device.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            callback.onLocationError("Location permission not granted");
        }

    }

}

