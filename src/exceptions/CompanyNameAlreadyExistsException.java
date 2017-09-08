package exceptions;

public class CompanyNameAlreadyExistsException extends Exception { //Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3676046985653509001L;
	
	public CompanyNameAlreadyExistsException(String message) {
		super(message);
	}

}
