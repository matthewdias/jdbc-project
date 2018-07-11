package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.dao.impl.ProductDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.services.ProductPersistenceService;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	private DataSource dataSource;

	public ProductPersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public Product create(Product product) throws SQLException, DAOException
	{
		//check if product id is not null, throw exception if not null
		if(product.getId() != null)
		{
			throw new DAOException("Non-Null product ID detected, cannot insert; please provide Null ID.");
		}
		
		//create ProductDAO object
		ProductDAO productDAO = new ProductDaoImpl();
		
		//establish connection
		Connection connection = dataSource.getConnection();
		
		//attempt to call the product DAO method and pass the connection and object
		try
		{
			return productDAO.create(connection, product);
		}
		finally
		{
			//check connection and current status
			if(connection != null && !connection.isClosed())
			{
				//close if open
				connection.close();
			}
		}
	}

	public Product retrieve(Long id) throws SQLException, DAOException
	{
		//check if product id is null, throw exception if null
		if(id == null)
		{
			throw new DAOException("Null product ID detected, cannot retrieve; please provide Non-Null ID.");
		}
		
		//create ProductDAO object
		ProductDAO productDAO = new ProductDaoImpl();
		
		//establish connection
		Connection connection = dataSource.getConnection();
		
		//attempt to call the product DAO method and pass the connection and object
		try
		{
			return productDAO.retrieve(connection, id);
		}
		finally
		{
			//check connection and current status
			if(connection != null && !connection.isClosed())
			{
				//close if open
				connection.close();
			}
		}
	}

	public int update(Product product) throws SQLException, DAOException
	{
		//check if product id is null, throw exception if null
		if(product.getId() == null)
		{
			throw new DAOException("Null product ID detected, cannot update; please provide Non-Null ID.");
		}
		
		//create ProductDAO object
		ProductDAO productDAO = new ProductDaoImpl();
		
		//establish connection
		Connection connection = dataSource.getConnection();
		
		//attempt to call the product DAO method and pass the connection and object
		try
		{
			return productDAO.update(connection, product);
		}
		finally
		{
			//check connection and current status
			if(connection != null && !connection.isClosed())
			{
				//close if open
				connection.close();
			}
		}
	}

	public int delete(Long id) throws SQLException, DAOException
	{
		//check if product id is null, throw exception if null
		if(id == null)
		{
			throw new DAOException("Null product ID detected, cannot delete; please provide Non-Null ID.");
		}
		
		//create ProductDAO object
		ProductDAO productDAO = new ProductDaoImpl();
		
		//establish connection
		Connection connection = dataSource.getConnection();
		
		//attempt to call the product DAO method and pass the connection and object
		try
		{
			return productDAO.delete(connection, id);
		}
		finally
		{
			//check connection and current status
			if(connection != null && !connection.isClosed())
			{
				//close if open
				connection.close();
			}
		}
	}

	public Product retrieveByUPC(String upc) throws SQLException, DAOException
	{
		//check if product upc is null, throw exception if null
		if(upc == null)
		{
			throw new DAOException("Null product upc detected, cannot retrieve; please provide Non-Null upc.");
		}
		
		//create ProductDAO object
		ProductDAO productDAO = new ProductDaoImpl();
		
		//establish connection
		Connection connection = dataSource.getConnection();
		
		//attempt to call the product DAO method and pass the connection and object
		try
		{
			return productDAO.retrieveByUPC(connection, upc);
		}
		finally
		{
			//check connection and current status
			if(connection != null && !connection.isClosed())
			{
				//close if open
				connection.close();
			}
		}
	}

	public List<Product> retrieveByCategory(int category) throws SQLException, DAOException
	{
		//create ProductDAO object
		ProductDAO productDAO = new ProductDaoImpl();
		
		//establish connection
		Connection connection = dataSource.getConnection();
		
		//attempt to call the product DAO method and pass the connection and object
		try
		{
			return productDAO.retrieveByCategory(connection, category);
		}
		finally
		{
			//check connection and current status
			if(connection != null && !connection.isClosed())
			{
				//close if open
				connection.close();
			}
		}
	}
}