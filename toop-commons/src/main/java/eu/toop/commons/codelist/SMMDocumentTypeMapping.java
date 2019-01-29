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
package eu.toop.commons.codelist;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsEnumMap;
import com.helger.commons.collection.impl.ICommonsMap;

/**
 * This class manages the TOOP Document type to Semantic Mapping Module (SMM).
 * namespaces.
 *
 * @author Philip Helger
 */
public final class SMMDocumentTypeMapping
{
  public static final String URI_REGISTERED_ORGANIZATION = "http://toop.eu/registered-organization";
  private static final ICommonsMap <EPredefinedDocumentTypeIdentifier, String> s_aMap = new CommonsEnumMap <> (EPredefinedDocumentTypeIdentifier.class);

  static
  {
    // Fill the map
    s_aMap.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P10_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_10,
                URI_REGISTERED_ORGANIZATION);
    s_aMap.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P10_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_10,
                URI_REGISTERED_ORGANIZATION);
    s_aMap.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_LIST_1_40,
                URI_REGISTERED_ORGANIZATION);
    s_aMap.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_LIST_1_40,
                URI_REGISTERED_ORGANIZATION);
    s_aMap.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_LIST_1_40,
                URI_REGISTERED_ORGANIZATION);
    s_aMap.put (EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_LIST_1_40,
                URI_REGISTERED_ORGANIZATION);
  }

  private SMMDocumentTypeMapping ()
  {}

  @Nonnull
  @Nonempty
  public static String getToopSMNamespace (@Nullable final EPredefinedDocumentTypeIdentifier eDocType)
  {
    final String ret = s_aMap.get (eDocType);
    if (ret == null)
      throw new IllegalArgumentException ("Unsupported document type " + eDocType);
    return ret;
  }
}
