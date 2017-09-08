package exceptions;

public class CouponAmountIsEmptyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5745217499988014534L;

	
	public CouponAmountIsEmptyException(String message) {
		super(message);
	}
}
