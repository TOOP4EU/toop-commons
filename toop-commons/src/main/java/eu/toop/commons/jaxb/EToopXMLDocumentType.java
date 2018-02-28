/**
 * Copyright (C) 2015-2017 difi (www.difi.no)
 * Copyright (C) 2018 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * This Source Code Form is subject to the terms of the
 * Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/
 */
package eu.toop.commons.jaxb;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.validation.Schema;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.string.StringHelper;
import com.helger.jaxb.builder.IJAXBDocumentType;
import com.helger.jaxb.builder.JAXBDocumentType;

import eu.toop.commons.dataexchange.TDETOOPDataRequestType;
import eu.toop.commons.dataexchange.TDETOOPDataResponseType;

/**
 * Enumeration with all available TOOP XML document types.
 *
 * @author Philip Helger
 */
public enum EToopXMLDocumentType implements IJAXBDocumentType {
  DATA_REQUEST (TDETOOPDataRequestType.class, "/xsd/toop/TOOP_DataExchange.xsd"),
  DATA_RESPONSE (TDETOOPDataResponseType.class, "/xsd/toop/TOOP_DataExchange.xsd");

  private final JAXBDocumentType m_aDocType;

  private EToopXMLDocumentType (@Nonnull final Class<?> aClass, @Nonnull final String sXSDPath) {
    m_aDocType = new JAXBDocumentType (aClass, new CommonsArrayList<> (sXSDPath),
                                       x -> StringHelper.trimEnd (x, "Type"));
  }

  @Nonnull
  public Class<?> getImplementationClass () {
    return m_aDocType.getImplementationClass ();
  }

  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public ICommonsList<String> getAllXSDPaths () {
    return m_aDocType.getAllXSDPaths ();
  }

  @Nonnull
  public String getNamespaceURI () {
    return m_aDocType.getNamespaceURI ();
  }

  @Nonnull
  @Nonempty
  public String getLocalName () {
    return m_aDocType.getLocalName ();
  }

  @Nonnull
  public Schema getSchema (@Nullable final ClassLoader aClassLoader) {
    return m_aDocType.getSchema (aClassLoader);
  }
}
