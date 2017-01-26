package org.qcri.micromappers.models;

import java.io.Serializable;

import org.qcri.micromappers.utility.FacebookEntityType;

public class FacebookProfile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1791261352750702140L;
	
	private String id;
	private String link;
	private Integer fans;
	private String name;
	private String imageUrl;
	private FacebookEntityType type;
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
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return the fans
	 */
	public Integer getFans() {
		return fans;
	}
	/**
	 * @param fans the fans to set
	 */
	public void setFans(Integer fans) {
		this.fans = fans;
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
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the type
	 */
	public FacebookEntityType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(FacebookEntityType type) {
		this.type = type;
	}
}
