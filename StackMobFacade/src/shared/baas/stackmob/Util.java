package shared.baas.stackmob;

import com.parse.ParseGeoPoint;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Util {
	private static LocationListener locationListener;

	public interface Callback {

		public void done(ParseGeoPoint parseGeoPoint);

	}

	public static void geoPointInBackground(Context ctx, final Callback callback) {
		// Acquire a reference to the system Location Manager
		final LocationManager locationManager = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);

		final Location loc = locationManager
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if (loc != null) {
			callback.done(new ParseGeoPoint(loc.getLatitude(), loc.getLongitude()));
			return;
		}

		locationListener = new LocationListener() {
			public void onLocationChanged(Location loc) {
				// Called when a new location is found by the network location
				// provider.
				callback.done(new ParseGeoPoint(loc.getLatitude(), loc.getLongitude()));
				if (locationListener != null)
					locationManager.removeUpdates(locationListener);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
}
