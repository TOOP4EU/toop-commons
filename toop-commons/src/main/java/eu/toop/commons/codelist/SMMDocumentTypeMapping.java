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
package eu.toop.commons.codelist;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsEnumMap;
import com.helger.commons.collection.impl.ICommonsMap;

/**
 * This class manages the TOOP Document type to Semantic Mapping Module
 * namespaces.
 *
 * @author Philip Helger
 */
public final class SMMDocumentTypeMapping {
  private static final ICommonsMap<EPredefinedDocumentTypeIdentifier, String> s_aMap = new CommonsEnumMap<> (EPredefinedDocumentTypeIdentifier.class);

  static {
    // Fill the map
    s_aMap.put (EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION, "http://toop.eu/organization");
    s_aMap.put (EPredefinedDocumentTypeIdentifier.RESPONSE_REGISTEREDORGANIZATION, "http://toop.eu/organization");
  }

  private SMMDocumentTypeMapping () {
  }

  @Nonnull
  @Nonempty
  public static String getToopSMNamespace (@Nullable final EPredefinedDocumentTypeIdentifier eDocType) {
    final String ret = s_aMap.get (eDocType);
    if (ret == null)
      throw new IllegalArgumentException ("Unsupported document type " + eDocType);
    return ret;
  }
}
