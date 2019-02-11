package com;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Test {

	private static SessionFactory factory;
	private static ServiceRegistry serviceRegistry;

	public static void main(String[] args) {

		Configuration config = new Configuration();
		config.configure();
		config.addAnnotatedClass(User.class);
		config.addResource("User.hbm.xml");
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		factory = config.buildSessionFactory(serviceRegistry);

		User usuario1 = new User(4, "", "", "", "");
		User usuario2 = new User(1, "Otro", "Otro", "otro@gmail.com", "otroo");
		Test ejemplo = new Test();

		// ejemplo.insertUser(usuario2);
		// ejemplo.updateUser(usuario2);
		// ejemplo.deleteUser(usuario2);

		List<User> users = ejemplo.listUsers();
		System.out.println("Total usuarios: " + users.size());
		for (User u : users) {
			System.out.println(u.getId() + " " + u.getFirstname() + " " + u.getLastname() + " " + u.getEmail() + " "
					+ u.getUsername());
		}

		List<String> nombres = ejemplo.listUsersNombre();
		System.out.println("Total usernames: " + nombres.size());
		for (String u : nombres) {
			System.out.println(u);
		}

		User x = ejemplo.userPorId(1);
		System.out.println(x);

		ejemplo.updateUsernameUserPorId(1, "UserCuela");

	}

	private void insertUser(User u) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(u);
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}

	private void updateUser(User u) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(u);
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}

	private void deleteUser(User u) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(u);
			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}

	private List<User> listUsers() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<User> users = new ArrayList();
		try {
			tx = session.beginTransaction();
			users = session.createQuery("From User").list();
			tx.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return users;
	}

	private List<String> listUsersNombre() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<String> nombres = new ArrayList();
		try {
			tx = session.beginTransaction();
			String sql = "SELECT username FROM User";
			Query query = session.createQuery(sql);
			nombres = query.list();
			tx.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return nombres;
	}

	private User userPorId(int num) {
		Session session = factory.openSession();
		Transaction tx = null;
		User usuario = new User();
		try {
			tx = session.beginTransaction();
			String sql = "FROM User WHERE id=:num";
			Query query = session.createQuery(sql);
			query.setParameter("num", num);
			usuario = (User) query.list().get(0);
			tx.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return usuario;
	}

	private void updateUsernameUserPorId(int num, String username) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String sql = "UPDATE User SET username=:username WHERE id=:num";
			Query query = session.createQuery(sql);
			query.setParameter("num", num);
			query.setParameter("username", username);
			query.executeUpdate();
			System.out.println("Usuario " + num + " nuevo username = " + username);
			tx.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
}
