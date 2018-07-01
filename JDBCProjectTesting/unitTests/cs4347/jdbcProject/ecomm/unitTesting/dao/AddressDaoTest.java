/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of every team member to the Provost Office for academic 
 * dishonesty. 
 */ 

package cs4347.jdbcProject.ecomm.unitTesting.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.dao.impl.AddressDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.testing.DataSourceManager;

public class AddressDaoTest
{
	// NOTE: This must be a valid CustomerID
	Long customerID = 2L;

	@Test
	public void testCreate() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		try {
			// Do not commit changes made by this test.
			connection.setAutoCommit(false);

			Address addr = buildAddress();
			AddressDAO dao = new AddressDaoImpl();

			Address addr2 = dao.create(connection, addr, customerID);
			assertNotNull(addr2);
		}
		finally {
			// Do not commit changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	@Test
	public void testRetrieveForCustomerID() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		// Do not commit changes made by this test.
		connection.setAutoCommit(false);
		try {
			AddressDAO dao = new AddressDaoImpl();

			Address addr = dao.retrieveForCustomerID(connection, customerID);
			assertNotNull(addr);
			assertNotNull(addr.getAddress1());
			assertNotNull(addr.getCity());
			assertNotNull(addr.getState());
			assertNotNull(addr.getZipcode());

		}
		finally {
			// Do not commit changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	@Test
	public void testDeleteForCustomerID() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		try {
			// Do not commit changes made by this test.
			connection.setAutoCommit(false);

			AddressDAO dao = new AddressDaoImpl();

			Address addr = dao.retrieveForCustomerID(connection, customerID);
			assertNotNull(addr);
			dao.deleteForCustomerID(connection, customerID);
			Address addr2 = dao.retrieveForCustomerID(connection, customerID);
			assertNull(addr2);
		}
		finally {
			// Do not commit changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	/**
	 * This test is expected to cause an SQLException when the added Address
	 * contains an invalid CustomerID.
	 */
	@Test(expected = SQLException.class)
	public void testForeignKey() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		try {
			// Do not commit changes made by this test.
			connection.setAutoCommit(false);

			Address addr = buildAddress();
			AddressDAO dao = new AddressDaoImpl();

			// This customer ID cannot be in the DBMS
			Address addr2 = dao.create(connection, addr, 9999l);
			assertNotNull(addr2);
		}
		finally {
			// Do not commit changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	private Address buildAddress()
	{
		Address addr = new Address();
		addr.setAddress1("address one");
		addr.setAddress2("address two");
		addr.setCity("city");
		addr.setState("IL");
		addr.setZipcode("32345");
		return addr;
	}
}
