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
 * Predefined TOOP process ID. Currently mock.
 *
 * @author Philip Helger
 *
 */
public enum EToopProcess {
  PROCESS_REQUEST_RESPONSE ("urn:eu.toop:request-response");

  // For R2D2 we need a scheme
  public static final String PROCESS_SCHEME = "toop-procid";

  private final String m_sProcessID;

  private EToopProcess (@Nonnull @Nonempty final String sProcessID) {
    m_sProcessID = sProcessID;
  }

  /**
   * @return Always {@link #PROCESS_SCHEME}.
   */
  @Nonnull
  @Nonempty
  public String getScheme () {
    return PROCESS_SCHEME;
  }

  /**
   *
   * @return The process ID (value) from the constructor. Does not contain the
   *         scheme!
   */
  @Nonnull
  @Nonempty
  public String getValue () {
    return m_sProcessID;
  }

  @Nonnull
  @Nonempty
  public String getURIEncoded () {
    return getScheme () + "::" + getValue ();
  }
}
