package org.qcri.micromappers.utility;

public enum ComputerVisionStatus {
	EXCEPTION_NOT_CORRECT_IMAGE_FORMAT("EXCEPTION_NOT_CORRECT_IMAGE_FORMAT"),
	EXCEPTION_NOT_VALID_IMAGE_URL("EXCEPTION_NOT_VALID_IMAGE_URL"),
	EXCEPTION_UNKNOWN("EXCEPTION_UNKNOWN"),
	COMPUTER_VISION_ANALYSIS_ACTIVE("COMPUTER_VISION_ANALYSIS_ACTIVE"),
	COMPUTER_VISION_ANALYSIS_INACTIVE("COMPUTER_VISION_ANALYSIS_INACTIVE"),
	COMPUTER_VISION_ANALYSER_TASK_ONGOING("ONGOING"),
	COMPUTER_VISION_ANALYSER_TASK_COMPLETED("COMPLETED"),
	OK("OK");

	private ComputerVisionStatus(String status) {
		this.status = status;
	}

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public static ComputerVisionStatus getByStatus(String status){
		for(ComputerVisionStatus collectionStatus : ComputerVisionStatus.values() ){
			if(collectionStatus.getStatus().equals(status)){
				return collectionStatus;
			}
		}
		return null;
	}
	
}
