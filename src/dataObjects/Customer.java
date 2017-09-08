package dataObjects;

import java.util.ArrayList;

public class Customer {

	private long id;
	private String custName;
	private String password;
	private ArrayList <Coupon> coupons;

	/**
	 * default Constructor of Customer class
	 */
	public Customer(){
	
	}
	/**
	 * This method returns the id of the Customer
	 * @return id
	 */
	public long getId() {
		return id;
	}
	/**
	 * This method set the id of the Customer
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * This method returns the name of the Customer
	 * @return custName
	 */
	public String getCustName() {
		return custName;
	}
	/**
	 * This method set the custName of the Customer
	 * @param custName
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}
	/**
	 * This method returns the password of the Customer
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * This method set the password of the Customer
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * This method returns all the Coupons of the Customer
	 * @return ArrayList<Coupon> coupons
	 */
	public ArrayList<Coupon> getCoupons() {
		return coupons;
	}
	/**
	 * This method set the Coupons of the Customer
	 * @param ArrayList<Coupon> coupons
	 */
	public void setCoupons(ArrayList<Coupon> coupons) {
		this.coupons = coupons;
	}
	/** 
	 * This method display all the fields of Customer object
	 * @return String
	 */
	@Override
	public String toString() {
		return "\n Customer [id=" + id + ", custName=" + custName + ", password=" + password + ", coupons=" + coupons + "]";
	}
}