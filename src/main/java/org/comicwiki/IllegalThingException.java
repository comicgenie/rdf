package org.comicwiki;

public class IllegalThingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1574306965578130260L;

	public IllegalThingException() {
		super();
	}

	public IllegalThingException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalThingException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalThingException(String message) {
		super(message);
	}

	public IllegalThingException(Throwable cause) {
		super(cause);
	}

}
