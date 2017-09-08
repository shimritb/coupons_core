package exceptions;

public class CustomerNameNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1099480710677239774L;

	public CustomerNameNotFound (String message){
		super(message);
	}
}