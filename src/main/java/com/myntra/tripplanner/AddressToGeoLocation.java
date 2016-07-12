package com.myntra.tripplanner;

import java.util.ArrayList;
import java.util.Collections;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class AddressToGeoLocation {

	private static final String geoCodingAPIKey = "AIzaSyDO1bOfB_s4NVTs0FadoWqgZv5VFejYkHQ";
	private static final String geoCodingAPIKey2 = "AIzaSyASQ_n0CyqfuPCKY3HazLcDyskkhINr_II";
	private static final String geoCodingAPIKey3 = "AIzaSyDXnGc-BI4E1mmrk10ehqWrULqPgYdiTB0";
	private static final String geoCodingAPIKey4 = "AIzaSyAeyhljrpKF1rRl54_4_taHMs5PdS1tC3s";
	private static int requestCount = 0;
	private GeoApiContext context;
	private Boolean isLatLong;

	public AddressToGeoLocation() {
		// TODO Auto-generated constructor stub
		context = new GeoApiContext().setApiKey(geoCodingAPIKey4);
		isLatLong = true;
	}

	private LatLong getLatLong (String fullAddress) {
		System.out.println(fullAddress);
		if ( requestCount > 0 && (requestCount % 10) == 0 ) {
			try {
				Thread.sleep(2000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		requestCount += 1;
		LatLong result = new LatLong(0.0, 0.0);
		try {
			GeocodingResult[] results = GeocodingApi.newRequest(context).address(fullAddress).await();
			if (results.length != 0) {
				result.setLatitude(results[0].geometry.location.lat);
				result.setLongitude(results[0].geometry.location.lng);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		System.out.println(result.toString());
		System.out.println("Requested count: " + requestCount);
		return result;
	}

	private LatLong getBestLatLong(Address address) {
		String localityAddress = getLocalityAddress( address );
		LatLong localityLatLong = getLatLong( localityAddress );
		if (localityLatLong.isZero())
			return localityLatLong;
		String streetAddress = address.getStreetAddress();
		if (streetAddress.contains("\n")) {
			String[] splitAddress = streetAddress.split("\n");
			for (String add : splitAddress) {
				LatLong temp = getLatLong(add + " " + localityAddress);
				if (temp.isZero())
					continue;
				if( difference(localityLatLong, temp) )
					return temp;
			}
		}
		else {
			address = removeUnwantedCharacters(address);
			address = removeRepeatedWords(address);
			String tempStr = address.getStreetAddress();
			do{
				LatLong temp = getLatLong(tempStr + " " + localityAddress);
				if (!temp.isZero() && difference(localityLatLong, temp) )
					return temp;
				try {
					tempStr = tempStr.split(",", 2)[1];
				} catch (ArrayIndexOutOfBoundsException ex) {
					break;
				}
			} while(tempStr != null && tempStr.contains(","));

			address = removeUnwantedCharacters(address);
			address = removeRepeatedWords(address);
			String[] splitStr = address.getStreetAddress().split(",");
			String landmark = "", street = "";

			for (String str : splitStr) {
				if (str.contains("block") || str.contains("road") || str.contains("main") || str.contains("cross")) {
					str = str.replaceAll("main", "main rd");
					str = str.replaceAll("cross", "cross rd");
					street += str + ", ";
				}
				if(str.contains("near") || str.contains("landmark") || str.contains("next") || str.contains("opp")
						|| str.contains("front") || str.contains("behind") || str.contains("above")) {
					str = str.replaceAll("(near|next) (by |to )?","");
					str = str.replaceAll("(landmark|behind|above) ?", "");
					str = str.replaceAll("(in )?front (of )?", "");
					str = str.replaceAll("(opp|opp.|opposite) (to |of )?", "");
					landmark = str;
				}
			}
			if (!street.isEmpty()) {
				LatLong temp = getLatLong(street + localityAddress);
				if (!temp.isZero() && difference(localityLatLong, temp) ) {
					System.out.println("#########shout##########");
					return temp;
				}
			}
			if(!landmark.isEmpty()) {
				LatLong temp = getLatLong(landmark + ", " + localityAddress);
				if (!temp.isZero() && difference(localityLatLong, temp) ){
					System.out.println("#########shout##########");
					return temp;
				}
			}
			System.out.println("");
			System.out.println("/********************************/");
			System.out.println(streetAddress);
			System.out.println(street + localityAddress);
			System.out.println(landmark + ", " + localityAddress);
			System.out.println("/********************************/");

		}
		isLatLong = false;
		return localityLatLong;
	}

	private String getLocalityAddress(Address address) {
		String localityAddress = "";
		String locality = address.getLocality();
		if (locality != null && !locality.isEmpty())
			localityAddress += locality + ", " + address.getCity();
		else
			localityAddress += address.getCity() + " - " + address.getPincode();
		return localityAddress;
	}

	private boolean difference(LatLong x, LatLong y) {
		double diff = 0;
		diff += Math.abs(x.getLatitude() - y.getLatitude());
		diff += Math.abs(x.getLongitude() - y.getLongitude());
		if (diff > 0 && diff < 0.05)
			return true;
		else
			return false;
	}

	ArrayList< LatLong > processAddresses ( ArrayList< Address > addressList, ArrayList< Address > unresolvedAddresses ) {
		ArrayList< LatLong > latlongList = new ArrayList< LatLong >();
		ArrayList< Integer > badAddresses = new ArrayList< Integer >();
		int i = 0;
		for (Address address: addressList) {
			isLatLong = true;
			address.print();
			LatLong temp = getBestLatLong(address);
			address.setLatitude( temp.getLatitude() );
			address.setLongitude( temp.getLongitude() );
			if (!temp.isZero() && isLatLong)
				latlongList.add( temp );
			else {
				badAddresses.add(i);
				unresolvedAddresses.add(address);
			}
			i += 1;
		}
		Collections.sort(badAddresses, Collections.reverseOrder());
		for(int index: badAddresses) {
			addressList.remove(index);
		}
		return latlongList;
	}

	private Address removeUnwantedCharacters(Address address) {
		address.setStreetAddress(address.getStreetAddress().replaceAll("(\\t|\\r?\\n)+", ", "));
		address.setStreetAddress( address.getStreetAddress().replaceAll("[^A-Za-z/#.,0-9 ]+", "").toLowerCase() );
		return address;
	}

	private Address removeRepeatedWords(Address address) {
		String streetAddress = address.getStreetAddress();
		streetAddress = streetAddress.replaceAll(address.getLocality(),"");
		streetAddress = streetAddress.replaceAll(address.getCity(), "");
		streetAddress = streetAddress.replaceAll(address.getPincode(), "");
		streetAddress = streetAddress.replaceAll("[,./][ ]*[/,.]", ",");
		streetAddress = streetAddress.replaceAll("[ ]+"," ");
		streetAddress = streetAddress.replaceFirst("[^a-z]*$", "");
		address.setStreetAddress(streetAddress);
		return address;
	}

}
