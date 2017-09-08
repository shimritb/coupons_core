package client;

import java.sql.SQLException;
import java.util.ArrayList;

import dataObjects.Company;
import dataObjects.Coupon;
import dataObjects.CouponType;
import db_services.CompanyDAO;
import db_services.CouponClientFacade;
import db_services.CustomerDAO;
import db_services.db_dao.CompanyDBDAO;
import db_services.db_dao.CouponDBDAO;
import db_services.db_dao.CustomerDBDAO;
import exceptions.CouponAmountIsEmptyException;
import exceptions.CouponExpiredException;
import exceptions.CouponWasPurchasedException;
import exceptions.CustomSqlSyntaxException;
import exceptions.LoginFailedException;

public class CustomerFacade implements CouponClientFacade {
	private CustomerDBDAO custDbDao = null;
	private CouponDBDAO couponDbDao = null;
	
	/**
	 * 	This Constructor contains/creats new objects of dbdao in order to....
	 */
	public CustomerFacade(){
		custDbDao =  new CustomerDBDAO();
		couponDbDao = new CouponDBDAO();
	}
	
	/**
	 * 	This method enable the customer to purchase coupon on some conditions
	 * @param coupon is the parameter to purchaseCoupon method
	 */
	public void purchaseCoupon(Coupon coupon) {
		try {
			custDbDao.purchaseCoupon(coupon);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CouponAmountIsEmptyException e) {
			System.out.println(e.getMessage());
		} catch (CouponExpiredException e) {
			System.out.println(e.getMessage());
		} catch (CouponWasPurchasedException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 	This method display all the coupons who was purchased by the connected user -customer.
	 * @return ArrayList<Coupon> or null
	 */
	public ArrayList<Coupon> getAllPurchasedCoupons() {
		ArrayList<Coupon> couponList = null;
		
		try {
			couponList = custDbDao.getAllPurchasedCoupons();
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		}	
		return couponList;
	}
	
	/**
	 * 	This method display all the coupons of the wanted type who was purchased by the connected user -customer.
	 * @return ArrayList<Coupon> or null
	 */		
	public ArrayList<Coupon> getAllPurchasedCouponsByType(CouponType couponType){
		ArrayList<Coupon> couponListByType = null;
		
		try {
			couponListByType = couponDbDao.getAllPurchasedCouponsByType(couponType);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		}
		return couponListByType;
	}
	
	/**
	 * 	This method display all the coupons of specific price who was purchased by the connected user -customer.
	 * @return ArrayList<Coupon> or null
	 */
	public ArrayList<Coupon> getAllPurchasedCouponsByPrice(double price){
		ArrayList<Coupon> couponListByPrice = null;
		
		try {
			couponListByPrice = couponDbDao.getAllPurchasedCouponsByPrice(price);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		}
		return couponListByPrice;
	}
	
	/**
	 * 	This method provides access to Customer user
	 * @param String name is the first parameter to login method
	 * @param passwors is the second parameter to login method
	 * @param clientType is the third parameter to login method
	 * @return CustomerFacade or null
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType){		
		boolean logResult = false;
		
		try {
			logResult = custDbDao.login(name, password);
			
			if(logResult){		
				couponDbDao.setIdOfOwner(custDbDao.getLoggedInCustomerId());
				return this; //if the login succeded then we return the facade itself
			}
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		return null; // if we hit an exception or got false in return then we should return null
	}
}
