package exceptions;

public class LoginFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7997249421758962152L;

	public LoginFailedException(String message) {
		super(message);
	}
}
