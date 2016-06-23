package com.myntra.tripplanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class Manager {
	private ArrayList< Address > addressList;
	private ArrayList< Address > unresolvedAddresses;
	private ArrayList< LatLong > geoLocationList;
	private int noOfStaffsAvailable;
	AccessDatabase DAO;

	Manager() {
	}

	void initialize() {
		addressList = new ArrayList< Address >();
		unresolvedAddresses = new ArrayList< Address >();
		noOfStaffsAvailable = 0;
		DAO = new AccessDatabase();
	}

	void dispose() {
		DAO.closeSessionFactory();
	}

	//	private void gatherData() {
	//		try {
	//			JsonParser jsonParser = new JsonParser();
	//			JsonObject jo = (JsonObject)jsonParser.parse(new JsonReader(new FileReader("data/data_dc_bangalore.json")));
	//			JsonArray jArray = jo.getAsJsonArray("data");
	//			for (JsonElement jElement : jArray) {
	//				JsonObject jAddObj = jElement.getAsJsonObject();
	//				Address address = new Address();
	//				address.setStreetAddress(jAddObj.getAsJsonPrimitive("address").getAsString());
	//				address.setLocality(jAddObj.getAsJsonPrimitive("locality").getAsString());
	//				address.setCity(jAddObj.getAsJsonPrimitive("city").getAsString());
	//				address.setPincode(jAddObj.getAsJsonPrimitive("zipcode").getAsString());
	//				String classLabel = String.valueOf( jAddObj.getAsJsonPrimitive("delivery_staff_id").getAsInt() );
	//				if (!staffs.contains(classLabel))
	//					noOfStaffsAvailable += 1;
	//				staffs.add(classLabel);
	//				addressList.add(address);
	//			}
	//		} catch(Exception e) {
	//			e.printStackTrace();
	//		}
	//	}

	private void gatherData( long deliveryCenterId ) {
		noOfStaffsAvailable = DAO.getAvailableStaffs( deliveryCenterId ).size();

		List< Shipment > shipments = DAO.getAvailableShipments( deliveryCenterId );
		for (Shipment shipment : shipments){
			Address address = new Address();
			address.setID( shipment.getID() );
			address.setDeliveryCenterID( shipment.getDeliveryCenterID() );
			address.setStreetAddress( shipment.getAddress() );
			address.setLocality( shipment.getLocality() );
			address.setCity( shipment.getCity() );
			address.setPincode( shipment.getPincode() );
			addressList.add(address);
		}
	}

	private void computeGeoLocations() {
		AddressToGeoLocation atg = new AddressToGeoLocation();
		geoLocationList = atg.processAddresses(addressList, unresolvedAddresses);
	}

	void formClusters( long deliveryCenterId ) {
		gatherData( deliveryCenterId );
		System.out.println("gathered " + addressList.size() + " shipment records");
		computeGeoLocations();
		Dataset data = new DefaultDataset();
		int i = 0;
		for (LatLong loc : geoLocationList) {
			double[] coord = new double[2];
			coord[0] = loc.getLatitude();
			coord[1] = loc.getLongitude();
			Instance instance = new DenseInstance(coord, i );
			data.add(instance);
			i += 1;
		}
		storeData(data);
		//		Dataset data = restoreData();
		System.out.println("Data set prepared with " + data.size() + " instances and " + noOfStaffsAvailable + " clusters");
		Dataset[] clusters = new Cluster().getClusters(noOfStaffsAvailable, data);
		for (int j = 0; j < clusters.length; j++){
			Dataset cluster = clusters[j];
			for (Instance instance : cluster) {
				int index = (int) instance.classValue();
				Address address = addressList.get(index);
				address.setCluster("SDA" + j);
			}
		}
		for (Address address : unresolvedAddresses)
			address.setCluster("Not assigned");
		addressList.addAll(unresolvedAddresses);

		for (Address address : addressList)
			System.out.println(address.toString());
		//		writeToFile(clusters);
		DAO.writeTripClusters(addressList);
	}

	private void storeData(Dataset data) {
		try{

			FileOutputStream fout = new FileOutputStream("data/data.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(data);
			oos.close();
			System.out.println("Data stored to file");

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private Dataset restoreData() {
		Dataset data = null;
		try{

			FileInputStream fin = new FileInputStream("data/data.ser");
			ObjectInputStream ois = new ObjectInputStream(fin);
			data = (Dataset) ois.readObject();
			ois.close();
			System.out.println("Data restored from file");

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return data;
	}

	private void writeToFile(Dataset[] clusters) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("data/testResult.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int i = 0;
		Iterator<Address> addressIterator = addressList.iterator();
		for (Dataset cluster : clusters) {
			i += 1;
			for (Instance instance : cluster) {
				Address address = addressIterator.next();
				StringBuilder sb = new StringBuilder();
				sb.append(address.getStreetAddress());
				sb.append("|");
				sb.append(address.getLocality());
				sb.append("|");
				sb.append(address.getCity());
				sb.append("|");
				sb.append(address.getPincode());
				sb.append("|");
				sb.append(String.valueOf(instance.get(0)));
				sb.append("|");
				sb.append(String.valueOf(instance.get(1)));
				sb.append("|");
				sb.append(instance.classValue());
				sb.append("|Cluster" + i);
				sb.append("\n");
				//				System.out.println(sb);
				if (pw != null)
					pw.write(sb.toString());
			}
		}
		if (pw != null)
			pw.close();
	}

	public static void main(String[] args) {
		Manager manager = new Manager();
		manager.initialize();
		manager.formClusters( 72 );
		manager.dispose();
	}

}
