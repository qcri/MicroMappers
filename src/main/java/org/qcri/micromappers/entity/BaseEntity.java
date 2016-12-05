package org.qcri.micromappers.entity;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity
  implements Serializable
{
  private static final long serialVersionUID = 5714436358953848715L;
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  
  public Long getId()
  {
    return this.id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
}
