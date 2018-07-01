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
import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.dao.impl.AddressDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CreditCardDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.testing.DataSourceManager;

public class CreditcardDaoTest
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

			CreditCard ccard = buildCreditcard();
			CreditCardDAO dao = new CreditCardDaoImpl();

			CreditCard ccard2 = dao.create(connection, ccard, customerID);
			assertNotNull(ccard2);
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
			CreditCardDAO dao = new CreditCardDaoImpl();

			CreditCard ccard = dao.retrieveForCustomerID(connection, customerID);
			assertNotNull(ccard);
			assertNotNull(ccard.getCcNumber());
			assertNotNull(ccard.getExpDate());
			assertNotNull(ccard.getName());
			assertNotNull(ccard.getSecurityCode());

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
		// Do not commit changes made by this test.
		connection.setAutoCommit(false);
		try {
			CreditCardDAO dao = new CreditCardDaoImpl();

			CreditCard ccard = dao.retrieveForCustomerID(connection, customerID);
			assertNotNull(ccard);
			dao.deleteForCustomerID(connection, customerID);
			CreditCard ccard2 = dao.retrieveForCustomerID(connection, customerID);
			assertNull(ccard2);

		}
		finally {
			// Do not commit changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	/**
	 * This test is expected to cause an SQLException when the added CreditCard
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

			CreditCard ccard = buildCreditcard();
			CreditCardDAO dao = new CreditCardDaoImpl();

			// This customer ID cannot be in the DBMS
			CreditCard ccard2 = dao.create(connection, ccard, 9999l);
			assertNotNull(ccard2);
		}
		finally {
			// Do not commit changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	private CreditCard buildCreditcard()
	{
		CreditCard ccard = new CreditCard();
		ccard.setCcNumber("222233334444");
		ccard.setExpDate("2/13");
		ccard.setName("Josh Josh");
		ccard.setSecurityCode("222");
		return ccard;
	}
}
