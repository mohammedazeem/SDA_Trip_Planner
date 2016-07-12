package com.myntra.tripplanner;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "ml_shipment" )
public class Shipment implements Serializable{

	private static final long serialVersionUID = 1L;
	private long ID;
	private long deliveryCenterID;
	private String shipmentType;
	private String shipmentStatus;
	private String address;
	private String locality;
	private String city;
	private String pincode;

	public Shipment(){
	}

	@Id
	@GeneratedValue
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

	@Column( name = "shipment_type" )
	public String getShipmentType() {
		return shipmentType;
	}
	public void setShipmentType(String shipmentType) {
		this.shipmentType = shipmentType;
	}

	@Column( name = "shipment_status" )
	public String getShipmentStatus() {
		return shipmentStatus;
	}
	public void setShipmentStatus(String shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}

	@Column( name = "address" )
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@Column( name = "locality" )
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Column( name = "city" )
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	@Column( name = "pincode" )
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Shipment [ID=");
		builder.append(ID);
		builder.append(", deliveryCenterID=");
		builder.append(deliveryCenterID);
		builder.append(", shipmentType=");
		builder.append(shipmentType);
		builder.append(", shipmentStatus=");
		builder.append(shipmentStatus);
		builder.append(", address=");
		builder.append(address);
		builder.append(", locality=");
		builder.append(locality);
		builder.append(", city=");
		builder.append(city);
		builder.append(", pincode=");
		builder.append(pincode);
		builder.append("]");
		return builder.toString();
	}

	public void print() {
		System.out.println("** Shipment **");
		System.out.println("Street Address: " + address);
		System.out.println("Locality: " + locality);
		System.out.println("City: " + city);
		System.out.println("Pincode: " + pincode);
	}

}
