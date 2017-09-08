package client;

import java.sql.Date;
import java.util.ArrayList;
//import java.util.Date;

import dataObjects.Company;
import dataObjects.Coupon;
import dataObjects.CouponType;
import dataObjects.Customer;
import db_services.CompanyDAO;
import db_services.CouponClientFacade;
import db_services.db_dao.CompanyDBDAO;
import db_services.db_dao.CouponDBDAO;
import exceptions.CouponNameAlreadyExistsException;
import exceptions.CouponNameNotFoundException;
import exceptions.CouponNotBelongToCompanyException;
import exceptions.CustomSqlSyntaxException;
import exceptions.LoginFailedException;

public class CompanyFacade implements CouponClientFacade {
	private CouponDBDAO couponDbDao = null;
	private CompanyDBDAO compDbDao = null;
	
	public CompanyFacade(){
		compDbDao = new CompanyDBDAO();
		couponDbDao = new CouponDBDAO();
	}
	
	/**
	 * 	This method creates new coupon
	 * @param coupon is the parameter to createCoupon method
	 */
	public void createCoupon(Coupon coupon){
		try {
			couponDbDao.createCoupon(coupon);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CouponNameAlreadyExistsException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 	This method removes an existing coupon
	 * @param coupon is the parameter to createCoupon method
	 */
	
	public void removeCoupon(Coupon coupon){
		try {
			couponDbDao.removeCoupon(coupon);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CouponNameNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (CouponNotBelongToCompanyException e) {
			System.out.println(e.getMessage());
		}	
	}
	
	/**
	 * 	This method update an existing coupon
	 * @param coupon is the parameter to updateeCoupon method
	 */
	public void updateCoupon(Coupon coupon){
		try {
			couponDbDao.updateCoupon(coupon);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CouponNameNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 	This method display a specific coupon by id of the connected company
	 * @param long id is the parameter to getCoupon method
	 * @return Coupon
	 */
	public Coupon getCoupon(long id){
		Coupon coupon = null;
		
		try {
			return couponDbDao.getCoupon(id);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (CouponNotBelongToCompanyException e) {
			System.out.println(e.getMessage());
		}
		return coupon;
	}
	
	/**
	 * 	This method display a list of all the coupons of the connected company
	 * @return ArrayList<Coupon> Coupon or null
	 */
	public ArrayList<Coupon> getCoupons() {
		ArrayList<Coupon> couponList = null;	
		
		try {
			couponList = compDbDao.getCoupons();
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		}
		return couponList;
	}

	/**
	 * 	This method display a list of all the coupons  by chosen type of the connected company
	 * @return ArrayList<Coupon> Coupon or null
	 */
	public ArrayList<Coupon> getCouponByType(CouponType couponType) {
		ArrayList<Coupon> couponListByType = null;
		
		try {
			couponListByType = couponDbDao.getCouponByType(couponType);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		}
		return couponListByType;
	}
	
	/**
	 * 	This method display a list of all the coupons  by chosen price of the connected company
	 * @return ArrayList<Coupon> Coupon or null
	 */
	public ArrayList<Coupon> getCouponByPrice(double price) {
		ArrayList<Coupon> couponListByPrice = null;
		
		try {
			couponListByPrice = couponDbDao.getCouponByPrice(price);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		}
		return couponListByPrice;
	}

	/**
	 * 	This method display a list of all the coupons by chosen date of the connected company
	 * @return ArrayList<Coupon> Coupon or null
	 */
	public ArrayList<Coupon> getCouponByDate(Date endDate) {
		ArrayList<Coupon> couponListByDate = null;
		
		try {
			couponListByDate = couponDbDao.getCouponByDate(endDate);
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		}
		return couponListByDate;
	}
	
	/**
	 * 	This method provides access to Company user
	 * @param String name is the first parameter to login method
	 * @param passwors is the second parameter to login method
	 * @param clientType is the third parameter to login method
	 * @return CompanyFacade or null
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType) {			
		boolean logResult = false;
		
		try {
			logResult = compDbDao.login(name, password);
			
			if(logResult){		
				couponDbDao.setIdOfOwner(compDbDao.getLogedInCompanyId());
				return this; //if the login succeded then we return the facade itself
			}
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		
		return null;	// if we hit an exception or got false in return then we should return null 
	}
}
