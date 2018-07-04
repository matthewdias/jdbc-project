package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.util.DAOException;


public class CreditCardDaoImpl implements CreditCardDAO
{
	public CreditCard create(Connection connection, CreditCard creditCard, Long customerID) throws SQLException, DAOException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"INSERT INTO credit_card (name, cc_number, exp_date, security_code, customer_id)"
				+ "VALUES (?, ?, ?, ?, ?);"
			);
			statement.setString(1, creditCard.getName());
			statement.setString(2, creditCard.getCcNumber());
			statement.setString(3, creditCard.getExpDate());
			statement.setString(4, creditCard.getSecurityCode());
			statement.setLong(5, customerID);
			
			statement.executeUpdate();
			
			return creditCard;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public CreditCard retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"SELECT name, cc_number, exp_date, security_code"
				+ "FROM credit_card"
				+ "WHERE customer_id = ?"
			);
			statement.setLong(1, customerID);
			
			statement.executeQuery();
			ResultSet result = statement.getResultSet();
			result.next();
			
			CreditCard creditCard = new CreditCard();
			creditCard.setName(result.getString(1));
			creditCard.setCcNumber(result.getString(2));
			creditCard.setExpDate(result.getString(3));
			creditCard.setSecurityCode(result.getString(4));
			
			return creditCard;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public void deleteForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"DELETE FROM creditCard"
				+ "WHERE customer_id = ?"
			);
			statement.setLong(1, customerID);
			
			statement.executeUpdate();
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
