package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerDaoImpl implements CustomerDAO
{
	public Customer create(Connection connection, Customer customer) throws SQLException, DAOException {
		if (customer.getId() != null) {
			throw new DAOException("Trying to insert Customer with NON-NULL ID");
		}
		
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"INSERT INTO customer (first_name, last_name, gender, dob, email) "
				+ "VALUES (?, ?, ?, ?, ?);",
				Statement.RETURN_GENERATED_KEYS
			);
			statement.setString(1, customer.getFirstName());
			statement.setString(2, customer.getLastName());
			statement.setString(3, String.valueOf(customer.getGender()));
			statement.setDate(4, customer.getDob());
			statement.setString(5, customer.getEmail());
			
			statement.executeUpdate();
			
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			int id = result.getInt(1);
			customer.setId((long)id);
			
			return customer;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public Customer retrieve(Connection connection, Long id) throws SQLException, DAOException {
		if (id == null) {
			throw new DAOException("Trying to retrieve Customer with NULL ID");
		}
		
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"SELECT first_name, last_name, gender, dob, email "
				+ "FROM customer "
				+ "WHERE id = ?;"
			);
			statement.setLong(1, id);
			
			statement.executeQuery();
			ResultSet result = statement.getResultSet();
			result.next();
			
			Customer customer = new Customer();
			customer.setId(id);
			customer.setFirstName(result.getString(1));
			customer.setLastName(result.getString(2));
			customer.setGender(result.getString(3).charAt(0));
			customer.setDob(result.getDate(4));
			customer.setEmail(result.getString(5));
			
			return customer;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public int update(Connection connection, Customer customer) throws SQLException, DAOException {
		if (customer.getId() == null) {
			throw new DAOException("Trying to update Customer with NULL ID");
		}
		
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"UPDATE customer "
				+ "SET first_name = ?, last_name = ?, gender = ?, dob = ?, email = ? "
				+ "WHERE id = ?;"
			);
			statement.setString(1, customer.getFirstName());
			statement.setString(2, customer.getLastName());
			statement.setString(3, String.valueOf(customer.getGender()));
			statement.setDate(4, customer.getDob());
			statement.setString(5, customer.getEmail());
			statement.setLong(6, customer.getId());
			
			int rows = statement.executeUpdate();
			
			return rows;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public int delete(Connection connection, Long id) throws SQLException, DAOException {
		if (id == null) {
			throw new DAOException("Trying to update Customer with NULL ID");
		}
		
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"DELETE FROM customer "
				+ "WHERE id = ?;"
			);
			statement.setLong(1, id);
			
			int rows = statement.executeUpdate();
			
			return rows;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public List<Customer> retrieveByZipCode(Connection connection, String zipCode) throws SQLException, DAOException {		
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"SELECT customer.id, customer.first_name, customer.last_name, customer.gender, customer.dob, customer.email "
				+ "FROM customer, address "
				+ "WHERE customer.id = address.customer_id "
				+ "AND address.zipcode = ?;"
			);
			statement.setString(1, zipCode);
			
			statement.executeQuery();
			ResultSet result = statement.getResultSet();
			
			ArrayList<Customer> customers = new ArrayList<Customer>();
			while(result.next()) {
				Customer customer = new Customer();
				customer.setId((long)result.getInt(1));
				customer.setFirstName(result.getString(2));
				customer.setLastName(result.getString(3));
				customer.setGender(result.getString(4).charAt(0));
				customer.setDob(result.getDate(5));
				customer.setEmail(result.getString(6));
				customers.add(customer);
			}
			
			return customers;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public List<Customer> retrieveByDOB(Connection connection, Date startDate, Date endDate) throws SQLException, DAOException {		
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"SELECT id, first_name, last_name, gender, dob, email "
				+ "FROM customer "
				+ "WHERE dob BETWEEN ? AND ?;"
			);
			statement.setDate(1, startDate);
			statement.setDate(2, endDate);
			
			statement.executeQuery();
			ResultSet result = statement.getResultSet();
			
			ArrayList<Customer> customers = new ArrayList<Customer>();
			while(result.next()) {
				Customer customer = new Customer();
				customer.setId((long)result.getInt(1));
				customer.setFirstName(result.getString(2));
				customer.setLastName(result.getString(3));
				customer.setGender(result.getString(4).charAt(0));
				customer.setDob(result.getDate(5));
				customer.setEmail(result.getString(6));
				customers.add(customer);
			}
			
			return customers;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
}
