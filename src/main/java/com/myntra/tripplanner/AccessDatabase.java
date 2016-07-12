package com.myntra.tripplanner;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class AccessDatabase {
	private SessionFactory sf;

	AccessDatabase() {
		try{
			sf = new Configuration().
					setProperty("hibernate.jdbc.batch_size", "20").
					configure().buildSessionFactory();
		}catch (Throwable ex) {
			printFullTrace(ex);
			throw new ExceptionInInitializerError(ex);
		}	}

	@SuppressWarnings("unchecked")
	List<DeliveryStaff> getAvailableStaffs (long deliveryCenterId) {
		Session session = sf.openSession();
		Transaction tx = null;
		List<DeliveryStaff> staffs = null;
		try{
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM DeliveryStaff where deliveryCenterID =:dcid");
			query.setParameter( "dcid", deliveryCenterId );
			staffs = query.list();
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		}finally {
			session.close();
		}
		return staffs;
	}

	@SuppressWarnings("unchecked")
	List<Shipment> getAvailableShipments( long deliveryCenterId ) {
		Session session = sf.openSession();
		Transaction tx = null;
		List<Shipment> shipments = null;
		try{
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM Shipment where deliveryCenterID = :dcid and shipmentType = :type and shipmentStatus = :status");
			query.setParameter("dcid", deliveryCenterId);
			query.setParameter("type", "DL");
			query.setParameter("status", "UNASSIGNED");
			shipments = query.list();
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		}finally {
			session.close();
		}
		return shipments;
	}

	void writeTripClusters ( ArrayList < Address> addressList) {
		Session session = sf.openSession();
		Transaction tx = null;
		long id = 0;
		try{
			tx = session.beginTransaction();
			for (int i = 0; i < addressList.size(); i++) {
				Address address = addressList.get(i);
				id = address.getID();
				session.save(address);
				if ( i % 20 == 0 ) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
			System.out.println("problem with ID : " + id);
		}finally {
			session.close();
		}

	}

	void closeSessionFactory() {
		if (sf != null)
			sf.close();
	}

	static void printFullTrace(Throwable throwable){
		for(StackTraceElement element: throwable.getStackTrace())
			System.out.println(element);
	}

}
