package com.myntra.tripplanner;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;

public class Cluster {
	Dataset[] getClusters(int noOfClusters, Dataset data) {
		System.out.println("Clustering.....");
		Clusterer km = new KMeans( noOfClusters, 100, new DistanceMetric() );
		Dataset[] clusters = km.cluster(data);
		System.out.println("");
		System.out.println("Writing data to output..... ");
		System.out.println("Distance matrix api call requested: " + DistanceMetric.requestCount + " times");
		System.out.println("");
		return clusters;
		//		new DisplayGraph().display(noOfClusters, clusters);
	}
}
