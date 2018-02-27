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
package eu.toop.commons.exchange;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsList;

import eu.toop.commons.concept.ConceptValue;

/**
 * Member state request (DC to DP). This data request MUST serialize to XML
 * because it is used
 *
 * @author Philip Helger
 */
public interface IMSDataRequest extends IToopDataElement {
  /**
   * @return The participant ID of the sender needed for R2D2 invocation from DP
   *         to DC. It must contain identifier scheme and identifier values in a
   *         combined string in the form <code>scheme::value</code>.
   */
  @Nonnull
  @Nonempty
  String getSenderParticipantID ();

  /**
   * @return The ID of the destination country. May not be <code>null</code> and
   *         must be the 2-letter country code as in "DE" or "AT". This field is
   *         required for R2D2 client.
   */
  @Nonnull
  @Nonempty
  String getDestinationCountryCode ();

  /**
   * @return The document type ID that is e.g. used in R2D2 for endpoint
   *         discovery.
   */
  @Nonnull
  @Nonempty
  String getDocumentTypeID ();

  /**
   * @return The process ID that is e.g. used in R2D2 for endpoint discovery.
   */
  @Nonnull
  @Nonempty
  String getProcessID ();

  /**
   * @return A copy of the list of all concept values. Never <code>null</code> but
   *         maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  ICommonsList<ConceptValue> getAllConceptValues ();
}
