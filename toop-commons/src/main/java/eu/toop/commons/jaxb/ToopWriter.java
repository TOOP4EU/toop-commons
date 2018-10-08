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

import com.helger.jaxb.builder.JAXBWriterBuilder;

import eu.toop.commons.dataexchange.TDETOOPErrorMessageType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import eu.toop.commons.dataexchange.TDETOOPResponseType;

public class ToopWriter<JAXBTYPE> extends JAXBWriterBuilder<JAXBTYPE, ToopWriter<JAXBTYPE>> {
  /**
   * Constructor with an arbitrary document type.
   *
   * @param aDocType
   *          Document type to be used. May not be <code>null</code>.
   */
  public ToopWriter (@Nonnull final EToopXMLDocumentType aDocType) {
    super (aDocType);
  }

  /**
   * Create a writer builder for {@link TDETOOPRequestType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopWriter<TDETOOPRequestType> request () {
    final ToopWriter<TDETOOPRequestType> ret = new ToopWriter<> (EToopXMLDocumentType.REQUEST);
    ret.setFormattedOutput (true);
    return ret;
  }

  /**
   * Create a writer builder for {@link TDETOOPResponseType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopWriter<TDETOOPResponseType> response () {
    final ToopWriter<TDETOOPResponseType> ret = new ToopWriter<> (EToopXMLDocumentType.RESPONSE);
    ret.setFormattedOutput (true);
    return ret;
  }

  /**
   * Create a writer builder for {@link TDETOOPErrorMessageType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopWriter<TDETOOPErrorMessageType> errorMessage () {
    final ToopWriter<TDETOOPErrorMessageType> ret = new ToopWriter<> (EToopXMLDocumentType.ERROR_MESSAGE);
    ret.setFormattedOutput (true);
    return ret;
  }
}
