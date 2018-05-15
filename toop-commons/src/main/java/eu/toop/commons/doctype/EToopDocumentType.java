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
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;

/**
 * Predefined TOOP document types. Currently all mock.
 *
 * @author Philip Helger
 *
 */
public enum EToopDocumentType {
  DOCTYPE_REGISTERED_ORGANIZATION_REQUEST ("urn:eu:toop:ns:dataexchange-1p10::Request##urn:eu.toop.request.registeredorganization::1.10",
                                           "http://toop.eu/registered-organization"),
  DOCTYPE_REGISTERED_ORGANIZATION_RESPONSE ("urn:eu:toop:ns:dataexchange-1p10::Response##urn:eu.toop.response.registeredorganization::1.10",
                                            "http://toop.eu/registered-organization");

  // For R2D2 we need a scheme
  public static final String DOC_TYPE_SCHEME = "toop-doctypeid-qns";

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
  public String getSharedToopSMMNamespace () {
    return m_sSemanticMappingNS;
  }

  @Nullable
  public EToopDocumentType getMatchingResponseDocumentType () {
    if (this == DOCTYPE_REGISTERED_ORGANIZATION_REQUEST)
      return DOCTYPE_REGISTERED_ORGANIZATION_RESPONSE;

    // add additional doc type stuff here
    return null;
  }

  @Nullable
  public EToopDocumentType getMatchingRequestDocumentType () {
    if (this == DOCTYPE_REGISTERED_ORGANIZATION_RESPONSE)
      return DOCTYPE_REGISTERED_ORGANIZATION_REQUEST;

    // add additional doc type stuff here
    return null;
  }

  @Nullable
  public static EToopDocumentType getFromIDOrNull (final String sSchemeID, final String sValue) {
    for (final EToopDocumentType e : values ())
      if (e.getScheme ().equals (sSchemeID) && e.getValue ().equals (sValue))
        return e;
    return null;
  }
}
