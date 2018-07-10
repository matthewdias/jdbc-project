package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class AddressDaoImpl implements AddressDAO
{
	public Address create(Connection connection, Address address, Long customerID) throws SQLException, DAOException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"INSERT INTO address (address1, address2, city, state, zipcode, customer_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?);"
			);
			statement.setString(1, address.getAddress1());
			statement.setString(2, address.getAddress2());
			statement.setString(3, address.getCity());
			statement.setString(4, address.getState());
			statement.setString(5, address.getZipcode());
			statement.setLong(6, customerID);
			
			statement.executeUpdate();
			
			return address;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
//			if (connection != null && !connection.isClosed()) {
//				connection.close();
//			}
		}
	}
	
	public Address retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"SELECT address1, address2, city, state, zipcode "
				+ "FROM address "
				+ "WHERE customer_id = ?;"
			);
			statement.setLong(1, customerID);
			
			statement.executeQuery();
			ResultSet result = statement.getResultSet();
			
			if (result.next()) {
				Address address = new Address();
				address.setAddress1(result.getString(1));
				address.setAddress2(result.getString(2));
				address.setCity(result.getString(3));
				address.setState(result.getString(4));
				address.setZipcode(result.getString(5));
			
				return address;
			}
			
			return null;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
//			if (connection != null && !connection.isClosed()) {
//				connection.close();
//			}
		}
	}
	
	public void deleteForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"DELETE FROM address "
				+ "WHERE customer_id = ?;"
			);
			statement.setLong(1, customerID);
			
			statement.executeUpdate();
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
//			if (connection != null && !connection.isClosed()) {
//				connection.close();
//			}
		}
	}
}
