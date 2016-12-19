package org.qcri.micromappers.utility;

public enum CollectionStatus {
	RUNNING("RUNNING"),
	FATAL_ERROR("FATAL_ERROR"),
	RUNNING_WARNING("RUNNING_WARNING"),
	NOT_RUNNING("NOT_RUNNING"),
	TRASHED("TRASHED"),
	WARNING("WARNING"),
	EXCEPTION("EXCEPTION");

	private CollectionStatus(String status) {
		this.status = status;
	}

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public static CollectionStatus getByStatus(String status){
		for(CollectionStatus collectionStatus : CollectionStatus.values() ){
			if(collectionStatus.getStatus().equals(status)){
				return collectionStatus;
			}
		}
		return null;
	}
	
}
