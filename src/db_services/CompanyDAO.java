package db_services;

import java.sql.SQLException;

import java.util.ArrayList;

import dataObjects.Company;
import dataObjects.Coupon;
import exceptions.LoginFailedException;

/**
 * 	This methods will be implemented in CompanyDbDao  
 */
public interface CompanyDAO {


	public void createCompany(Company company) throws SQLException, Exception;
	/**
	 * 
	 * @param company
	 * @throws SQLException
	 * @throws Exception
	 */
	public void removeCompany(Company company) throws SQLException, Exception;
	/**
	 * 
	 * @param company
	 * @throws SQLException
	 * @throws Exception
	 */
	public void updateCompany(Company company) throws SQLException, Exception;
	/**
	 * 
	 * @param id
	 * @return Company
	 * @throws SQLException
	 * @throws Exception
	 */
	public Company getCompany(long id) throws SQLException, Exception;
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Company> getAllCompanies() throws SQLException;
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Coupon> getCoupons() throws SQLException;
	/**
	 * 
	 * @param compName
	 * @param password
	 * @return boolean
	 * @throws SQLException 
	 * @throws Exception
	 */
	public boolean login(String compName, String password) throws SQLException, Exception;
}
