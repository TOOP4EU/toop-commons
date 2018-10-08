/**
 * Copyright (C) 2018 toop.eu
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

import eu.toop.commons.dataexchange.TDETOOPErrorMessageType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import eu.toop.commons.dataexchange.TDETOOPResponseType;

/**
 * Enumeration with all available TOOP XML document types.
 *
 * @author Philip Helger
 */
public enum EToopXMLDocumentType implements IJAXBDocumentType {
  REQUEST (TDETOOPRequestType.class, "/xsd/toop/TOOP_DataExchange-1.2.0.xsd"),
  RESPONSE (TDETOOPResponseType.class, "/xsd/toop/TOOP_DataExchange-1.2.0.xsd"),
  ERROR_MESSAGE (TDETOOPErrorMessageType.class, "/xsd/toop/TOOP_DataExchange-1.2.0.xsd");

  private final JAXBDocumentType m_aDocType;

  private EToopXMLDocumentType (@Nonnull final Class<?> aClass, @Nonnull final String sXSDPath) {
    m_aDocType = new JAXBDocumentType (aClass, new CommonsArrayList<> (sXSDPath),
                                       x -> StringHelper.trimEnd (StringHelper.trimStart (x, "TOOP"), "Type"));
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
