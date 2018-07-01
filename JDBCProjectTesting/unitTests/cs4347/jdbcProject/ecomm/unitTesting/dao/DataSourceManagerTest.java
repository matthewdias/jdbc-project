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
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;

import cs4347.jdbcProject.ecomm.testing.DataSourceManager;

public class DataSourceManagerTest
{

	@Test
	public void testGetPropertiesFromClasspath() throws Exception
	{
		Properties props = DataSourceManager.getPropertiesFromClasspath();
		assertNotNull(props);
		assertNotNull(props.getProperty("url"));
		assertNotNull(props.getProperty("id"));
		assertNotNull(props.getProperty("passwd"));
	}


	@Test
	public void testGetDataSource() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		assertNotNull(ds);
	}
	
	/**
	 * NOTE: This test will execute only if the DBMS is running 
	 * at the url in the configuration file. 
	 * @throws Exception
	 */
	@Test
	public void testGetConnection() throws Exception
	{
		DataSource ds = DataSourceManager.getDataSource();
		Connection conn = ds.getConnection();
		assertNotNull(conn);
	}
}
