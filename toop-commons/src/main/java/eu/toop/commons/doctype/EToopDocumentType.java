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
  DOCTYPE1 ("urn:eu.toop:docType1"),
  DOCTYPE2 ("urn:eu.toop:docType1"),
  DOCTYPE3 ("urn:eu.toop:docType1"),
  DOCTYPE4 ("urn:eu.toop:docType1");

  // For R2D2 we need a scheme
  public static final String DOC_TYPE_SCHEME = "toop-doctypeid";

  private final String m_sDocTypeID;

  private EToopDocumentType (@Nonnull @Nonempty final String sDocTypeID) {
    m_sDocTypeID = sDocTypeID;
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
}
