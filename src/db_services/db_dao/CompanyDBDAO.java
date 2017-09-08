package db_services.db_dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//import client.CompNameExists;
//import client.removeCompanyFromCompanyTbl;
//import client.removeCompanyFromCouponCompanyTbl;
import dataObjects.Company;
import dataObjects.Coupon;
import dataObjects.CouponType;
import db_services.CompanyDAO;
import db_services.connection_pool.ConnectionPool;
import db_services.db_dao.queries.SQLCompanyQueries;
import db_services.db_dao.queries.SQLCompany_CouponQueries;
import db_services.db_dao.queries.SQLCouponQueries;
import db_services.db_dao.queries.SQLCustomerQueries;
import db_services.db_dao.sql_fields.SQLCompanyFields;
import db_services.db_dao.sql_fields.SQLCouponFields;
import db_services.db_dao.sql_fields.SQLCustomerFields;
import exceptions.CompanyIdNotFoundException;
import exceptions.CompanyNameAlreadyExistsException;
import exceptions.CompanyNameDoesNotExistsException;
import exceptions.CustomSqlSyntaxException;
import exceptions.ExceptionStrings;
import exceptions.LoginFailedException;


public class CompanyDBDAO implements CompanyDAO {
	private ConnectionPool connPoolInstance = null;
	private long loggedInCompanyId;
	
	/**
	 * public Constructor
	 * calling to create an instance of a connectionPool
	 */
	public CompanyDBDAO (){
		connPoolInstance = ConnectionPool.getInstance();
	}
	
