package db_services;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.Date;

import dataObjects.Coupon;
import dataObjects.CouponType;
/**
 * 	This methods will be implemented in CouponDbDao  
 */
public interface CouponDAO {
	/**
	 * 
	 */
	public  void createCoupon(Coupon coupon) throws SQLException, Exception;
	/**
	 * 
	 * @param coupon
	 * @throws SQLException
	 * @throws Exception
	 */
	public void removeCoupon(Coupon coupon) throws SQLException, Exception;
	/**
	 * 
	 * @param coupon
	 * @throws SQLException
	 * @throws Exception
	 */
	public void updateCoupon(Coupon coupon) throws SQLException, Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public Coupon getCoupon(long id) throws SQLException, Exception;
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Coupon> getAllCoupon() throws SQLException;
	/**
	 * 
	 * @param couponType
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Coupon> getCouponByType(CouponType couponType) throws SQLException;
	/**
	 * 
	 * @param price
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Coupon> getCouponByPrice(double price) throws SQLException;
	/**
	 * 
	 * @param endDate
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Coupon> getCouponByDate(Date endDate) throws SQLException;
	/**
	 * 
	 * @param couponType
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Coupon> getAllPurchasedCouponsByType(CouponType couponType) throws SQLException;
	/**
	 * 
	 * @param price
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Coupon> getAllPurchasedCouponsByPrice(double price) throws SQLException;
}
