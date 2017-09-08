package exceptions;

public class CouponNameAlreadyExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1574325212238591455L;
	
	public CouponNameAlreadyExistsException(String message) {
		super(message);
	}

}
