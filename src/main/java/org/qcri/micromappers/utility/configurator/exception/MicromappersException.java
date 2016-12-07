package org.qcri.micromappers.utility.configurator.exception;

/**
 * An Micromappers-specific exception
 *
 */
public abstract class MicromappersException extends Exception {
    
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -739288302986762394L;

	/**
     * Creates an exception
     */
    public MicromappersException() {
        super();
    }
 
    /**
     * Creates an exception with a given message.
     * 
     * @param message the message to include in the exception
     */
    public MicromappersException(String message) {
        super("[MicromappersException] " + message);
    }
 
    /**
     * Creates an exception with a given cause
     * 
     * @param cause the cause
     */
    public MicromappersException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an exception with a given message and cause
     * 
     * @param message the message
     * @param cause the cause
     */
    public MicromappersException(String message, Throwable cause) {
    	super("[MicromappersException] " + message, cause);
    }
}
