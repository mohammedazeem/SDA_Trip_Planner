package com.myntra.tripplanner;

public class LatLong {
	double latitude;
	double longitude;

	LatLong() {
		latitude = 0.0;
		longitude = 0.0;
	}

	LatLong(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}

	void setLatitude(double lat) {
		latitude = lat;
	}

	void setLongitude(double lon) {
		longitude = lon;
	}

	double getLatitude() {
		return latitude;
	}

	double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return (getLatitude() + ", " + getLongitude());
	}

	boolean isZero() {
		if (getLatitude() == 0 && getLongitude() == 0)
			return true;
		else
			return false;
	}

}
