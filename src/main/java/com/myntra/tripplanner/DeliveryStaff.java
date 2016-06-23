package com.myntra.tripplanner;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "delivery_staff")
public class DeliveryStaff implements Serializable{
	private static final long serialVersionUID = 1L;
	private long ID;
	private long deliveryCenterID;

	public DeliveryStaff() {
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}

	@Column(name = "delivery_center_id")
	public long getDeliveryCenterID() {
		return deliveryCenterID;
	}
	public void setDeliveryCenterID(long deliveryCenterID) {
		this.deliveryCenterID = deliveryCenterID;
	}
	@Override
	public String toString() {
		return "DeliveryStaff [ID=" + ID + ", deliveryCenterID=" + deliveryCenterID + "]";
	}

}
