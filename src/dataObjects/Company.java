package dataObjects;

import java.util.ArrayList;


public class Company {
	private long id;
	private String compName;
	private String password;
	private String email;
	private ArrayList <Coupon> coupons;
	
	/**
	 * default Constructor of Company class
	 */
	public Company(){
		
	}

	/**
	 * This method returns the id of the Company
	 * @return id
	 */
	public long getId() {
		return id;
	}
	/**
	 * This method set the id of Company
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * This method returns the name of the Company
	 * @return compName
	 */
	public String getCompName() {
		return compName;
	}
	/**
	 * This method set the name of Company
	 * @param compName
	 */
	public void setCompName(String compName) {
		this.compName = compName;
	}
	/**
	 * This method get the password of the Company
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * This method set the password of the Company
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * This method get the email of the Company
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * This method set the email of the Company
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * This method gets all the coupons of the Company
	 * @return ArrayList<Coupon> coupons
	 */
	public ArrayList<Coupon> getCoupons() {
		return coupons;
	}
	/**
	 * This method sets the coupons of the Company
	 * @param ArrayList<Coupon> coupons
	 */
	public void setCoupons(ArrayList<Coupon> coupons) {
		this.coupons = coupons;
	}
	/** 
	 * This method display all the fields of Company object
	 * @return String
	 */
	@Override
	public String toString() {
		if (coupons == null )
			return "\n Company [id=" + id + ", compName=" + compName + ", password=" + password + ", email=" + email;
		else
			return "\n Company [id=" + id + ", compName=" + compName + ", password=" + password + ", email=" + email + " coupons:" + coupons.toString() + "]";
	}
}
