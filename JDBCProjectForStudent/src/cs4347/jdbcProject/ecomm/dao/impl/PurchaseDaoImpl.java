package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchaseDaoImpl implements PurchaseDAO
{
	public Purchase create(Connection connection, Purchase purchase) throws SQLException, DAOException
	{
		if (purchase.getId() != null) {
			throw new DAOException("Trying to insert purchase with NON-NULL ID");
		}
		
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"INSERT INTO purchase (purchase_amount, purchase_date, product_id, customer_id) "
				+ "VALUES (?, ?, ?, ?);",
				Statement.RETURN_GENERATED_KEYS
			);
			statement.setDouble(1, purchase.getPurchaseAmount());
			statement.setDate(2, purchase.getPurchaseDate());
			statement.setLong(3, purchase.getProductID());
			statement.setLong(4, purchase.getCustomerID());
			
			statement.executeUpdate();
			
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			int id = result.getInt(1);
			purchase.setId((long)id);
			
			return purchase;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		}
	}
	
	public Purchase retrieve(Connection connection, Long id) throws SQLException, DAOException
	{
		if (id == null) {
			throw new DAOException("Trying to retrieve Purchase with NULL ID");
		}
		
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"SELECT purchase_amount, purchase_date, product_id, customer_id "
				+ "FROM purchase "
				+ "WHERE id = ?;"
			);
			statement.setLong(1, id);
			
			statement.executeQuery();
			ResultSet result = statement.getResultSet();
			
			if (result.next()) {
				Purchase purchase = new Purchase();
				purchase.setId(id);
				purchase.setPurchaseAmount(result.getDouble(1));
				purchase.setPurchaseDate(result.getDate(2));
				purchase.setProductID(result.getLong(3));
				purchase.setCustomerID(result.getLong(4));
				
				return purchase;
			}
			
			return null;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		}
	}
 
	
	public int update(Connection connection, Purchase purchase) throws SQLException, DAOException
	{
		if (purchase.getId() == null) {
			throw new DAOException("Trying to update Purchase with NULL ID");
		}
	
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"UPDATE purchase "
				+ "SET purchase_amount = ?, purchase_date = ?, product_id = ?, customer_id = ? "
				+ "WHERE id = ?;"
			);
			statement.setDouble(1, purchase.getPurchaseAmount());
			statement.setDate(2, purchase.getPurchaseDate());
			statement.setLong(3, purchase.getProductID());
			statement.setLong(4, purchase.getCustomerID());
			statement.setLong(5, purchase.getId());
		
			int rows = statement.executeUpdate();
		
			return rows;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		}
	}
	
	public int delete(Connection connection, Long id) throws SQLException, DAOException
	{
		if (id == null) {
			throw new DAOException("Trying to delete Purchase with NULL ID");
		}
	
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"DELETE FROM purchase "
				+ "WHERE id = ?;"
			);
			statement.setLong(1, id);
		
			int rows = statement.executeUpdate();
		
			return rows;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		}
	}
	
	public List<Purchase> retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException
	{
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"SELECT id, purchase_amount, purchase_date, product_id, customer_id "
				+ "FROM purchase "
				+ "WHERE customer_id = ?;"
			);
			statement.setLong(1, customerID);
			
			statement.executeQuery();
			ResultSet result = statement.getResultSet();
			
			ArrayList<Purchase> purchases = new ArrayList<Purchase>();
			while(result.next()) {
				Purchase purchase = new Purchase();
				purchase.setId((long)result.getInt(1));
				purchase.setPurchaseAmount(result.getDouble(2));
				purchase.setPurchaseDate(result.getDate(3));
				purchase.setProductID(result.getLong(4));
				purchase.setCustomerID(result.getLong(5));
				
				purchases.add(purchase);
			}
			
			return purchases;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		}
	}
	
	public List<Purchase> retrieveForProductID(Connection connection, Long productID) throws SQLException, DAOException
	{
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"SELECT id, purchase_amount, purchase_date, product_id, customer_id "
				+ "FROM purchase "
				+ "WHERE product_id = ?;"
			);
			statement.setLong(1, productID);
			
			statement.executeQuery();
			ResultSet result = statement.getResultSet();
			
			ArrayList<Purchase> purchases = new ArrayList<Purchase>();
			while(result.next()) {
				Purchase purchase = new Purchase();
				purchase.setId((long)result.getInt(1));
				purchase.setPurchaseAmount(result.getDouble(2));
				purchase.setPurchaseDate(result.getDate(3));
				purchase.setProductID(result.getLong(4));
				purchase.setCustomerID(result.getLong(5));
				
				purchases.add(purchase);
			}
			
			return purchases;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		}
	}
	
	public PurchaseSummary retrievePurchaseSummary(Connection connection, Long customerID) throws SQLException, DAOException
	{
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"SELECT MIN(purchase_amount), MAX(purchase_amount), AVG(purchase_amount) "
				+ "FROM purchase "
				+ "GROUP BY customer_id "
				+ "HAVING customer_id = ?"
			);
			statement.setLong(1,customerID);
			
			statement.executeQuery();
			ResultSet result = statement.getResultSet();
			
			if (result.next()) {
				PurchaseSummary psum = new PurchaseSummary();
				psum.minPurchase = result.getFloat(1);
				psum.maxPurchase = result.getFloat(2);
				psum.avgPurchase = result.getFloat(3);
				
				return psum; 
			}
			
			return null;
		} 	
		finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		}	
	}
}

	
