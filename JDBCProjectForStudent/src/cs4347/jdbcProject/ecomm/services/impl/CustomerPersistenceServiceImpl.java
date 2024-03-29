package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.dao.impl.AddressDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CreditCardDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CustomerDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.services.CustomerPersistenceService;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerPersistenceServiceImpl implements CustomerPersistenceService
{
	private DataSource dataSource;

	public CustomerPersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	/**
	 * This method provided as an example of transaction support across multiple inserts.
	 * 
	 * Persists a new Customer instance by inserting new Customer, Address, 
	 * and CreditCard instances. Notice the transactional nature of this 
	 * method which inludes turning off autocommit at the start of the 
	 * process, and rolling back the transaction if an exception 
	 * is caught. 
	 */
	@Override
	public Customer create(Customer customer) throws SQLException, DAOException
	{
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			Customer cust = customerDAO.create(connection, customer);
			Long custID = cust.getId();

			if (cust.getAddress() == null) {
				throw new DAOException("Customers must include an Address instance.");
			}
			Address address = cust.getAddress();
			addressDAO.create(connection, address, custID);

			if (cust.getCreditCard() == null) {
				throw new DAOException("Customers must include an CreditCard instance.");
			}
			CreditCard creditCard = cust.getCreditCard();
			creditCardDAO.create(connection, creditCard, custID);

			connection.commit();
			return cust;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public Customer retrieve(Long id) throws SQLException, DAOException
	{
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			Customer cust = customerDAO.retrieve(connection, id);

			Address address = addressDAO.retrieveForCustomerID(connection, id);
			cust.setAddress(address);

			CreditCard creditCard = creditCardDAO.retrieveForCustomerID(connection, id);
			cust.setCreditCard(creditCard);

			connection.commit();
			return cust;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public int update(Customer customer) throws SQLException, DAOException
	{
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);	
			int rows = customerDAO.update(connection, customer);

			if (customer.getAddress() != null) {
				addressDAO.deleteForCustomerID(connection, customer.getId());
				addressDAO.create(connection, customer.getAddress(), customer.getId());
			}
			
			if (customer.getCreditCard() != null) {
				creditCardDAO.deleteForCustomerID(connection, customer.getId());
				creditCardDAO.create(connection, customer.getCreditCard(), customer.getId());
			}

			connection.commit();
			return rows;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public int delete(Long id) throws SQLException, DAOException
	{
		CustomerDAO customerDAO = new CustomerDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			return customerDAO.delete(connection, id);
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException {
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();
		
		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			
			List<Customer> customers = customerDAO.retrieveByZipCode(connection, zipCode);
			for (Customer customer : customers) {
				Address address = addressDAO.retrieveForCustomerID(connection, customer.getId());
				customer.setAddress(address);
				
				CreditCard creditCard = creditCardDAO.retrieveForCustomerID(connection, customer.getId());
				customer.setCreditCard(creditCard);
			}
			
			return customers;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
	
	public List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException {
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();
		
		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			
			List<Customer> customers = customerDAO.retrieveByDOB(connection, startDate, endDate);
			for (Customer customer : customers) {
				Address address = addressDAO.retrieveForCustomerID(connection, customer.getId());
				customer.setAddress(address);
				
				CreditCard creditCard = creditCardDAO.retrieveForCustomerID(connection, customer.getId());
				customer.setCreditCard(creditCard);
			}
			
			return customers;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}
}
