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
 * This class manages the relationship between TOOP request and response
 * Document types, so that .
 *
 * @author Philip Helger
 */
public final class ReverseDocumentTypeMapping {
  private static final ICommonsMap<EPredefinedDocumentTypeIdentifier, EPredefinedDocumentTypeIdentifier> s_aMap = new CommonsEnumMap<> (EPredefinedDocumentTypeIdentifier.class);

  private static void _add (@Nonnull final EPredefinedDocumentTypeIdentifier aRequest,
                            @Nonnull final EPredefinedDocumentTypeIdentifier aResponse) {
    s_aMap.put (aRequest, aResponse);
    s_aMap.put (aResponse, aRequest);
  }

  static {
    // Fill the map
    _add (EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
          EPredefinedDocumentTypeIdentifier.RESPONSE_REGISTEREDORGANIZATION);
  }

  private ReverseDocumentTypeMapping () {
  }

  @Nonnull
  @Nonempty
  public static EPredefinedDocumentTypeIdentifier getReverseDocumentType (@Nullable final EPredefinedDocumentTypeIdentifier eDocType) {
    final EPredefinedDocumentTypeIdentifier ret = s_aMap.get (eDocType);
    if (ret == null)
      throw new IllegalArgumentException ("Unsupported document type " + eDocType);
    return ret;
  }
}
