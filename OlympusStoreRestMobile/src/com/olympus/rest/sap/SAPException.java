package com.olympus.rest.sap;

public class SAPException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SAPException() {}

	public SAPException(String message) {
		super(message);
	}

	public SAPException(Throwable cause) {
		super(cause);
	}

	public SAPException(String message, Throwable cause) {
		super(message, cause);
	}

	public SAPException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
