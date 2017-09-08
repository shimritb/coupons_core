package db_services.db_dao.queries;

public class SQLCompany_CouponQueries {
	
	public static final String SELECT_COUPONSIDS_BY_COMPANY_ID = "SELECT * FROM coupons.Company_Coupon WHERE COMP_ID=?";
	
	public static final String DELETE_COMPANY_FROM_COMPANY_COUPON_TABLE ="DELETE FROM coupons.Company_Coupon WHERE COMP_ID=?";
	
	public static final String DELETE_COUPON_FROM_COMPANY_COUPON_TABLE = "DELETE FROM coupons.Company_Coupon WHERE COUPON_ID=?";
	
	public static final String CHECK_IF_COUPON_BELONG_TO_COMPANY = "SELECT * FROM coupons.Company_Coupon WHERE COMP_ID=? AND COUPON_ID=?";
	
	

}
