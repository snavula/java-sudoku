package com.sneha.jsudoku;

public class InvalidValueException extends Exception {

	private static final long serialVersionUID = 80574016974886531L;

	public InvalidValueException() {
		super();
	}

	public InvalidValueException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidValueException(String message) {
		super(message);
	}

	public InvalidValueException(Throwable cause) {
		super(cause);
	}

}
