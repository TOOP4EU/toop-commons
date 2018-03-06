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
package eu.toop.commons.doctype;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * Predefined TOOP document types. Currently all mock.
 *
 * @author Philip Helger
 *
 */
public enum EToopDocumentType {
  DOCTYPE_REGISTERED_ORGANIZATION_REQUEST ("urn:eu.toop:registered-organization:request",
                                           "http://toop.eu/organization"),
  DOCTYPE_REGISTERED_ORGANIZATION_RESPONSE ("urn:eu.toop:registered-organization:response",
                                            "http://toop.eu/organization");

  // For R2D2 we need a scheme
  public static final String DOC_TYPE_SCHEME = "toop-doctypeid";

  private final String m_sDocTypeID;
  private final String m_sSemanticMappingNS;

  private EToopDocumentType (@Nonnull @Nonempty final String sDocTypeID,
                             @Nonnull @Nonempty final String sSemanticMappingNS) {
    m_sDocTypeID = sDocTypeID;
    m_sSemanticMappingNS = sSemanticMappingNS;
  }

  /**
   * @return Always {@link #DOC_TYPE_SCHEME}.
   */
  @Nonnull
  @Nonempty
  public String getScheme () {
    return DOC_TYPE_SCHEME;
  }

  /**
   *
   * @return The document type ID (value) from the constructor. Does not contain
   *         the scheme!
   */
  @Nonnull
  @Nonempty
  public String getValue () {
    return m_sDocTypeID;
  }

  @Nonnull
  @Nonempty
  public String getURIEncoded () {
    return getScheme () + "::" + getValue ();
  }

  @Nonnull
  @Nonempty
  public String getSemanticMappingNamespace () {
    return m_sSemanticMappingNS;
  }
}
