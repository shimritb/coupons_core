package exceptions;

public class CustomerNameAlreadyExsitsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5030488127099892540L;

	public CustomerNameAlreadyExsitsException (String message){
		super(message);
	}
}
