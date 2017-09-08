package db_services.db_dao.queries;

public class SQLCouponQueries {
	
	public static final String SELECT_COUPON_BY_ITS_ID = "SELECT * FROM coupons.Coupon WHERE ID =?";
	
	public static final String UPDATE_CURRENT_AMOUNT_IN_COUPON_TABLE = "UPDATE coupons.Coupon SET AMOUNT=(AMOUNT-1) WHERE ID=?";
	
	public static final String SELECT_COUPON_ID_BY_COUPON_NAME = "SELECT ID FROM coupons.Coupon WHERE COUPON.TITLE =?";
	
	public static final String INSERT_NEW_COUPON = "INSERT INTO coupons.Coupon (TITLE,START_DATE,END_DATE,AMOUNT,TYPE,MESSAGE,PRICE,IMAGE) VALUES (?,?,?,?,?,?,?,?)";
	
	public static final String DELETE_COUPON_FROM_COUPON_TABLE = "DELETE FROM coupons.Coupon WHERE ID=?";
	
	public static final String UPDATE_COUPON = "UPDATE coupons.Coupon SET END_DATE=?,PRICE=? WHERE ID=?";

	public static final String SELECT_SPECIFIC_COUPON = "SELECT * FROM coupons.Coupon WHERE ID=?";
	
	public static final String SELECT_ALL_COUPONS = "SELECT * FROM coupons.Coupon";
	
	public static final String SELECT_COUPON_BY_TYPE_OWNED_BY_COMPANY = "SELECT * FROM coupons.Coupon WHERE TYPE =? AND ID IN (SELECT COUPON_ID FROM Company_Coupon WHERE COMP_ID =?)";
	
	public static final String SELECT_COUPON_BY_PRICE_OWNED_BY_COMPANY = "SELECT * FROM coupons.Coupon WHERE PRICE < ? AND ID IN (SELECT COUPON_ID FROM Company_Coupon WHERE COMP_ID=?)";
	
	public static final String SELECT_COUPON_BY_DATE_OWNED_BY_COMPANY = "SELECT * FROM coupons.Coupon WHERE END_DATE < ? AND ID IN (SELECT COUPON_ID FROM Company_Coupon WHERE COMP_ID=?)";
	
	public static final String SELECT_PURCHASED_COUPONS_BY_TYPE_OWNED_BY_CUSTOMER = "SELECT * FROM coupons.Coupon WHERE TYPE =? AND ID IN (SELECT COUPON_ID FROM Customer_Coupon WHERE CUSTOMER_ID =?)";
	
	public static final String SELECT_PURCHASED_COUPONS_BY_PRICE_OWNED_BY_CUSTOMER = "SELECT * FROM coupons.Coupon WHERE PRICE <? AND ID IN (SELECT COUPON_ID FROM Customer_Coupon WHERE CUSTOMER_ID =?)";
	
}
