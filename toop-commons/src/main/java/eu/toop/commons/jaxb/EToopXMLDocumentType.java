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
package eu.toop.commons.jaxb;

import javax.annotation.Nonnull;
import javax.xml.validation.Schema;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.string.StringHelper;
import com.helger.jaxb.builder.IJAXBDocumentType;
import com.helger.jaxb.builder.JAXBDocumentType;

import eu.toop.commons.exchange.ToopMessageBuilder120;
import eu.toop.commons.exchange.ToopMessageBuilder140;

/**
 * Enumeration with all available TOOP XML document types.
 *
 * @author Philip Helger
 */
public enum EToopXMLDocumentType implements IJAXBDocumentType
{
  REQUEST_120 (eu.toop.commons.dataexchange.v120.TDETOOPRequestType.class, ToopMessageBuilder120.TOOP_XSD),
  REQUEST_140 (eu.toop.commons.dataexchange.v140.TDETOOPRequestType.class, ToopMessageBuilder140.TOOP_XSD),
  RESPONSE_120 (eu.toop.commons.dataexchange.v120.TDETOOPResponseType.class, ToopMessageBuilder120.TOOP_XSD),
  RESPONSE_140 (eu.toop.commons.dataexchange.v140.TDETOOPResponseType.class, ToopMessageBuilder140.TOOP_XSD);

  private final JAXBDocumentType m_aDocType;

  private EToopXMLDocumentType (@Nonnull final Class <?> aClass, @Nonnull final ClassPathResource aXSD)
  {
    m_aDocType = new JAXBDocumentType (aClass,
                                       new CommonsArrayList <> (aXSD),
                                       x -> StringHelper.trimEnd (StringHelper.trimStart (x, "TOOP"), "Type"));
  }

  @Nonnull
  public Class <?> getImplementationClass ()
  {
    return m_aDocType.getImplementationClass ();
  }

  @Nonnull
  @Nonempty
  @ReturnsMutableCopy
  public ICommonsList <ClassPathResource> getAllXSDResources ()
  {
    return m_aDocType.getAllXSDResources ();
  }

  @Nonnull
  public String getNamespaceURI ()
  {
    return m_aDocType.getNamespaceURI ();
  }

  @Nonnull
  @Nonempty
  public String getLocalName ()
  {
    return m_aDocType.getLocalName ();
  }

  @Nonnull
  public Schema getSchema ()
  {
    return m_aDocType.getSchema ();
  }
}
