package db_services.db_dao.queries;

public class SQLCustomer_CouponQueries {
	
	public static final String DELETE_CUSTOMER_FROM_CUSTOMER_COUPON_TABLE = "DELETE FROM coupons.Customer_Coupon WHERE CUSTOMER_ID=?";
	
	public static final String SELECT_COUPONSIDS_BY_CUSTOMER_ID = "SELECT * FROM coupons.Customer_Coupon where CUSTOMER_ID=?";
	
	public static final String INSERT_PURCHASED_COUPONS  = "INSERT INTO coupons.Customer_Coupon (CUSTOMER_ID,COUPON_ID) VALUES (?,?)";
	
	public static final String SELECT_PURCHASED_COUPONS = "SELECT * FROM coupons.Customer_Coupon WHERE COUPON_ID =? AND CUSTOMER_ID = ?";

}
