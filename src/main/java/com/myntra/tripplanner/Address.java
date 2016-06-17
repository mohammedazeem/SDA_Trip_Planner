package com.myntra.tripplanner;

import org.unbescape.html.HtmlEscape;

public class Address {
	private String street_address;
	private String locality;
	private String city;
	private String pincode;
	private String state;
	private String country;

	Address(){}

	Address(String street_address, String locality, String city, String pincode, String state,String country) {
		this.street_address = street_address;
		setLocality( locality );
		setCity( city );
		setPincode( pincode);
		setState( state );
		setCountry( country );
	}

	String getStreetAddress() {
		return street_address;
	}

	void setStreetAddress(String streetAddress) {
		street_address = HtmlEscape.unescapeHtml( streetAddress ).toLowerCase();
	}

	String getLocality() {
		return locality;
	}

	void setLocality(String locality) {
		locality = HtmlEscape.unescapeHtml( locality );
		locality = locality.replaceAll("(\\t|\\r?\\n)+", ", ").toLowerCase();
		locality = locality.replaceAll("[^a-z, ]+","");
		this.locality = locality;
	}

	String getCity() {
		return city;
	}

	void setCity(String city) {
		city = HtmlEscape.unescapeHtml( city );
		city = city.replaceAll("(\\t|\\r?\\n)+", ", ").toLowerCase();
		city = city.replaceAll("[^a-z, ]+","");
		this.city = city;
	}

	String getPincode() {
		return pincode;
	}

	void setPincode(String pincode) {
		pincode = HtmlEscape.unescapeHtml( pincode );
		pincode = pincode.replaceAll("(\\t|\\r?\\n)+", "");
		pincode = pincode.replaceAll("[^0-9]+","");
		this.pincode = pincode;
	}

	String getState() {
		return state;
	}

	void setState(String state) {
		this.state = state;
	}

	String getCountry() {
		return country;
	}

	void setCountry(String country) {
		this.country = country;
	}

	void print(){
		System.out.println("Street Address: " + street_address);
		System.out.println("Locality: " + locality);
		System.out.println("City: " + city);
		System.out.println("Pincode: " + pincode);
		System.out.println("State: " + state);
		System.out.println("Country: " + country);
	}
}
