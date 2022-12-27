package br.com.hsneves.certi.test.exceptions;

/**
 * 
 * @author Henrique Neves
 *
 */
public class CertiTestRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -6469562702204362829L;

	public CertiTestRuntimeException() {
		super();
	}

	public CertiTestRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CertiTestRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CertiTestRuntimeException(String message) {
		super(message);
	}

	public CertiTestRuntimeException(Throwable cause) {
		super(cause);
	}
	
}
