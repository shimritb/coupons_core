package exceptions;

public class CouponExpiredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -543242908857838980L;

	public CouponExpiredException(String message) {
		super(message);
	}
}
