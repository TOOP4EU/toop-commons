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

import com.helger.jaxb.builder.JAXBWriterBuilder;

public class ToopWriter <JAXBTYPE> extends JAXBWriterBuilder <JAXBTYPE, ToopWriter <JAXBTYPE>>
{
  /**
   * Constructor with an arbitrary document type.
   *
   * @param aDocType Document type to be used. May not be <code>null</code>.
   */
  public ToopWriter (@Nonnull final EToopXMLDocumentType aDocType)
  {
    super (aDocType);
  }

  /**
   * Create a writer builder for
   * {@link eu.toop.commons.dataexchange.v120.TDETOOPRequestType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopWriter <eu.toop.commons.dataexchange.v120.TDETOOPRequestType> request120 ()
  {
    final ToopWriter <eu.toop.commons.dataexchange.v120.TDETOOPRequestType> ret = new ToopWriter <> (EToopXMLDocumentType.REQUEST_120);
    ret.setFormattedOutput (true);
    return ret;
  }

  /**
   * Create a writer builder for
   * {@link eu.toop.commons.dataexchange.v120.TDETOOPResponseType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopWriter <eu.toop.commons.dataexchange.v120.TDETOOPResponseType> response120 ()
  {
    final ToopWriter <eu.toop.commons.dataexchange.v120.TDETOOPResponseType> ret = new ToopWriter <> (EToopXMLDocumentType.RESPONSE_120);
    ret.setFormattedOutput (true);
    return ret;
  }

  /**
   * Create a writer builder for
   * {@link eu.toop.commons.dataexchange.v140.TDETOOPRequestType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopWriter <eu.toop.commons.dataexchange.v140.TDETOOPRequestType> request140 ()
  {
    final ToopWriter <eu.toop.commons.dataexchange.v140.TDETOOPRequestType> ret = new ToopWriter <> (EToopXMLDocumentType.REQUEST_140);
    ret.setFormattedOutput (true);
    return ret;
  }

  /**
   * Create a writer builder for
   * {@link eu.toop.commons.dataexchange.v140.TDETOOPResponseType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopWriter <eu.toop.commons.dataexchange.v140.TDETOOPResponseType> response140 ()
  {
    final ToopWriter <eu.toop.commons.dataexchange.v140.TDETOOPResponseType> ret = new ToopWriter <> (EToopXMLDocumentType.RESPONSE_140);
    ret.setFormattedOutput (true);
    return ret;
  }
}
