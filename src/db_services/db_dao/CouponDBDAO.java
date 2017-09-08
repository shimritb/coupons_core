package db_services.db_dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import dataObjects.Company;
import dataObjects.Coupon;
import dataObjects.CouponType;
import db_services.CouponDAO;
import db_services.connection_pool.ConnectionPool;
import db_services.db_dao.queries.SQLCompanyQueries;
import db_services.db_dao.queries.SQLCompany_CouponQueries;
import db_services.db_dao.queries.SQLCouponQueries;
import db_services.db_dao.sql_fields.SQLCompanyFields;
import db_services.db_dao.sql_fields.SQLCouponFields;
import exceptions.CouponNameAlreadyExistsException;
import exceptions.CouponNameNotFoundException;
import exceptions.CouponNotBelongToCompanyException;
import exceptions.CustomSqlSyntaxException;
import exceptions.ExceptionStrings;

public class CouponDBDAO implements CouponDAO {
	private ConnectionPool connPoolInstance = null;
	private long ownerId;
	
	public CouponDBDAO(){
		connPoolInstance = ConnectionPool.getInstance();
	}
	
	/**
	 * 	This method checks if coupon name exists in database
	 * @param String couponTitle is the parameter to couponNameExists method
	 * @return boolean
	 */
	private boolean couponNameExists(String couponTitle) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Connection connection = null;
		
		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCouponQueries.SELECT_COUPON_ID_BY_COUPON_NAME);
			pstmt.setString(1, couponTitle);
			result = pstmt.executeQuery();
			
			if(result.next()){
				connPoolInstance.returnConnection(connection);
				return true; // coupon exists in table
			} 		
		} finally {
			connPoolInstance.returnConnection(connection);
		}
		return false;	
	}
	
	/**
	 * This method creates new coupon
	 * @param coupon is the parameter to createCoupon method
	 * @see couponNameExists {@link #couponNameExists(String couponTitle)}
	 */
	@Override
	public void createCoupon(Coupon coupon) throws CouponNameAlreadyExistsException, CustomSqlSyntaxException {
		PreparedStatement pstmt = null;		
		Connection connection = null;
		
		String name = coupon.getTitle();
		Date startDate = coupon.getStartDate();
		Date endDate = coupon.getEndDate();
		int amount = coupon.getAmount();
		CouponType type = coupon.getType();
		String message = coupon.getMessage();
		double price = coupon.getPrice();
		String image = coupon.getImage();

		connection = connPoolInstance.getConnection();
		
	try {
		if(!couponNameExists(coupon.getTitle())){
			try {
				pstmt = connection.prepareStatement(SQLCouponQueries.INSERT_NEW_COUPON);
				pstmt.setString(1, name);
				pstmt.setDate(2, startDate);
				pstmt.setDate(3, endDate);
				pstmt.setInt(4, amount);
				pstmt.setString(5, type.toString()); //return the string of the enum
				pstmt.setString(6, message);
				pstmt.setDouble(7, price);
				pstmt.setString(8, image);
				pstmt.executeUpdate();	
			} finally {
				connPoolInstance.returnConnection(connection);
			}
		} else
			throw new CouponNameAlreadyExistsException(ExceptionStrings.COUPON_NAME_ALREADY_EXSITS);
	} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR+e.getMessage());
		}
	}

	/**
	 * 	This method removes the coupon from coupon table and from coupon_company table at once
	 * @param coupon is the parameter to createCoupon method
	 * @see couponNameExists {@link #couponNameExists (String couponTitle)}
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws CouponNameNotFoundException, CustomSqlSyntaxException, CouponNotBelongToCompanyException {
		long couponId = coupon.getId();
		
		try {
			if(couponNameExists(coupon.getTitle())) {
				if(couponBelongsToCompany(ownerId,couponId)) {
					removeCouponFromCouponTbl(couponId);
					removeCouponFromCompanyCouponTbl(couponId);
				} else 
					throw new CouponNotBelongToCompanyException(ExceptionStrings.COUPON_NOT_BELONG_TO_COMPANY);
			} else 
				throw new CouponNameNotFoundException(ExceptionStrings.COUPON_NAME_NOT_FOUND);
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
	}
	/**
	 * This private method removes the coupon from coupon table
	 * @param couponId
	 * @throws SQLException
	 */
	private void removeCouponFromCouponTbl(Long couponId) throws SQLException {
		PreparedStatement pstmt = null;		
		Connection connection = null;
			
		connection = connPoolInstance.getConnection();
	
		try {
			pstmt = connection.prepareStatement(SQLCouponQueries.DELETE_COUPON_FROM_COUPON_TABLE);
			pstmt.setLong(1, couponId);
			pstmt.executeUpdate();
		} finally {
			connPoolInstance.returnConnection(connection);
		}
	}
	/**
	 * This private method removes the coupon from CompanyCoupon table
	 * @param couponId
	 * @throws SQLException
	 */
	private void removeCouponFromCompanyCouponTbl(Long couponId) throws SQLException {
		PreparedStatement pstmt = null;		
		Connection connection = null;
		
		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCompany_CouponQueries.DELETE_COUPON_FROM_COMPANY_COUPON_TABLE);
			pstmt.setLong(1, couponId);
			pstmt.executeUpdate();
		} finally {
			connPoolInstance.returnConnection(connection);
		}	
	}
	/**
	 * This private method checks whether the given coupon id belongs to the given compid
	 * @param compId
	 * @param couponId
	 * @return boolean
	 * @throws SQLException
	 */
	private boolean couponBelongsToCompany(long compId, long couponId) throws SQLException {	
		PreparedStatement pstmt = null;	
		Connection connection = null;
		ResultSet rs = null;
			
		connection = connPoolInstance.getConnection();
			
		try {
			pstmt = connection.prepareStatement(SQLCompany_CouponQueries.CHECK_IF_COUPON_BELONG_TO_COMPANY);
			pstmt.setLong(1, compId);
			pstmt.setLong(2, couponId);
			rs = pstmt.executeQuery();
				
			if(rs.next()) {
				connPoolInstance.returnConnection(connection);
				return true;
			} 
		} finally {
			connPoolInstance.returnConnection(connection);
		}
		return false;
	}
	
	/**
	 * 	This method updates the EndDate and price of the coupon from coupon table 
	 * @param coupon is the parameter to updateCoupon method
	 * @see couponNameExists {@link #couponNameExists (String couponTitle)}
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws CouponNameNotFoundException, CustomSqlSyntaxException {
		PreparedStatement pstmt = null;			
		Connection connection = null;
		
		try {
			if(couponNameExists(coupon.getTitle())) {	
				long id = coupon.getId();						
				Date endDate = coupon.getEndDate();
				double price = coupon.getPrice();
						
				connection = connPoolInstance.getConnection();
			
				try {
					pstmt = connection.prepareStatement(SQLCouponQueries.UPDATE_COUPON);
					pstmt.setDate(1, endDate);
					pstmt.setDouble(2, price);
					pstmt.setLong(3,id);
					pstmt.executeUpdate();
				} finally {
						connPoolInstance.returnConnection(connection);
				}
			} else 
				throw new CouponNameNotFoundException(ExceptionStrings.COUPON_NAME_NOT_FOUND);
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
	}

	/**
	 * 	This method select and display a coupon from coupon table by the id that is given by the user
	 * @param long id is the parameter to getCoupon method
	 * @see couponBelongsToCompany {@link #couponBelongsToCompany(long compId, long couponId)}
	 */
	@Override
	public Coupon getCoupon(long id) throws CouponNotBelongToCompanyException, CustomSqlSyntaxException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rs = null;
			
		connection = connPoolInstance.getConnection();
		Coupon coupon = null;	// we claim a memory for the object but do not yet making an instance of the object
			
		try {
			if(couponBelongsToCompany(ownerId,id)){
				pstmt = connection.prepareStatement(SQLCouponQueries.SELECT_SPECIFIC_COUPON);
				pstmt.setLong(1, id);
				rs = pstmt.executeQuery();					
				if(rs.next()){
					coupon = new Coupon(); // if the result is not empty then we actually making the instance of the object
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
				} 
			} else
				throw new CouponNotBelongToCompanyException(ExceptionStrings.COUPON_NOT_BELONG_TO_COMPANY);
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		} finally {
			connPoolInstance.returnConnection(connection);		
		}
		return coupon;				
	}

	/**
	 * 	This method display all the coupons existing in coupon table
	 * @return an ArrayList of Coupon 
	 * @throws CustomSqlSyntaxException
	 */  
	@Override
	public ArrayList<Coupon> getAllCoupon() throws CustomSqlSyntaxException {
		Statement stmt = null;		
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Coupon> couponList = null;

		connection = connPoolInstance.getConnection();
		Coupon coupon = null;	// we claim a memory for the object but do not yet making an instance of the object
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(SQLCouponQueries.SELECT_ALL_COUPONS); //adminfacade will use this method
			
			if(rs.next()){
				couponList = new ArrayList<>();
				do{
					coupon = new Coupon(); // if the result is not empty then we actually making the instance of the object
				
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
				} while(rs.next());
			}
		} catch (SQLException e) {
				throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
			} finally {
				connPoolInstance.returnConnection(connection);	
			}
		return couponList;				
	}

	/**
	 * 	This method display the coupons of a type chosen by the owner company user
	 * @param CouponType couponType
	 * @return an ArrayList of Coupon 
	 */  
	@Override
	public ArrayList<Coupon> getCouponByType(CouponType couponType) throws CustomSqlSyntaxException {
		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Coupon> couponList = null;
		
		connection = connPoolInstance.getConnection();
		Coupon coupon = null;	// we claim a memory for the object but do not yet making an instance of the object
		
		//the query checks wether the coupon belongs to the company because it goes to the company_coupon table
		//so there is no need to use the "coupon belongs to company" function
		try {
			pstmt = connection.prepareStatement(SQLCouponQueries.SELECT_COUPON_BY_TYPE_OWNED_BY_COMPANY);
			pstmt.setString(1, couponType.toString());
			pstmt.setLong(2, ownerId);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				couponList = new ArrayList<>();
				do{
					coupon = new Coupon(); // if the result is not empty then we actually making the instance of the object
				
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
				} while(rs.next());
			}
		} catch (SQLException e) {
				throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
		finally {
			connPoolInstance.returnConnection(connection);		
		}
		return couponList;	
	}

	/**
	 * 	This method display the coupons by price owned by company
	 * @param double price
	 * @return an ArrayList of Coupon 
	 */  
	@Override
	public ArrayList<Coupon> getCouponByPrice(double price) throws CustomSqlSyntaxException {
		PreparedStatement pstmt = null;		
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Coupon> couponList = null;
		
		connection = connPoolInstance.getConnection();
		Coupon coupon = null;
		
		try {
			pstmt = connection.prepareStatement(SQLCouponQueries.SELECT_COUPON_BY_PRICE_OWNED_BY_COMPANY);
			pstmt.setDouble (1, price);
			pstmt.setLong(2, ownerId);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				couponList = new ArrayList<>();
				do{
					coupon = new Coupon(); // if the result is not empty then we actually making the instance of the object
		
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
				} while(rs.next());
			}
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		} finally {
		connPoolInstance.returnConnection(connection);
		}
		return couponList;	
	}

	/**
	 * 	This method display the coupons by date owned by company
	 * @param Date endDate
	 * @return an ArrayList of Coupon 
	 */  
	@Override
	public ArrayList<Coupon> getCouponByDate(Date endDate) throws CustomSqlSyntaxException {
		PreparedStatement pstmt = null;			
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Coupon> couponList = null;
		
		connection = connPoolInstance.getConnection();
		Coupon coupon = null;	
		
		try {
			pstmt = connection.prepareStatement(SQLCouponQueries.SELECT_COUPON_BY_DATE_OWNED_BY_COMPANY);
			pstmt.setDate (1, endDate);
			pstmt.setLong(2, ownerId);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				couponList = new ArrayList<>();
				do{
					coupon = new Coupon(); // if the result is not empty then we actually making the instance of the object
				
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
				} while(rs.next());
			}
		} catch (SQLException e) {
				throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		} finally {
			connPoolInstance.returnConnection(connection);	
		} 
		return couponList;	
	} 

	/**
	 * 	This method sets the id of the entity which is logged in
	 * @param long ownerId is the parameter to setIdOfOwner
	 */  
	public void setIdOfOwner(long ownerId){ 
		this.ownerId = ownerId;
	}
	
	/**
	 * 	This method display all the purchsed coupon by type from coupon and customer_coupon tables
	 * @param CouponType couponType
	 * @return an ArrayList of Coupon 
	 */  
	@Override
	public ArrayList<Coupon> getAllPurchasedCouponsByType(CouponType couponType) throws CustomSqlSyntaxException {
		PreparedStatement pstmt = null;			
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Coupon> couponList = null;

		connection = connPoolInstance.getConnection();
		Coupon coupon = null;	// we claim a memory for the object but do not yet making an instance of the object
		
		try {
			pstmt = connection.prepareStatement(SQLCouponQueries.SELECT_PURCHASED_COUPONS_BY_TYPE_OWNED_BY_CUSTOMER);
			pstmt.setString (1, couponType.toString());
			pstmt.setLong(2, ownerId);
			rs = pstmt.executeQuery();
			
			System.out.println("nono: " + rs.getFetchSize());
			
			if(rs.next()){
				couponList = new ArrayList<>();
				do{
					coupon = new Coupon(); // if the result is not empty then we actually making the instance of the object
				
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
				} while(rs.next());
			}
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		} finally {
			connPoolInstance.returnConnection(connection);	
		}
		return couponList;	
	}

	/**
	 * 	This method display all the purchsed coupon by price from coupon and customer_coupon tables
	 * @return an ArrayList of Coupon 
	 */ 
	@Override
	public ArrayList<Coupon> getAllPurchasedCouponsByPrice(double price) throws CustomSqlSyntaxException {
		PreparedStatement pstmt = null;				
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Coupon> couponList = null;

		connection = connPoolInstance.getConnection();
		Coupon coupon = null;	
		
		try {
			pstmt = connection.prepareStatement(SQLCouponQueries.SELECT_PURCHASED_COUPONS_BY_PRICE_OWNED_BY_CUSTOMER);
			pstmt.setDouble (1,price);
			pstmt.setLong(2, ownerId);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				couponList = new ArrayList<>();
				do{
					coupon = new Coupon(); // if the result is not empty then we actually making the instance of the object
		
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
				} while(rs.next());
			}
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		} finally {	
			connPoolInstance.returnConnection(connection);		
		}
		return couponList;	
	}

	/**
	 * 	This method is a daily task which delete all expired coupons from the system every 24 hours
	 * @param java.util.Date currentDate is the parameter to deleteAllExpiredCoupon method
	 */
	public void deleteAllExpiredCoupon(java.util.Date currentDate) {
		Statement stmt = null;		
		Connection connection = null;
		
		java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
		
		String query1 = "delete from  Customer_Coupon where Coupon_ID in (select ID from coupon where END_date < '" + sqlDate + "')";
		String query2 = "delete from  Company_Coupon where Coupon_ID in (select ID from coupon where END_date < '" + sqlDate + "')";
		String query3 = "delete from  Coupon where END_date < '" + sqlDate + "'";

		connection = connPoolInstance.getConnection();
		
		try {			
			stmt = connection.createStatement();
			stmt.addBatch(query1);
			stmt.addBatch(query2);
			stmt.addBatch(query3);
			stmt.executeBatch();

		} catch (SQLException e) {
			e.getMessage();
		} finally {
			connPoolInstance.returnConnection(connection);		
		}
	}
}
