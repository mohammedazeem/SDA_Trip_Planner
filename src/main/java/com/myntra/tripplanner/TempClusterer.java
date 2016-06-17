package com.myntra.tripplanner;

import java.util.ArrayList;
import java.util.Collections;

public class TempClusterer {
	double[][] distanceMatrix;
	int noOfPoints;
	static final int MAX_ITERATIONS = 100000;
	static int distanceCallCount = 0;

	ArrayList< ArrayList< LatLong > > getClusters(int noOfClusters, ArrayList< LatLong > dataPoints) {
		ArrayList< ArrayList< LatLong > > clusters = new ArrayList< ArrayList< LatLong > >();
		fillDistanceMatrix(dataPoints);
		ArrayList<Integer> newCenters = getRandomCentroids(noOfClusters);
		ArrayList<Integer> centers = null;
		int[] pointMembership = new int[noOfPoints];
		double[] clusterDistances = new double[noOfClusters];
		int iteration = 0;
		while( !equalLists(centers, newCenters) && iteration < MAX_ITERATIONS ){
			iteration += 1;
			centers = newCenters;
			double mean = 0;
			for (int i = 0; i < noOfClusters; i++)
				clusterDistances[i] = 0;
			mean = mean / noOfClusters;
			for (int i = 0; i < noOfPoints; i++) {
				for (int k = 0; k < noOfClusters; k++){
					mean += clusterDistances[k];
				}
				double[] meanDeviation = new double[noOfClusters];
				double min = Double.MAX_VALUE;
				int bestCluster = -1;
				double bestDist = 0.0;
				for (int j = 0; j < noOfClusters; j++){
					double dist = distanceMatrix[centers.get(j)][i];
					meanDeviation[j] = clusterDistances[j] + dist - mean;
					if (meanDeviation[j] < min){
						min = meanDeviation[j];
						bestCluster = j;
						bestDist = dist;
					}
				}
				pointMembership[i] = centers.get(bestCluster);
				clusterDistances[bestCluster] += bestDist;
			}
			newCenters = getCentroids(noOfClusters, pointMembership, centers, dataPoints);
		}
		for ( int i = 0; i < noOfClusters; i++ ) {
			clusters.add( new ArrayList<LatLong>() );
		}

		for ( int i = 0; i < noOfPoints; i++ ) {
			int center = pointMembership[i];
			int cluster = newCenters.indexOf(center);
			clusters.get(cluster).add( dataPoints.get(i) );
		}
		System.out.println("Number of iteration: " + iteration);
		System.out.println("Distance api calls: " + distanceCallCount);
		return clusters;
	}

	ArrayList<Integer> getCentroids(int noOfClusters, int[] pointMembership, ArrayList<Integer> oldCenters, ArrayList< LatLong > dataPoints){
		ArrayList<Integer> centers = new ArrayList<Integer>();
		for (int center : oldCenters) {
			LatLong centroid = new LatLong();
			ArrayList< Integer > instances = new ArrayList< Integer >();
			for ( int i = 0; i < noOfPoints; i++) {
				if (pointMembership[i] == center) {
					instances.add(i);
					centroid.setLatitude(centroid.getLatitude() + dataPoints.get(i).getLatitude());
				}
			}
			centroid.setLatitude(centroid.getLatitude() / instances.size());
			centroid.setLongitude(centroid.getLongitude() / instances.size());
			int tempCenter = -1;
			double min = Double.MAX_VALUE;
			for (int instance : instances) {
				double dist = getDistance(dataPoints.get(instance), centroid);
				if (dist < min) {
					min = dist;
					tempCenter = instance;
				}
			}
			centers.add(tempCenter);
		}
		return centers;
	}

	ArrayList<Integer> getRandomCentroids(int noOfClusters) {
		ArrayList<Integer> centers = new ArrayList<Integer>();
		for (int i = 0; i < noOfClusters; i++)
			centers.add( (int) (Math.random() * noOfPoints) );
		return centers;
	}

	public  boolean equalLists(ArrayList<Integer> a, ArrayList<Integer> b){
		if (a == null && b == null) return true;
		if ((a == null && b!= null) || (a != null && b== null) || (a.size() != b.size()) ){
			return false;
		}
		// Sort and compare the two lists
		Collections.sort(a);
		Collections.sort(b);
		return a.equals(b);
	}

	void fillDistanceMatrix( ArrayList< LatLong > dataPoints ) {
		noOfPoints = dataPoints.size();
		distanceMatrix = new double[noOfPoints][noOfPoints];
		for (int i = 0; i < noOfPoints; i++) {
			distanceMatrix[i][i]=0;
		}
		for (int i = 0; i < noOfPoints; i++)
			for (int j = 0; j < noOfPoints; j++){
				if ( i == j )
					continue;
				distanceMatrix[i][j] = distanceMatrix[j][i] = getDistance(dataPoints.get(i), dataPoints.get(j));
			}
	}

	double getDistance(LatLong x, LatLong y) {
		distanceCallCount += 1;
		double dist = 0.0;
		double lat1 = x.getLatitude();
		double lon1 = x.getLongitude();
		double lat2 = y.getLatitude();
		double lon2 = y.getLongitude();
		double theta = lon1 - lon2;
		dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344 * 1000;
		if (dist == 0.0) {
		}
		return dist;
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

}
