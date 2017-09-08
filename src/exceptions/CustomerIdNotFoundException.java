package exceptions;

public class CustomerIdNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4284498553239917975L;

	public CustomerIdNotFoundException (String message){
		super(message);
	}
}