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
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.junit.Test;

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.dao.impl.ProductDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.PurchaseDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.testing.DataSourceManager;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class ProductDaoTest
{
	@Test
	public void testCreate() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		// Do not commit any changes made by this test.
		connection.setAutoCommit(false);
		try {
			Product prod = buildProduct();
			ProductDAO dao = new ProductDaoImpl();

			Product prod2 = dao.create(connection, prod);
			assertNotNull(prod2);
			assertNotNull(prod2.getId());
			System.out.println("Generated Key: " + prod2.getId());

		}
		finally {
			// Do not commit any changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	/**
	 * Test a failed creation due to attempting to insert a Product with a
	 * non=null ID. Expects to catch DAOException.
	 */
	@Test(expected = DAOException.class)
	public void testCreateFailed() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		// Do not commit any changes made by this test.
		connection.setAutoCommit(false);
		try {
			ProductDAO dao = new ProductDaoImpl();

			Product prod = buildProduct();
			// This will cause the create to fail
			prod.setId(System.currentTimeMillis());
			dao.create(connection, prod);

		}
		finally {
			// Do not commit any changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	@Test
	public void testRetrieve() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		try {
			Product prod = buildProduct();
			ProductDAO dao = new ProductDaoImpl();

			Product prod2 = dao.create(connection, prod);
			Long id = prod2.getId();

			Product prod3 = dao.retrieve(connection, id);
			assertNotNull(prod3);
			assertEquals(prod2.getId(), prod3.getId());
			assertEquals(prod2.getProdName(), prod3.getProdName());
			assertEquals(prod2.getProdDescription(), prod3.getProdDescription());
			assertEquals(prod2.getProdCategory(), prod3.getProdCategory());
			assertEquals(prod2.getProdUPC(), prod3.getProdUPC());
		}
		finally {
			connection.close();
		}
	}

	@Test
	public void testRetrieveFailed() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		try {
			ProductDAO dao = new ProductDaoImpl();

			Long id = System.currentTimeMillis();
			Product prod = dao.retrieve(connection, id);
			assertNull(prod);
		}
		finally {
			connection.close();
		}
	}

	@Test
	public void testUpdate() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		// Do not commit any changes made by this test.
		connection.setAutoCommit(false);
		try {
			Product prod = buildProduct();
			ProductDAO dao = new ProductDaoImpl();

			Product prod2 = dao.create(connection, prod);
			Long id = prod2.getId();

			String newUPC = genUPC();
			prod2.setProdUPC(newUPC);
			int rows = dao.update(connection, prod2);
			assertEquals(1, rows);

			Product prod3 = dao.retrieve(connection, id);
			assertEquals(newUPC, prod3.getProdUPC());
		}
		finally {
			// Do not commit any changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	@Test
	public void testDelete() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		// Do not commit any changes made by this test.
		connection.setAutoCommit(false);
		try {
			Product prod = buildProduct();
			ProductDAO dao = new ProductDaoImpl();

			Product prod2 = dao.create(connection, prod);
			Long id = prod2.getId();

			int rows = dao.delete(connection, id);
			assertEquals(1, rows);
		}
		finally {
			// Do not commit any changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	/**
	 * Tests deleting a product and associated purchases. NOTE: This test
	 * assumes that the cascade delete option was set on the foreign key.
	 */
	@Test
	public void testDelete2() throws Exception
	{
		Long prodID = 57l; // Need Product ID with attached Purchases

		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		// Do not commit any changes made by this test.
		connection.setAutoCommit(false);
		try {
			ProductDAO prodDao = new ProductDaoImpl();
			PurchaseDAO purcDao = new PurchaseDaoImpl();

			Product prod1 = prodDao.retrieve(connection, prodID);
			assertNotNull(prod1);

			List<Purchase> purchases = purcDao.retrieveForProductID(connection, prodID);
			assertTrue(purchases.size() > 1);

			int rows = prodDao.delete(connection, prodID);
			assertEquals(1, rows);

			prod1 = prodDao.retrieve(connection, prodID);
			assertNull(prod1);

			// Assumes Cascade Delete
			purchases = purcDao.retrieveForProductID(connection, prodID);
			assertTrue(purchases.size() == 0);
		}
		finally {
			// Do not commit any changes made by this test.
			connection.rollback();
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	@Test
	public void testRetrieveByCategory() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection connection = ds.getConnection();
		try {
			ProductDAO prodDao = new ProductDaoImpl();

			List<Product> products = prodDao.retrieveByCategory(connection, 7); // 0-9
			assertTrue(products.size() > 0);
		}
		finally {
			connection.close();
		}
	}

	private Product buildProduct()
	{

		Product result = new Product();
		result.setProdName("ProductXYZ");
		result.setProdDescription("ProductXYZ Description");
		result.setProdCategory(3);
		result.setProdUPC(genUPC());
		return result;
	}

	private String genUPC()
	{
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int idx = 0; idx < 12; idx++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

}