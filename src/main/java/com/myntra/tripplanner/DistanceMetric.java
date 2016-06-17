package com.myntra.tripplanner;

import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;

public class DistanceMetric implements DistanceMeasure {
	private static final long serialVersionUID = 1L;
	private static final String APIKey = "AIzaSyCYmcAVcCdKIwgJAEte4NFK5cnpiJ1ekGs";
	private static final String APIKey2 = "AIzaSyASQ_n0CyqfuPCKY3HazLcDyskkhINr_II";
	static int requestCount = 0;

	@Override
	public boolean compare(double x, double y) {
		// TODO Auto-generated method stub
		return x < y;
	}

	@Override
	public double getMaxValue() {
		// TODO Auto-generated method stub
		return 999999;
	}

	@Override
	public double getMinValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double measure(Instance x, Instance y) {
		// TODO Auto-generated method stub
		requestCount += 1;
		double lat1 = x.get(0);
		double lon1 = x.get(1);
		double lat2 = y.get(0);
		double lon2 = y.get(1);
		double dist = geoDist(lat1, lon1, lat2, lon2);
		if (dist < 0) {
			//Haversine Distance
			double theta = lon1 - lon2;
			dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
			dist = Math.acos(dist);
			dist = rad2deg(dist);
			dist = dist * 60 * 1.1515;
			dist = dist * 1.609344;
		}
		//		System.out.println("Distance b/n " + x.toString() + " and " + y.toString() + " is " + dist);
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	private double geoDist(double lat1, double lon1, double lat2, double lon2) {
		//		GeoApiContext context = new GeoApiContext().setApiKey(APIKey);
		//		LatLng origins = new LatLng(lat1,lon1);
		//		LatLng destinations = new LatLng(lat2,lon2);
		//		DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
		//		        .origins(origins)
		//		        .destinations(destinations)
		//		        .mode(TravelMode.DRIVING)
		//		        .awaitIgnoreError();
		long dist = -1;
		//		if (matrix.rows[0].elements[0].status == DistanceMatrixElementStatus.OK) {
		//			dist = matrix.rows[0].elements[0].distance.inMeters;
		//		}

		return dist;
	}
}
