package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.dao.impl.PurchaseDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchasePersistenceService;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	private DataSource dataSource;

	public PurchasePersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public Purchase create(Purchase purchase) throws SQLException, DAOException
	{
		if (purchase.getId() != null) {
			throw new DAOException("Trying to insert Purchase with NON-NULL ID");
		}
		
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			return purchaseDAO.create(connection, purchase);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public Purchase retrieve(Long id) throws SQLException, DAOException
	{
		if (id == null ) {
			throw new DAOException("Trying to retrieve Purchase with NULL ID");
		}
		
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			return purchaseDAO.retrieve(connection, id);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public int update(Purchase purchase) throws SQLException, DAOException
	{
		if (purchase.getId() == null ) {
			throw new DAOException("Trying to update Purchase with NULL ID");
		}
		
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		
		Connection connection = dataSource.getConnection();
		try {
			return purchaseDAO.update(connection, purchase);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public int delete(Long id) throws SQLException, DAOException
	{
		if (id == null ) {
			throw new DAOException("Trying to delete Purchase with NULL ID");
		}
		
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			return purchaseDAO.delete(connection, id);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
	{
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		
		Connection connection = dataSource.getConnection();
		try {
			return purchaseDAO.retrieveForCustomerID(connection, customerID);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
	{
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		
		Connection connection = dataSource.getConnection();
		try {
			return purchaseDAO.retrievePurchaseSummary(connection, customerID);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
	{
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		
		Connection connection = dataSource.getConnection();
		try {
			return purchaseDAO.retrieveForProductID(connection, productID);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
}
