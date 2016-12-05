package org.qcri.micromappers.utility;

public enum CollectionType
{
  TWITTER("twitter", "tweet", "tweets"),  FACEBOOK("facebook", "post", "posts");
  
  private String value;
  private String singular;
  private String plural;
  
  private CollectionType(String value, String singular, String plural)
  {
    setValue(value);
    this.singular = singular;
    this.plural = plural;
  }
  
  public String getSingular()
  {
    return this.singular;
  }
  
  public String getPlural()
  {
    return this.plural;
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public void setValue(String value)
  {
    this.value = value;
  }
}
