package eu.toop.commons.exchange;

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;

/**
 * TOOP Request including attachments.
 *
 * @author Philip Helger
 * @since 0.10.2
 */
public class ToopRequestWithAttachments140 implements Serializable
{
  private final TDETOOPRequestType m_aRequest;
  private final ICommonsList <AsicReadEntry> m_aAttachments;

  public ToopRequestWithAttachments140 (@Nonnull final TDETOOPRequestType aRequest,
                                     @Nonnull final ICommonsList <AsicReadEntry> aAttachments)
  {
    ValueEnforcer.notNull (aRequest, "Request");
    ValueEnforcer.notNull (aAttachments, "Attachments");
    m_aRequest = aRequest;
    m_aAttachments = aAttachments.getClone ();
  }

  /**
   * @return The main TOOP Request. Never <code>null</code>.
   */
  @Nonnull
  public TDETOOPRequestType getRequest ()
  {
    return m_aRequest;
  }

  /**
   * @return A non-<code>null</code> but maybe empty list with all attachments
   *         of the ASiC container.
   */
  @Nonnull
  @ReturnsMutableObject
  public ICommonsList <AsicReadEntry> attachments ()
  {
    return m_aAttachments;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final ToopRequestWithAttachments140 rhs = (ToopRequestWithAttachments140) o;
    return m_aRequest.equals (rhs.m_aRequest) && m_aAttachments.equals (rhs.m_aAttachments);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aRequest).append (m_aAttachments).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Request", m_aRequest)
                                       .append ("Attachments", m_aAttachments)
                                       .getToString ();
  }
}
