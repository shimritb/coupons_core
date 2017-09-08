package client;

import java.util.ArrayList;

import dataObjects.Company;
import dataObjects.Coupon;
import dataObjects.Customer;
import db_services.CouponClientFacade;
import db_services.db_dao.CompanyDBDAO;
import db_services.db_dao.CouponDBDAO;
import db_services.db_dao.CustomerDBDAO;
import exceptions.CompanyIdNotFoundException;
import exceptions.CompanyNameAlreadyExistsException;
import exceptions.CompanyNameDoesNotExistsException;
import exceptions.CustomSqlSyntaxException;
import exceptions.CustomerIdNotFoundException;
import exceptions.CustomerNameAlreadyExsitsException;
import exceptions.CustomerNameNotFound;

public class AdminFacade  implements CouponClientFacade {
	private CompanyDBDAO compDbDao = null;
	private CustomerDBDAO custDbDao = null;
	private CouponDBDAO couponDbDao = null;
	
	public AdminFacade(){
		compDbDao = new CompanyDBDAO();
		custDbDao = new CustomerDBDAO();
		couponDbDao = new CouponDBDAO();
	}
	
	/**
	 * 	This method creates new company
	 * @param company is the parameter to createCompany method
	 */
	public void createCompany(Company company){ 	
		try {
			compDbDao.createCompany(company);
		} catch (CompanyNameAlreadyExistsException e) {
			System.out.println(e.getMessage());
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} 
	}
	
	/**
	 * 	This method removes an existing company
	 * @param company is the parameter to createCompany method
	 * 
	 */
	public void removeCompany(Company company){	
		try {
			compDbDao.removeCompany(company);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CompanyNameDoesNotExistsException e) {
			System.out.println(e.getMessage());
		}
	}	
	
	/**
	 * 	This method updates the data of an existing company Except for company name
	 * @param company is the parameter to createCompany method
	 */  
	public void updateCompany(Company company){
		try {
			compDbDao.updateCompany(company);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CompanyNameDoesNotExistsException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 	This method display the data of a specific company
	 * @param id is the parameter to getCompany method
	 * @return Company or null
	 */  
	public Company getCompany(long id){
		Company company = null;
		
		try {
			company = compDbDao.getCompany(id);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CompanyIdNotFoundException e) {
			System.out.println(e.getMessage());
		}	
		return company; 
	}

	
	/**
	 * 	This method display the data of all companies exists in database
	 * @param company is the parameter to createCompany method
	 * @return an ArrayList of Companies or null
	 */  
	public ArrayList<Company> getAllCompanies(){
		ArrayList<Company> compList = null;
		
		try {
			compList = compDbDao.getAllCompanies();
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		}
		return compList;
	}
	
	/**
	 * 	This method creates a new Customer
	 * @param customer is the parameter to createCustomer method
	 */  
	public void createCustomer(Customer customer){
		try {
			custDbDao.createCustomer(customer);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CustomerNameAlreadyExsitsException e) {
			System.out.println(e.getMessage());
		}
	}

	
	/**
	 * 	This method removes a Customer
	 * @param customer is the parameter to removeCustomer method
	 */  
	public void removeCustomer(Customer customer){	
		try {
			custDbDao.removeCustomer(customer);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CustomerNameNotFound e) {
			System.out.println(e.getMessage());
		}	
	}

	/**
	 * 	This method updates the data of an existing customer Except for customer name
	 * @param customer is the parameter to updateCustomer method
	 */ 
	public void updateCustomer(Customer customer){
		try {
			custDbDao.updateCustomer(customer);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CustomerNameNotFound e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This method display the data of a specific customer
	 * @param id is the parameter to getCustomer method
	 * @return Customer or null
	 */ 
	public Customer getCustomer(long id){
		Customer customer = null;	
		
		try {
			return customer = custDbDao.getCustomer(id);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CustomerIdNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		return customer;
	}
	
	/**
	 * 	This method display the data of all customers exists in database
	 * @return an ArrayList of Customer or null
	 */  
	public ArrayList<Customer> getAllCustomer(){
		ArrayList<Customer> custList = null;

		try {
			custList = custDbDao.getAllCustomer();
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		}
		return custList;
	}
	
	/**
	 * 	This method display the data of all coupons exists in database
	 * @return an ArrayList of Coupon or null
	 */  
	public ArrayList<Coupon> getAllCoupon() {
		ArrayList<Coupon> couponList = null;
		
		try {
			couponList = couponDbDao.getAllCoupon();
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		}
		return couponList;
	}
	
	
	/**
	 * 	This method provides access to Admin user, doesnt go to database, uses local data
	 * @param String name is the first parameter to login method
	 * @param passwors is the second parameter to login method
	 * @param clientType is the third parameter to login method
	 * @return AdminFacade or null
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType) {
		if(name.equals("admin") && password.equals("1234")){ 
			return this;
		} else {
			System.out.println("ALERT: wrong admin credentials");
		}
		return null;
	}
}
