package org.qcri.micromappers.entity;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonObject;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

public class JSONObjectUserType implements UserType
{
  public Object assemble(Serializable cached, Object owner)
    throws HibernateException
  {
    return deepCopy(cached);
  }
  
  public Object deepCopy(Object value)
    throws HibernateException
  {
    return value;
  }
  
  public Serializable disassemble(Object value)
    throws HibernateException
  {
    return (String)deepCopy(value);
  }
  
  public boolean equals(Object x, Object y)
    throws HibernateException
  {
    if (x == null) {
      return y == null;
    }
    return x.equals(y);
  }
  
  public int hashCode(Object x)
    throws HibernateException
  {
    return x.hashCode();
  }
  
  public boolean isMutable()
  {
    return true;
  }
  
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
    throws HibernateException, SQLException
  {
    if (rs.getString(names[0]) != null)
    {
      JsonObject jsonObject = Json.createReader(new StringReader(rs.getString(names[0]))).readObject();
      return jsonObject;
    }
    return null;
  }
  
  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
    throws HibernateException, SQLException
  {
    if (value == null)
    {
      st.setNull(index, 1111);
      return;
    }
    st.setObject(index, value, 1111);
  }
  
  public Object replace(Object original, Object target, Object owner)
    throws HibernateException
  {
    return original;
  }
  
  public Class returnedClass()
  {
    return String.class;
  }
  
  public int[] sqlTypes()
  {
    return new int[] { 2000 };
  }
}
