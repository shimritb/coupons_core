package exceptions;

import java.sql.SQLException;

public class CustomSqlSyntaxException extends SQLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4982924849238701688L;
	
	public CustomSqlSyntaxException(String message) {
		super(message);
	}
}
