package org.qcri.micromappers.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UserConnection")
public class UserConnection implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4735169866806074545L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="userId")
	private String userId;

	@Column(name="providerId")
	private String providerId;

	@Column(name="providerUserId")
	private String providerUserId;

	@Column(name="rank")
	private int rank;

	@Column(name="displayName")
	private String displayName;

	@Column(name="profileUrl")
	private String profileUrl;

	@Column(name="imageUrl")
	private String imageUrl;

	@Column(name="accessToken", length=1024)
	private String accessToken;

	@Column(name="secret")
	private String secret;

	@Column(name="refreshToken")
	private String refreshToken;

	@Column(name="expireTime")
	private Long expireTime;

	public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getUserId()
	{
		return this.userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getProviderId()
	{
		return this.providerId;
	}

	public void setProviderId(String providerId)
	{
		this.providerId = providerId;
	}

	public String getProviderUserId()
	{
		return this.providerUserId;
	}

	public void setProviderUserId(String providerUserId)
	{
		this.providerUserId = providerUserId;
	}

	public int getRank()
	{
		return this.rank;
	}

	public void setRank(int rank)
	{
		this.rank = rank;
	}

	public String getDisplayName()
	{
		return this.displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getProfileUrl()
	{
		return this.profileUrl;
	}

	public void setProfileUrl(String profileUrl)
	{
		this.profileUrl = profileUrl;
	}

	public String getImageUrl()
	{
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public String getAccessToken()
	{
		return this.accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}

	public String getSecret()
	{
		return this.secret;
	}

	public void setSecret(String secret)
	{
		this.secret = secret;
	}

	public String getRefreshToken()
	{
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken)
	{
		this.refreshToken = refreshToken;
	}

	public Long getExpireTime()
	{
		return this.expireTime;
	}

	public void setExpireTime(Long expireTime)
	{
		this.expireTime = expireTime;
	}
}