	/**
	 * This method checks whether the logged company is already exists in db
	 * @param compName is the parameter to compNameExists method
	 * return boolean
	 * @throws CompanyNameAlreadyExistsException 
	 * */
	private boolean compNameExists(String compName) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Connection connection = null;
		boolean nameExistsStatus = false;
		
		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCompanyQueries.SELECT_COMPANY_ID_BY_COMP_NAME);
			pstmt.setString(1, compName);
			result = pstmt.executeQuery();
				
			if(result.next()){
				nameExistsStatus = true; // company exists in table
			} 
		} finally {
			connPoolInstance.returnConnection(connection);
		}	
		return nameExistsStatus;
	}

	/**
	 * @see CompNameExists {@link #CompNameExists}
	 * */
	@Override
	public void createCompany(Company company) throws CompanyNameAlreadyExistsException, CustomSqlSyntaxException {
		PreparedStatement pstmt = null;		
		Connection connection = null;
		
		String name = company.getCompName();
		String password = company.getPassword();
		String email = company.getEmail();
		
		try {
			if(!compNameExists(name)) {
				try {
					connection = connPoolInstance.getConnection();
					pstmt = connection.prepareStatement(SQLCompanyQueries.INSERT_NEW_COMPANY);
					pstmt.setString(1, name);
					pstmt.setString(2, password);
					pstmt.setString(3, email);
					pstmt.executeUpdate();
				} finally {
					connPoolInstance.returnConnection(connection);
				}
			} else 
				throw new CompanyNameAlreadyExistsException(ExceptionStrings.COMP_NAME_ALREADY_EXSITS);
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
	}
	
	/**
	 * @see CompNameExists {@link #CompNameExists(String compName)}
	 * @see removeCompanyFromCompanyTbl {@link #removeCompanyFromCompanyTbl(Long id)}
	 * @see removeCompanyFromCouponCompanyTbl {@link #removeCompanyFromCouponCompanyTbl(Long id)}
	 * */
	@Override
	public void removeCompany(Company company) throws CompanyNameDoesNotExistsException, CustomSqlSyntaxException {
		long id = company.getId();
		
		try {
			if(compNameExists(company.getCompName())) {
				removeCompanyFromCompanyTbl(id);
				removeCompanyFromCouponCompanyTbl(id);
			} else 
				throw new CompanyNameDoesNotExistsException(ExceptionStrings.COMP_NAME_DOES_NOT_EXSITS);
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
	} 
	/**
	 * This function is made for executing query number 1 for remove company function
	 * @param id
	 * @throws SQLException
	 */
	private void removeCompanyFromCompanyTbl(Long id) throws SQLException {		//this function is made for executing query number 1 for remove company function
		PreparedStatement pstmt = null;		
		Connection connection = null;
		
		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCompanyQueries.DELETE_COMPANY_FROM_COMPANY_TABLE);
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
		} finally {
			connPoolInstance.returnConnection(connection);
		}
	}
	/**
	 * This function is made for executing query number 2 for remove company function
	 * @param id
	 * @throws SQLException
	 */
	private void removeCompanyFromCouponCompanyTbl(Long id) throws SQLException { 
		PreparedStatement pstmt = null;		
		Connection connection = null;
		
		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCompany_CouponQueries.DELETE_COMPANY_FROM_COMPANY_COUPON_TABLE);
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
		}  finally {
			connPoolInstance.returnConnection(connection);
		}
	}
	/**
	 * 
	 * @param company
	 * @throws CompanyNameDoesNotExistsException
	 * @throws CustomSqlSyntaxException
	 * @see compNameExists {@link #compNameExists(String compName)}
	 */
	@Override
	public void updateCompany(Company company) throws CompanyNameDoesNotExistsException, CustomSqlSyntaxException {
		PreparedStatement pstmt = null;	
		Connection connection = null;
		
		try {
			if(compNameExists(company.getCompName())) {
				long id = company.getId();				
				String password = company.getPassword();
				String email = company.getEmail();
					 
				connection = connPoolInstance.getConnection();
				
				try {
					if(password == null && email != null){
						pstmt = connection.prepareStatement(SQLCompanyQueries.UPDATE_COMPANY_EMAIL);
						pstmt.setString(1, email);
						pstmt.setLong(2, id);
					} else if (email == null && password != null){
						pstmt = connection.prepareStatement(SQLCompanyQueries.UPDATE_COMPANY_PASSWORD);
						pstmt.setString(1, password);
						pstmt.setLong(2, id);
					} else if(email != null && password != null){
						pstmt = connection.prepareStatement(SQLCompanyQueries.UPDATE_COMPANY_EMAIL_PASSWORD);
						pstmt.setString(1, password);
						pstmt.setString(2, email);
						pstmt.setLong(3, id);
					} else return;
					
					pstmt.executeUpdate();
				} finally {
					connPoolInstance.returnConnection(connection);
				}	
			} else throw new CompanyNameDoesNotExistsException (ExceptionStrings.COMP_NAME_DOES_NOT_EXSITS);
		} catch (SQLException e) {
				throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}	
	}

	/**
	 * This method display the data of a specific company
	 * @param id
	 * @return Company
	 * @throws CompanyIdNotFoundException
	 * @throws CustomSqlSyntaxException
	 * @see getAllCouponIdsOwnedByCompany {@link #getAllCouponIdsOwnedByCompany(company.getId())}
	 * @see getAllCouponsOwnedByCompany {@link #getAllCouponsOwnedByCompany(couponIdList)}
	 */
	@Override
	public Company getCompany(long id) throws CompanyIdNotFoundException, CustomSqlSyntaxException {
		PreparedStatement pstmt = null;		
		Connection connection = null;
		ResultSet rs = null;
		
		connection = connPoolInstance.getConnection();
		
		Company company = null;	// we claim a memory for the object but do not yet making an instance of the object
		
		try {
			pstmt = connection.prepareStatement(SQLCompanyQueries.SELECT_COMPANY_BY_ID);
			pstmt.setLong(1,id);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				company = new Company(); // if the result is not empty then we actually making the instance of the object
				company.setId(rs.getLong(SQLCompanyFields.COMPANY_ID));
				company.setCompName(rs.getString(SQLCompanyFields.COMPANY_NAME).trim());
				company.setPassword(rs.getString(SQLCompanyFields.COMPANY_PASSWORD).trim());
				company.setEmail(rs.getString(SQLCompanyFields.COMPANY_EMAIL).trim());
				
				// the goal is to extract the last piece of the company object (Array list of coupons)
				// 1. get all the ids of coupons owened by some company from join table
				// 2. get all the objects-coupons from coupons table by the ids that we got from join table in step 1
				ArrayList<Long> couponIdList = getAllCouponIdsOwnedByCompany(company.getId()); 
				ArrayList<Coupon> couponList = getAllCouponsOwnedByCompany(couponIdList);
				company.setCoupons(couponList); 
			} else throw new CompanyIdNotFoundException(ExceptionStrings.COMP_ID_NOT_FOUND);
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		} finally {
			connPoolInstance.returnConnection(connection);
		}
		return company;	
	}

	/**
	 * This method display the data of all companies exists in database
	 * @return ArrayList<Company>
	 * @throws CustomSqlSyntaxException
	 * @see getAllCouponIdsOwnedByCompany {@link #getAllCouponIdsOwnedByCompany(company.getId())}
	 * @see getAllCouponsOwnedByCompany {@link #getAllCouponsOwnedByCompany(couponIdList)}
	 */
	@Override
	public ArrayList<Company> getAllCompanies() throws CustomSqlSyntaxException {
		Statement stmt = null;		
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Company> compList = null;
		
		connection = connPoolInstance.getConnection(); 
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(SQLCompanyQueries.SELECT_ALL_COMPANIES);
			
			if(rs.next()){	// checking if result that we got from database is not empty
				compList = new ArrayList<>();
				do{
					Company company = new Company();
					company.setId(rs.getLong(SQLCompanyFields.COMPANY_ID));
					company.setCompName(rs.getString(SQLCompanyFields.COMPANY_NAME).trim());
					company.setPassword(rs.getString(SQLCompanyFields.COMPANY_PASSWORD).trim());
					company.setEmail(rs.getString(SQLCompanyFields.COMPANY_EMAIL).trim());
					
					ArrayList<Long> couponIdListRs = getAllCouponIdsOwnedByCompany(company.getId()); 
					ArrayList<Coupon> couponListRs = getAllCouponsOwnedByCompany(couponIdListRs);
					
					company.setCoupons(couponListRs);
							
					compList.add(company);
				} while(rs.next());
			}		
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		} finally {
			connPoolInstance.returnConnection(connection);
		}
		return compList;
	} 
	/**
	 * This method display all the id of the coupons that belong to the companyId that is given
	 * @param companyId
	 * @return ArrayList<Long> couponsIdList
	 * @throws SQLException
	 */
	private ArrayList<Long> getAllCouponIdsOwnedByCompany(long companyId) throws SQLException {
		PreparedStatement pstmt = null;			
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Long> couponsIdList = null;
		
		connection = connPoolInstance.getConnection();
		
		try {
			
			pstmt = connection.prepareStatement(SQLCompany_CouponQueries.SELECT_COUPONSIDS_BY_COMPANY_ID);
			pstmt.setLong(1,companyId);
			rs = pstmt.executeQuery();

			if(rs.next()==true){	// checking if result that we got from db is not empty
				couponsIdList = new ArrayList<>();
				do{
					
					couponsIdList.add(rs.getLong("Coupon_ID"));
				} while(rs.next() == true);
			}	
	
		} finally {
			connPoolInstance.returnConnection(connection);
		}
		return couponsIdList;
	} 
	/**
	 * This method display all coupons objects according to the id list from the last function
	 * @param couponsIdList
	 * @return ArrayList<Coupon> couponList
	 * @throws SQLException
	 */
	private ArrayList<Coupon> getAllCouponsOwnedByCompany(ArrayList<Long> couponsIdList) throws SQLException { 
		PreparedStatement pstmt = null;		
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Coupon> couponList = new ArrayList<>();
		
		if(couponsIdList != null && !couponsIdList.isEmpty()){			
			for(Long couponId: couponsIdList){

				connection = connPoolInstance.getConnection();
				
				try {
					pstmt = connection.prepareStatement(SQLCouponQueries.SELECT_COUPON_BY_ITS_ID);
					pstmt.setLong(1,couponId);
					rs = pstmt.executeQuery();

					if(rs.next()){
						Coupon coupon = new Coupon();
						coupon.setId(rs.getLong(SQLCouponFields.COUPON_ID));
						coupon.setTitle(rs.getString(SQLCouponFields.COUPON_TITLE).trim());
						coupon.setStartDate(rs.getDate(SQLCouponFields.COUPON_START_DATE));
						coupon.setEndDate(rs.getDate(SQLCouponFields.COUPON_END_DATE));
						coupon.setAmount(rs.getInt(SQLCouponFields.COUPON_AMOUNT));
						String type = rs.getString(SQLCouponFields.COUPON_TYPE).trim();
						coupon.setType(CouponType.valueOf(type));
						coupon.setMessage(rs.getString(SQLCouponFields.COUPON_MESSAGE).trim());
						coupon.setPrice(rs.getDouble(SQLCouponFields.COUPON_PRICE));
						coupon.setImage(rs.getString(SQLCouponFields.COUPON_IMAGE).trim());
						
						couponList.add(coupon);	
						connPoolInstance.returnConnection(connection);
					}
					else {
						connPoolInstance.returnConnection(connection);
						continue; //continues into next iteration in the loop
					}			
				} finally {
					connPoolInstance.returnConnection(connection);
				}
			} 
		}
		return couponList;
	}

	//if the company doesnt have coupons that she created ,then an empty arraylist will return
	/**
	 * 
	 * @return ArrayList<Coupon> couponList
	 * @throws CustomSqlSyntaxException
	 */
	@Override
	public ArrayList<Coupon> getCoupons() throws CustomSqlSyntaxException {
		ArrayList<Coupon> couponList = null;
		
		try {
			ArrayList<Long> couponIdList = getAllCouponIdsOwnedByCompany(loggedInCompanyId); 
			couponList = getAllCouponsOwnedByCompany(couponIdList);
		} catch (SQLException e){
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
		return couponList;
	} 
	/**
	 * This method checks if the company exists in database and return a boolean response and 
	 * throws 2 kinds of Exception, one is logic and the other is sqlSyntaxError Exception
	 * @param compName
	 * @param password
	 * @return boolean loginStatus
	 * @throws CustomSqlSyntaxException
	 * @throws LoginFailedException
	 */
	@Override
	public boolean login(String compName, String password) throws CustomSqlSyntaxException, LoginFailedException {		
		PreparedStatement pstmt = null;	
	    ResultSet rs = null; 
	    Connection connection = null;
	    boolean loginStatus = false;
	    	
		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCompanyQueries.COMPANY_LOGIN_CHECK);
			pstmt.setString(1, compName);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
						
			if (rs.next()) {
				this.loggedInCompanyId = rs.getLong(SQLCompanyFields.COMPANY_ID);
				connPoolInstance.returnConnection(connection);
				loginStatus = true;		
			} else 
				throw new LoginFailedException(ExceptionStrings.LOGIN_FAILED); // throw logic exception
		} catch(SQLException e){
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage()); // throw custom sql exception
		} finally {
			connPoolInstance.returnConnection(connection);
		} 
		
		return loginStatus;
	}

	public long getLogedInCompanyId() {
		return this.loggedInCompanyId;
	}

}
