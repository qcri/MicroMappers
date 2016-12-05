package org.qcri.micromappers.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.qcri.micromappers.utility.RoleType;

@Entity
@Table(name="account")
public class Account
  extends ExtendedBaseEntity
{
  private static final long serialVersionUID = -881973526366597368L;
  @Column(name="user_name", nullable=false, unique=true)
  private String userName;
  @Column(name="locale", nullable=false)
  private String locale;
  @Column(name="api_key", nullable=false, unique=true)
  private String apiKey;
  @Enumerated(EnumType.ORDINAL)
  @Column(name="role")
  private RoleType role;
  
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
}
