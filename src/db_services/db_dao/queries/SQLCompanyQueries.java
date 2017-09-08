package db_services.db_dao.queries;

import dataObjects.Company;

public final class SQLCompanyQueries {
 
	public static final String SELECT_COMPANY_ID_BY_COMP_NAME = "SELECT ID FROM coupons.Company WHERE COMP_NAME =?";
	
	public static final String INSERT_NEW_COMPANY = "INSERT INTO coupons.Company (COMP_NAME,PASSWORD,EMAIL) VALUES (?,?,?)";
	
	public static final String DELETE_COMPANY_FROM_COMPANY_TABLE = "DELETE FROM coupons.Company WHERE ID=?";
	
	public static final String UPDATE_COMPANY_EMAIL = "UPDATE coupons.Company SET EMAIL=? WHERE ID=?";
			
	public static final String UPDATE_COMPANY_PASSWORD = "UPDATE coupons.Company SET PASSWORD=? WHERE ID=?";
	
	public static final String UPDATE_COMPANY_EMAIL_PASSWORD = "UPDATE coupons.Company SET PASSWORD=?,EMAIL=? WHERE ID=?";
	
	public static final String SELECT_COMPANY_BY_ID = "SELECT * FROM coupons.Company WHERE ID=?";
	
	public static final String SELECT_ALL_COMPANIES = "SELECT * FROM coupons.Company";

	public static final String COMPANY_LOGIN_CHECK = "SELECT * FROM coupons.Company WHERE COMP_NAME =? AND PASSWORD = ?";
}
