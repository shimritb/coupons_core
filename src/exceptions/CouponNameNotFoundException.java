package exceptions;

public class CouponNameNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4494670636395700497L;

	public CouponNameNotFoundException (String message){
		super(message);
	}
}
