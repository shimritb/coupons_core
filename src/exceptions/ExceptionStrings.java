package exceptions;

public class ExceptionStrings {
	
	public static final String SQL_SYNTAX_ERROR = "ERROR: syntax error within sql\n";
	public static final String COMP_NAME_ALREADY_EXSITS = "ERROR: company name is already exists";
	public static final String COMP_NAME_DOES_NOT_EXSITS = "ERROR: company name does not exsits";
	public static final String COMP_ID_NOT_FOUND = "ERROR: company id was not found";
	public static final String CUST_NAME_ALREADY_EXSITS = "ERROR: customer name is already exists";
	public static final String CUST_NAME_NOT_FOUND = "ERROR: customer name was not found";
	public static final String CUST_ID_NOT_FOUND = "ERROR: customer id was not found";
	public static final String COUPON_NAME_ALREADY_EXSITS = "ERROR: coupon name already exsits";
	public static final String COUPON_NAME_NOT_FOUND = "ERROR: coupon name not found";
	public static final String COUPON_NOT_BELONG_TO_COMPANY ="ERROR: the coupon doesnt belong to company";
	public static final String LOGIN_FAILED = "ERROR: failed to loging with given credentials";
	public static final String COUPON_AMOUNT_IS_EMPTY = "ERROR: no more coupons left";
	public static final String COUPON_EXPIRED = "ERROR: the requested coupon is expired";
	public static final String COUPON_WAS_PURCHASED_BY_CUSTOMER = "ERROR: coupon was already purchased by the user";
	
}
