/**
 * Copyright (C) 2018-2019 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.commons.exchange;

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;

/**
 * TOOP Response including attachments.
 *
 * @author Philip Helger
 * @since 0.10.2
 */
public class ToopResponseWithAttachments140 implements Serializable
{
  private final TDETOOPResponseType m_aResponse;
  private final ICommonsList <AsicReadEntry> m_aAttachments;

  public ToopResponseWithAttachments140 (@Nonnull final TDETOOPResponseType aResponse,
                                         @Nonnull final ICommonsList <AsicReadEntry> aAttachments)
  {
    ValueEnforcer.notNull (aResponse, "Response");
    ValueEnforcer.notNull (aAttachments, "Attachments");
    m_aResponse = aResponse;
    m_aAttachments = aAttachments.getClone ();
  }

  /**
   * @return The main TOOP Request. Never <code>null</code>.
   */
  @Nonnull
  public TDETOOPResponseType getResponse ()
  {
    return m_aResponse;
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
    final ToopResponseWithAttachments140 rhs = (ToopResponseWithAttachments140) o;
    return m_aResponse.equals (rhs.m_aResponse) && m_aAttachments.equals (rhs.m_aAttachments);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aResponse).append (m_aAttachments).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Response", m_aResponse)
                                       .append ("Attachments", m_aAttachments)
                                       .getToString ();
  }
}
