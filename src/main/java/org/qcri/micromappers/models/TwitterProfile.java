package org.qcri.micromappers.models;

import java.io.Serializable;

public class TwitterProfile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9061444052309716511L;
	private String id;
	private String name;
	private String screenName;
	private Integer followersCount;
	private Integer friendsCount;
	private String imageURL;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return screenName;
	}
	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	/**
	 * @return the followersCount
	 */
	public Integer getFollowersCount() {
		return followersCount;
	}
	/**
	 * @param followersCount the followersCount to set
	 */
	public void setFollowersCount(Integer followersCount) {
		this.followersCount = followersCount;
	}
	/**
	 * @return the friendsCount
	 */
	public Integer getFriendsCount() {
		return friendsCount;
	}
	/**
	 * @param friendsCount the friendsCount to set
	 */
	public void setFriendsCount(Integer friendsCount) {
		this.friendsCount = friendsCount;
	}
	/**
	 * @return the imageURL
	 */
	public String getImageURL() {
		return imageURL;
	}
	/**
	 * @param imageURL the imageURL to set
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
}
