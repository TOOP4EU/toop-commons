/**
 * Copyright (C) 2018-2020 toop.eu
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
package eu.toop.commons.usecase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsEnumMap;
import com.helger.commons.collection.impl.ICommonsMap;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;

/**
 * This class manages the TOOP Document type to Semantic Mapping Module (SMM).
 * namespaces.
 *
 * @author Philip Helger
 */
@SuppressWarnings ("deprecation")
public final class SMMDocumentTypeMapping
{
  public static final String SMM_DOMAIN_REGISTERED_ORGANIZATION = "http://toop.eu/registered-organization";
  private static final ICommonsMap <EPredefinedDocumentTypeIdentifier, String> MAP = new CommonsEnumMap <> (EPredefinedDocumentTypeIdentifier.class);

  static
  {
    // Fill the map
    MAP.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P10_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_10,
             SMM_DOMAIN_REGISTERED_ORGANIZATION);
    MAP.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P10_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_10,
             SMM_DOMAIN_REGISTERED_ORGANIZATION);
    MAP.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_40,
             SMM_DOMAIN_REGISTERED_ORGANIZATION);
    MAP.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_40,
             SMM_DOMAIN_REGISTERED_ORGANIZATION);
    MAP.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_LIST_1_40,
             SMM_DOMAIN_REGISTERED_ORGANIZATION);
    MAP.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_LIST_1_40,
             SMM_DOMAIN_REGISTERED_ORGANIZATION);
  }

  private SMMDocumentTypeMapping ()
  {}

  @Nullable
  public static String getToopSMDomainOrNull (@Nullable final EPredefinedDocumentTypeIdentifier eDocType)
  {
    return MAP.get (eDocType);
  }

  @Nonnull
  @Nonempty
  public static String getToopSMDomain (@Nullable final EPredefinedDocumentTypeIdentifier eDocType)
  {
    final String ret = getToopSMDomainOrNull (eDocType);
    if (ret == null)
      throw new IllegalArgumentException ("Unsupported document type " + eDocType);
    return ret;
  }

  /**
   * @return A copy of the map used for SMM domain mapping. Never
   *         <code>null</code>.
   * @since 0.10.2
   */
  @Nonnull
  @ReturnsMutableObject
  public static ICommonsMap <EPredefinedDocumentTypeIdentifier, String> getAllMappings ()
  {
    return MAP.getClone ();
  }
}
