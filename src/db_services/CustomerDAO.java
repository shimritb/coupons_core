package db_services;

import java.sql.SQLException;
import java.util.ArrayList;

import dataObjects.Coupon;
import dataObjects.Customer;
import exceptions.LoginFailedException;

public interface CustomerDAO {
	
	public void createCustomer(Customer customer) throws SQLException, Exception;
	
	public void removeCustomer(Customer customer) throws SQLException, Exception;
	
	public void updateCustomer(Customer customer) throws SQLException, Exception;
	
	public Customer getCustomer(long id) throws SQLException, Exception;
	
	public ArrayList<Customer> getAllCustomer() throws SQLException;
	
	public ArrayList<Coupon> getAllPurchasedCoupons() throws SQLException;
	
	public void purchaseCoupon(Coupon coupon) throws SQLException, Exception ;
		
	public boolean login(String custName,String password) throws SQLException, LoginFailedException;
}
