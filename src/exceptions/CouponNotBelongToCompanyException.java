package exceptions;

public class CouponNotBelongToCompanyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4777826298999343070L;
	

	public CouponNotBelongToCompanyException (String message){
		super(message);
	}
}
