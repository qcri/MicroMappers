/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.qcri.micromappers.utility;

import java.io.Serializable;

/**
 * @author Kushal
 * This class is used to send response to the clients. 
 */
public class ResponseWrapper implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2144284030642138755L;
	
	protected String statusCode;
	protected String message;
	private Object data;
	private Boolean success;
	
	public ResponseWrapper(Object data, Boolean success, String statusCode, String message) {
		this.setData(data);
		this.setSuccess(success);
		this.setStatusCode(statusCode);
		this.setMessage(getMessage(success, message));
	}

    private static String getMessage(Boolean success, String message){
        if (message != null){
            return message;
        }
        if (success != null && success){
            return "Successful";
        } else {
            return "Failure";
        }
    }
	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
