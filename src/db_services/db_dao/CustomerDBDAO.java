package db_services.db_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import dataObjects.Coupon;
import dataObjects.CouponType;
import dataObjects.Customer;
import db_services.CustomerDAO;
import db_services.connection_pool.ConnectionPool;
import db_services.db_dao.queries.SQLCouponQueries;
import db_services.db_dao.queries.SQLCustomerQueries;
import db_services.db_dao.queries.SQLCustomer_CouponQueries;
import db_services.db_dao.sql_fields.SQLCouponFields;
import db_services.db_dao.sql_fields.SQLCustomerFields;
import exceptions.CouponAmountIsEmptyException;
import exceptions.CouponExpiredException;
import exceptions.CouponWasPurchasedException;
import exceptions.CustomSqlSyntaxException;
import exceptions.CustomerIdNotFoundException;
import exceptions.CustomerNameAlreadyExsitsException;
import exceptions.CustomerNameNotFound;
import exceptions.ExceptionStrings;
import exceptions.LoginFailedException;

public class CustomerDBDAO implements CustomerDAO{
	private ConnectionPool connPoolInstance = null;
	private long loggedInCustomerId;
	
	public CustomerDBDAO(){
		connPoolInstance = ConnectionPool.getInstance();
	}
	
	/**
	 * This method check if the customer name exists in database
	 * @param custName
	 * @return true if customer exists in table and false if does not exists
	 */
	private boolean custNameExists(String custName) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Connection connection = null;

		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCustomerQueries.SELECT_CUSTOMER_ID_BY_CUSTOMER_NAME);
			pstmt.setString(1, custName);
			result = pstmt.executeQuery();

			if(result.next()){
				connPoolInstance.returnConnection(connection);
				return true; // customer exists in table
			} 
		} finally {
				connPoolInstance.returnConnection(connection);
		} 
		return false;
	} 
	
	/**
	 * This method insert new customer to the table if he is not in the database
	 *@param customer
	 *@see CustNameExists {@link #CustNameExists}
	 *@throws CustomerNameAlreadyExsitsException
	 *@throws CustomSqlSyntaxException
	 */
	@Override
	public void createCustomer(Customer customer) throws CustomerNameAlreadyExsitsException, CustomSqlSyntaxException {
		PreparedStatement pstmt = null;	
		Connection connection = null;
		
		String name = customer.getCustName();
		String password = customer.getPassword();
			
		connection = connPoolInstance.getConnection();
		
		try {
			if(!custNameExists(customer.getCustName())) {
				try {
					pstmt = connection.prepareStatement(SQLCustomerQueries.INSERT_NEW_CUSTOMER);
					pstmt.setString(1, name);
					pstmt.setString(2, password);
					pstmt.executeUpdate(); 
				} finally {
					connPoolInstance.returnConnection(connection);
				} 
			} else 
				throw new CustomerNameAlreadyExsitsException(ExceptionStrings.CUST_NAME_ALREADY_EXSITS);
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
	}
		
	/**
	 * This method delete an existing customer from the database
	 * @param customer
	 *@see CustNameExists {@link #CustNameExists}
	 *@throws CustomerNameNotFound
	 *@throws CustomSqlSyntaxException
	 */
	@Override
	public void removeCustomer(Customer customer) throws CustomerNameNotFound, CustomSqlSyntaxException {
		long id = customer.getId();
		
		try {
			if(custNameExists(customer.getCustName())) {
				removeCustomerFromCustomerTbl(id);
				removeCustomerFromCustomerCouponTbl(id);
			} else 
				throw new CustomerNameNotFound (ExceptionStrings.CUST_NAME_NOT_FOUND);
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
	}
	/**
	 * This private method removes customer from the Customer table
	 * @param id
	 * @throws SQLException
	 */
	private void removeCustomerFromCustomerTbl(Long id) throws SQLException {
		PreparedStatement pstmt = null;		
		Connection connection = null;

		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCustomerQueries.DELETE_CUSTOMER_FROM_CUSTOMER_TABLE);
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
		} finally {
			connPoolInstance.returnConnection(connection);
		}
	}
	/**
	 * This private method removes customer from the CustomerCoupon table	
	 * @param id
	 * @throws SQLException
	 */
	private void removeCustomerFromCustomerCouponTbl(Long id) throws SQLException{
		PreparedStatement pstmt = null;		
		Connection connection = null;
		
		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCustomer_CouponQueries.DELETE_CUSTOMER_FROM_CUSTOMER_COUPON_TABLE);
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
		} finally {
			connPoolInstance.returnConnection(connection);
		}
	}

	/**
	 * This method updates the password of an existing Customer
	 * @param customer
	 *@see CustNameExists {@link #CustNameExists}
	 *@throws CustomerNameNotFound
	 *@throws CustomSqlSyntaxException
	 */
	@Override
	public void updateCustomer(Customer customer) throws CustomerNameNotFound, CustomSqlSyntaxException {
		PreparedStatement pstmt = null;				
		Connection connection = null;
		
		try {
			if (custNameExists(customer.getCustName())) {
				long id = customer.getId();
				String password = customer.getPassword();
	
				connection = connPoolInstance.getConnection();
	
				try {
					pstmt = connection.prepareStatement(SQLCustomerQueries.UPDATE_CUSTOMER);
					pstmt.setString(1, password);
					pstmt.setLong(2, id);		
					pstmt.executeUpdate();
				} finally {
					connPoolInstance.returnConnection(connection);
				}
			} else throw new CustomerNameNotFound(ExceptionStrings.CUST_NAME_NOT_FOUND);
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
	}

	/**
	 * This methos display a specific existing customer by id
	 * @param id
	 * @throws CustomerIdNotFoundException
	 * @throws CustomSqlSyntaxException
	 */
	@Override
	public Customer getCustomer(long id) throws CustomerIdNotFoundException, CustomSqlSyntaxException {
		PreparedStatement pstmt = null;			
		Connection connection = null;
		ResultSet rs = null;
		
		connection = connPoolInstance.getConnection();
		Customer customer = null;
		
		try {
			pstmt = connection.prepareStatement(SQLCustomerQueries.SELECT_CUSTOMER_BY_ID);
			pstmt.setLong(1,id);
			rs = pstmt.executeQuery();
	
			if(rs.next()){
				customer = new Customer(); // if the result is not empty then we actually making the instance of the object
				
				customer.setId(rs.getLong(SQLCustomerFields.CUSTOMER_ID));
				customer.setCustName(rs.getString(SQLCustomerFields.CUSTOMER_NAME).trim());
				customer.setPassword(rs.getString(SQLCustomerFields.CUSTOMER_PASSWORD).trim());
				
				//the goal is to extract the last piece of the CUSTOMER object (Array list of coupons)
				// 1. get all the ids of coupons that belong to some customer from join table
				// 2. get all the objects-coupons from coupons table by the ids that we got from join table in step 1
				ArrayList<Long> couponIdListRs = getAllCouponIdsOwnedByCustomer(customer.getId()); 
				ArrayList<Coupon> couponListRs = getAllCouponsOwnedByCustomer(couponIdListRs);
				
				customer.setCoupons(couponListRs); 		
			} else throw new CustomerIdNotFoundException(ExceptionStrings.CUST_ID_NOT_FOUND);
	
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		} finally {
			connPoolInstance.returnConnection(connection);
		}
		return customer;
	}

	/**
	 * This method display all exiting customers in the table
	 * @return ArrayList<Customer> custList
	 */
	@Override
	public ArrayList<Customer> getAllCustomer() throws CustomSqlSyntaxException {
		Statement stmt = null;		
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Customer> custList = null;
		
		connection = connPoolInstance.getConnection(); 

		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(SQLCustomerQueries.SELECT_ALL_CUSTOMERS);
				
			if(rs.next()){	// checking if result that we got from database is not empty
				custList = new ArrayList<>();
				do{
					Customer customer = new Customer();
					customer.setId(rs.getLong(SQLCustomerFields.CUSTOMER_ID));
					customer.setCustName(rs.getString(SQLCustomerFields.CUSTOMER_NAME).trim());
					customer.setPassword(rs.getString(SQLCustomerFields.CUSTOMER_PASSWORD).trim());
					
					ArrayList<Long> couponIdListRs = getAllCouponIdsOwnedByCustomer(customer.getId()); 
					ArrayList<Coupon> couponListRs = getAllCouponsOwnedByCustomer(couponIdListRs);
					
					customer.setCoupons(couponListRs);
							
					custList.add(customer);
				} while(rs.next());
			}
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		} finally {
			connPoolInstance.returnConnection(connection);
		}
		return custList;
	}

	/**
	 * This private method display the id number of coupons that belong to the CustomerId given
	 * @param customerId
	 * @return ArrayList<Long> couponsIdList
	 * @throws SQLException
	 */
	private ArrayList<Long> getAllCouponIdsOwnedByCustomer(long customerId) throws SQLException {
		PreparedStatement pstmt = null;			
		Connection connection = null;
		ResultSet rs = null;
		ArrayList<Long> couponsIdList = null;
				
		connection = connPoolInstance.getConnection();

		try {
			pstmt = connection.prepareStatement(SQLCustomer_CouponQueries.SELECT_COUPONSIDS_BY_CUSTOMER_ID);
			pstmt.setLong(1,customerId);
			rs = pstmt.executeQuery();

			
			if(rs.next()){	// checking if result that we got from database is not empty
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
	 * This private method display the coupons that belong to the CustomerId given
	 * @param ArrayList<Long> couponsIdList
	 * @return ArrayList<Coupon> couponList
	 * @throws SQLException
	 */
	private ArrayList<Coupon> getAllCouponsOwnedByCustomer(ArrayList<Long> couponsIdList) throws SQLException {  // שולפי את הקופונים עצמם האובייקטים מטבלת קופונים ומאכלסים בקולקשיין
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
						continue; //continues into net iteration in for loop
					}		
				} finally {
					connPoolInstance.returnConnection(connection);	
				}	
			} 
		}
		return couponList;
	}
	
	/**
	 * This method checks the conditions to purchase coupon and if 
	 * the conditions are fullfield then it updates the relevant tables in the database accordingly
	 * @param coupon
	 * @see ifAmountIsEmpty {@link#ifAmountIsEmpty(coupon)}
	 * @see ifCouponDateExpired {@link #ifCouponDateExpired(Coupon)}
	 * @see ifCouponWasPurchsed {@link #ifCouponWasPurchsed(coupon)}
	 * @throws CouponAmountIsEmptyException
	 * @throws CouponExpiredException
	 * @throws CouponWasPurchasedException
	 * @throws CustomSqlSyntaxException 
	 */
	@Override
	public void purchaseCoupon(Coupon coupon) throws CouponAmountIsEmptyException, CouponExpiredException, CouponWasPurchasedException, CustomSqlSyntaxException {
		long id = coupon.getId();
	
		try{
			if (!ifAmountIsEmpty(coupon) && !ifCouponDateExpired(coupon) && !ifCouponWasPurchsed(coupon)){
				insertPurchasedCouponIntoTable(id);
				updateAmountInCouponTable(id);
			}
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
	}
	
	/**
	 * This private method insert the purchased coupon into the table
	 * @param Long id
	 * @throws SQLException
	 */
	private void insertPurchasedCouponIntoTable (Long id) throws SQLException {
		PreparedStatement pstmt = null;		
		Connection connection = null;
		
		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCustomer_CouponQueries.INSERT_PURCHASED_COUPONS);
			pstmt.setLong(1, loggedInCustomerId);
			pstmt.setLong(2, id);
			pstmt.executeUpdate();
		} finally {
			connPoolInstance.returnConnection(connection);
		}
	}
		
	/**
	 * This private method updates the current amount of coupons left after the purchasing
	 * @param Long id
	 * @throws SQLException
	 */
	private void updateAmountInCouponTable(Long id) throws SQLException {
		PreparedStatement pstmt = null;		
		Connection connection = null;
		
		connection = connPoolInstance.getConnection();

		try {
			pstmt = connection.prepareStatement(SQLCouponQueries.UPDATE_CURRENT_AMOUNT_IN_COUPON_TABLE);
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
		} finally {
			connPoolInstance.returnConnection(connection);
		}
	}

	/**
	 * This method checks the condition that needs to be checked before purchesing
	 *  a coupon - will be checked inside the purchaseCoupon function
	 * @param Coupon coupon
	 * @return boolean
	 * @throws CouponAmountIsEmptyException
	 */
	public boolean ifAmountIsEmpty(Coupon coupon) throws CouponAmountIsEmptyException{
		boolean status = false;
		if (coupon.getAmount() == 0){
			status = true;
			throw new CouponAmountIsEmptyException(ExceptionStrings.COUPON_AMOUNT_IS_EMPTY);
			
		} else status = false;
		return status;
	}
	
	/**
	 * This method checks the condition that needs to be checked before purchesing
	 *  a coupon - will be checked inside the purchaseCoupon function
	 * @param Coupon coupon
	 * @return boolean
	 * @throws CouponAmountIsEmptyException
	 */
	public boolean ifCouponDateExpired(Coupon coupon) throws CouponExpiredException{
		boolean status = false;
		Date currentDate = new Date();
		java.util.Date couponEndDate = new Date(coupon.getEndDate().getTime());
		
		if (currentDate.compareTo(couponEndDate) > 0) {
			status = true; // the coupon is expired
			throw new CouponExpiredException(ExceptionStrings.COUPON_EXPIRED);
		} else if (currentDate.compareTo(couponEndDate) < 0) {
			status = false; // the coupon is not expired
		} else if( currentDate.compareTo(couponEndDate) == 0) {
			status = false;	// the last date of coupons life
		}
		
		return status;
	}
	
	/**
	 * This method checks the condition that needs to be checked before purchesing
	 *  a coupon - will be checked inside the purchaseCoupon function
	 * @param Coupon coupon
	 * @return boolean
	 * @throws CouponAmountIsEmptyException
	 */
	public boolean ifCouponWasPurchsed(Coupon coupon) throws CouponWasPurchasedException, SQLException{
		boolean status = false;
		PreparedStatement pstmt = null;		
		Connection connection = null;
		ResultSet rs = null;
		Long id = coupon.getId();

		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCustomer_CouponQueries.SELECT_PURCHASED_COUPONS);
			pstmt.setLong(1,id);
			pstmt.setLong(2,loggedInCustomerId);
			rs = pstmt.executeQuery();
								
			if(rs.next()){
				connPoolInstance.returnConnection(connection);
				status = true;
				throw new CouponWasPurchasedException(ExceptionStrings.COUPON_WAS_PURCHASED_BY_CUSTOMER);
			}
		} finally {
			connPoolInstance.returnConnection(connection);
		}
		return status;
	}
	
	/**
	 * This method display all the purchased coupon list by the customer user
	 * @return ArrayList<Coupon> purchasedCouponslist
	 * @throws CustomSqlSyntaxException
	 */
	@Override
	public ArrayList<Coupon> getAllPurchasedCoupons() throws CustomSqlSyntaxException {
		ArrayList<Coupon> purchasedCouponslist = null;
		
		try {
			ArrayList<Long> purchasedidsList = getAllCouponIdsOwnedByCustomer(loggedInCustomerId);
			purchasedCouponslist = getAllCouponsOwnedByCustomer(purchasedidsList);
		} catch (SQLException e) {
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage());
		}
		 
		return purchasedCouponslist;
	}	

	/**
	 * This method checks if the customer exists in database and return a boolean response and 
	 * throws 2 kinds of Exception, one is logic and the other is sqlSyntaxError Exception
	 * @param custName
	 * @param password
	 * @return boolean loginStatus
	 * @throws CustomSqlSyntaxException
	 * @throws LoginFailedException
	 */
	@Override
	public boolean login(String custName, String password) throws CustomSqlSyntaxException, LoginFailedException {
		PreparedStatement pstmt = null;	
	    ResultSet rs = null; 
	    Connection connection = null;
	    boolean loginStatus = false;
	    	
		connection = connPoolInstance.getConnection();
		
		try {
			pstmt = connection.prepareStatement(SQLCustomerQueries.CUSTOMER_LOGIN_CHECK);
			pstmt.setString(1,custName);
			pstmt.setString(2,password);
			rs = pstmt.executeQuery();
						
			if (rs.next()) {
				loggedInCustomerId = rs.getLong(SQLCustomerFields.CUSTOMER_ID);
				connPoolInstance.returnConnection(connection);
				loginStatus = true;		
			} else 
				throw new LoginFailedException(ExceptionStrings.LOGIN_FAILED);	// throw logic exception
		} catch(SQLException e){
			throw new CustomSqlSyntaxException(ExceptionStrings.SQL_SYNTAX_ERROR + e.getMessage()); // thow custom sql exception
		} finally {
			connPoolInstance.returnConnection(connection);
		} 
		
		return loginStatus;
	}
	
	public long getLoggedInCustomerId() {
		return loggedInCustomerId;
	}
}
