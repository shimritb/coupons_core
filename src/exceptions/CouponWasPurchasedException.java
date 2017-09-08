package exceptions;

public class CouponWasPurchasedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5878457702059008626L;
	
	public CouponWasPurchasedException (String message){
		super(message);
	}
}
