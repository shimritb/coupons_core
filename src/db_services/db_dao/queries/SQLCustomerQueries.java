package db_services.db_dao.queries;

public class SQLCustomerQueries {
	
	public static final String SELECT_CUSTOMER_ID_BY_CUSTOMER_NAME = "SELECT ID FROM coupons.Customer WHERE CUST_NAME =?";
	
	public static final String INSERT_NEW_CUSTOMER = "INSERT INTO coupons.Customer (CUST_NAME,PASSWORD) VALUES (?,?)";
	
	public static final String DELETE_CUSTOMER_FROM_CUSTOMER_TABLE = "DELETE FROM coupons.Customer WHERE ID=?";
	
	public static final String UPDATE_CUSTOMER = "UPDATE coupons.Customer SET PASSWORD=? WHERE ID=?";
	
	public static final String SELECT_CUSTOMER_BY_ID = "SELECT * FROM coupons.Customer WHERE ID=?";
	
	public static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM coupons.Customer";
	
	public static final String CUSTOMER_LOGIN_CHECK =  "SELECT * FROM coupons.Customer WHERE CUST_NAME =? AND PASSWORD =?";
	
}
