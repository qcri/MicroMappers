package org.qcri.micromappers.utility;

public enum ResponseCode {
	SUCCESS("SUCCESS"),
	ERROR("ERROR"),
	WARNING("WARNING"),
	FAILED("FAILED");

	private ResponseCode(String code) {
		this.code = code;
	}

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static ResponseCode getByCode(String code){
		for(ResponseCode responseCode : ResponseCode.values() ){
			if(responseCode.getCode().equals(code)){
				return responseCode;
			}
		}
		return null;
	}
}
