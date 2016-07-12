package com.myntra.tripplanner;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.unbescape.html.HtmlEscape;

@Entity
@Table ( name = "trip_cluster" )
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;
	private long ID;
	private long deliveryCenterID;
	private String cluster;
	private String streetAddress;
	private String locality;
	private String city;
	private String pincode;
	private double latitude;
	private double longitude;

	Address(){}

	Address(String streetAddress, String locality, String city, String pincode) {
		this.streetAddress = streetAddress;
		setLocality( locality );
		setCity( city );
		setPincode( pincode);
	}

	@Id
	@Column( name = "id" )
	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	@Column( name = "delivery_center_id" )
	public long getDeliveryCenterID() {
		return deliveryCenterID;
	}

	public void setDeliveryCenterID(long deliveryCenterID) {
		this.deliveryCenterID = deliveryCenterID;
	}

	@Column( name = "cluster" )
	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	@Column( name = "address" )
	String getStreetAddress() {
		return streetAddress;
	}

	void setStreetAddress(String streetAddress) {
		this.streetAddress = HtmlEscape.unescapeHtml( streetAddress ).toLowerCase();
	}

	@Column( name = "locality" )
	String getLocality() {
		return locality;
	}

	void setLocality(String locality) {
		locality = HtmlEscape.unescapeHtml( locality );
		locality = locality.replaceAll("(\\t|\\r?\\n)+", ", ").toLowerCase();
		locality = locality.replaceAll("[^a-z, ]+","");
		this.locality = locality;
	}

	@Column( name = "city" )
	String getCity() {
		return city;
	}

	void setCity(String city) {
		city = HtmlEscape.unescapeHtml( city );
		city = city.replaceAll("(\\t|\\r?\\n)+", ", ").toLowerCase();
		city = city.replaceAll("[^a-z, ]+","");
		this.city = city;
	}

	@Column( name = "pincode" )
	String getPincode() {
		return pincode;
	}

	void setPincode(String pincode) {
		pincode = HtmlEscape.unescapeHtml( pincode );
		pincode = pincode.replaceAll("(\\t|\\r?\\n)+", "");
		pincode = pincode.replaceAll("[^0-9]+","");
		this.pincode = pincode;
	}

	@Column( name = "latitude" )
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Column( name = "longitude" )
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Address [ID=");
		builder.append(ID);
		builder.append(", deliveryCenterID=");
		builder.append(deliveryCenterID);
		builder.append(", cluster=");
		builder.append(cluster);
		builder.append(", streetAddress=");
		builder.append(streetAddress);
		builder.append(", locality=");
		builder.append(locality);
		builder.append(", city=");
		builder.append(city);
		builder.append(", pincode=");
		builder.append(pincode);
		builder.append(", latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append("]");
		return builder.toString();
	}

	public void print() {
		System.out.println("** Address **");
		System.out.println("Street Address: " + streetAddress);
		System.out.println("Locality: " + locality);
		System.out.println("City: " + city);
		System.out.println("Pincode: " + pincode);
	}
}
