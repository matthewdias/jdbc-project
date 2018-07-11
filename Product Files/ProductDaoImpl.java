package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class ProductDaoImpl implements ProductDAO
{

	public Product create(Connection connection, Product product) throws SQLException, DAOException
	{
		//check if product id is not null, throw exception if not null
		if(product.getId() != null)
		{
			throw new DAOException("Non-Null product ID detected, cannot insert; please provide Null ID.");
		}
		
		//attempt to prepare statement for insert
		PreparedStatement ps = null;
		
		try
		{
			//initial prepared statement INSERT values to be determined
			ps = connection.prepareStatement("INSERT INTO product (prodName, prodCategory, prodDescription, prodUPC) "
					+ "VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			
			//pull values from object into prepared statement for execution against dbms
			ps.setString(1,product.getProdName());
			ps.setInt(2,product.getProdCategory());
			ps.setString(3,product.getProdDescription());
			ps.setString(4,product.getProdUPC());
			
			//executes the insert query in the prepared statement
			ps.executeUpdate();
			
			
			//obtain product ID's from the table, then set the object's id variable before returning the object
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			product.setId((long)rs.getInt(1));
			return product;
		}
		finally
		{
			//check connection and current status
			if(ps != null && !ps.isClosed())
			{
				//close if open
				ps.close();
			}
		}
	}

	public Product retrieve(Connection connection, Long id) throws SQLException, DAOException
	{
		//check if product id is null, throw exception if null
		if(id == null)
		{
			throw new DAOException("Null product ID detected, cannot retrieve; please provide Non-Null ID.");
		}
		
		//attempt to prepare statement for retrieval
		PreparedStatement ps = null;
		
		try
		{
			//initialize the prepared statement
			ps = connection.prepareStatement("SELECT prodName, prodCategory, prodDescription, prodUPC FROM product WHERE id = ?");
			
			//set id value in SQL query statement
			ps.setLong(1, id);
			
			//take the result set of executed query and store
			ResultSet rs = ps.executeQuery();
			
			//test if product was found
			if(!rs.next())
			{
				return null;
			}
			
			//create new product object to store retrieved product data in
			Product prod = new Product();
			
			//assign the result set and the given id to the object
			prod.setId(id);
			prod.setProdName(rs.getString("prodName"));
			prod.setProdCategory(rs.getInt("prodCategory"));
			prod.setProdDescription(rs.getString("prodDescription"));
			prod.setProdUPC(rs.getString("prodUPC"));
			
			//return finished product object
			return prod;
		}
		finally
		{
			//check connection and current status
			if(ps != null && !ps.isClosed())
			{
				//close if open
				ps.close();
			}
		}
	}

	public int update(Connection connection, Product product) throws SQLException, DAOException
	{
		//check if product id is null, throw exception if null
		if(product.getId() == null)
		{
			throw new DAOException("Null product ID detected, cannot update; please provide Non-Null ID.");
		}
		
		//attempt to prepare statement for update
		PreparedStatement ps = null;
		
		try
		{
			//initialize the prepared statement
			ps = connection.prepareStatement("UPDATE product SET prodName = ?, prodCategory = ?, prodDescription = ?, prodUPC = ? "
					+ "WHERE id = ?");
			
			//set values in SQL query statement
			ps.setString(1, product.getProdName());
			ps.setInt(2, product.getProdCategory());
			ps.setString(3, product.getProdDescription());
			ps.setString(4, product.getProdUPC());
			ps.setLong(5, product.getId());
			
			//take the count of executed query and store
			int res = ps.executeUpdate();
			
			//return the count of rows updated
			return res;
		}
		finally
		{
			//check connection and current status
			if(ps != null && !ps.isClosed())
			{
				//close if open
				ps.close();
			}
		}
	}

	public int delete(Connection connection, Long id) throws SQLException, DAOException
	{
		//check if product id is null, throw exception if null
		if(id == null)
		{
			throw new DAOException("Null product ID detected, cannot delete; please provide Non-Null ID.");
		}
		
		//attempt to prepare statement for deletion
		PreparedStatement ps = null;
		
		try
		{
			//initialize the prepared statement
			ps = connection.prepareStatement("DELETE FROM product WHERE id = ?");
			
			//set id value in SQL query statement
			ps.setLong(1, id);
			
			//take the count of the executed update and store
			int count = ps.executeUpdate();
			
			//return count of rows affected
			return count;
		}
		finally
		{
			//check connection and current status
			if(ps != null && !ps.isClosed())
			{
				//close if open
				ps.close();
			}
		}
	}

	public List<Product> retrieveByCategory(Connection connection, int category) throws SQLException, DAOException
	{
		//attempt to prepare statement for retrieval
		PreparedStatement ps = null;
		
		try
		{
			//initialize the prepared statement
			ps = connection.prepareStatement("SELECT id, prodName, prodDescription, prodUPC FROM product WHERE prodCategory = ?");
			
			//set category value in SQL query statement
			ps.setLong(1, category);
			
			//take the result set of executed query and store
			ResultSet rs = ps.executeQuery();
			
			//create a list to store result set
			List<Product> result = new ArrayList<>();
			
			//insert all the results into the array list
			while(rs.next())
			{
				//create new product object
				Product prod = new Product();
				
				//fill in with result set values
				prod.setId(rs.getLong("id"));
				prod.setProdName(rs.getString("prodName"));
				prod.setProdDescription(rs.getString("prodDescription"));
				prod.setProdUPC(rs.getString("prodUPC"));
				prod.setProdCategory(category);
				
				//add to list
				result.add(prod);
			}
			//return product list
			return result;
		}
		finally
		{
			//check connection and current status
			if(ps != null && !ps.isClosed())
			{
				//close if open
				ps.close();
			}
		}
	}

	public Product retrieveByUPC(Connection connection, String upc) throws SQLException, DAOException
	{
		//attempt to prepare statement for retrieval
		PreparedStatement ps = null;
		
		try
		{
			//initialize the prepared statement
			ps = connection.prepareStatement("SELECT id, prodName, prodDescription, prodCategory FROM product WHERE prodUPC = ?");
			
			//set category value in SQL query statement
			ps.setString(1, upc);
			
			//take the result set of executed query and store
			ResultSet rs = ps.executeQuery();
			
			//test if product was found in table
			if(!rs.next())
			{
				//if not, return null
				return null;
			}
			
			//create new product object
			Product prod = new Product();
			
			//fill in with result set values
			prod.setId(rs.getLong("id"));
			prod.setProdName(rs.getString("prodName"));
			prod.setProdDescription(rs.getString("prodDescription"));
			prod.setProdUPC(upc);
			prod.setProdCategory(rs.getInt("prodCategory"));
			
			//return product object
			return prod;
		}
		finally
		{
			//check connection and current status
			if(ps != null && !ps.isClosed())
			{
				//close if open
				ps.close();
			}
		}
	}
}