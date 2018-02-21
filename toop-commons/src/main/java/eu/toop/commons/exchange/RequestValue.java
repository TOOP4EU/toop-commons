package eu.toop.commons.exchange;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

@Immutable
public final class RequestValue implements Serializable {
  private final String m_sKey;
  private final String m_sValue;

  public RequestValue (@Nonnull @Nonempty final String sKey, @Nullable final String sValue) {
    m_sKey = ValueEnforcer.notEmpty (sKey, "Key");
    m_sValue = sValue;
  }

  @Nonnull
  @Nonempty
  public String getKey () {
    return m_sKey;
  }

  @Nullable
  public String getValue () {
    return m_sValue;
  }

  @Override
  public boolean equals (final Object o) {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final RequestValue rhs = (RequestValue) o;
    return m_sKey.equals (rhs.m_sKey) && EqualsHelper.equals (m_sValue, rhs.m_sValue);
  }

  @Override
  public int hashCode () {
    return new HashCodeGenerator (this).append (m_sKey).append (m_sValue).getHashCode ();
  }

  @Override
  public String toString () {
    return new ToStringGenerator (this).append ("Key", m_sKey).append ("Value", m_sValue).getToString ();
  }
}