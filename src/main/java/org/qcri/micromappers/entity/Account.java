package org.qcri.micromappers.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.qcri.micromappers.models.AccountDTO;
import org.qcri.micromappers.utility.RoleType;

@Entity
@Table(name="account")
public class Account extends ExtendedBaseEntity
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7979015238515155811L;
	@Column(name="user_name", nullable=false, unique=true)
	private String userName;
	
	@Column(name="locale", nullable=false)
	private String locale;
	
	@Column(name="api_key", nullable=false, unique=true)
	private String apiKey;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name="role")
	private RoleType role;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	private Boolean mailEnabled;

	public String getUserName()
	{
		return this.userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getLocale()
	{
		return this.locale;
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	public String getApiKey()
	{
		return this.apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	public RoleType getRole()
	{
		return this.role;
	}

	public void setRole(RoleType role)
	{
		this.role = role;
	}

	/**
	 * @return the mailEnabled
	 */
	public Boolean getMailEnabled() {
		return mailEnabled;
	}

	/**
	 * @param mailEnabled the mailEnabled to set
	 */
	public void setMailEnabled(Boolean mailEnabled) {
		this.mailEnabled = mailEnabled;
	}

	public AccountDTO toDTO(){
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setId(this.getId());
		accountDTO.setUserName(this.getUserName());
		accountDTO.setLocale(this.getLocale());
		return accountDTO;
	}
}
