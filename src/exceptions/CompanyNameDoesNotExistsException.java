package exceptions;

public class CompanyNameDoesNotExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7501309829496172594L;

	public CompanyNameDoesNotExistsException (String message){
		super(message);
	}
}
