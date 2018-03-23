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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EToopDocumentTypeTest {
  @Test
  public void testBasic () {
    for (final EToopDocumentType e : EToopDocumentType.values ()) {
      assertEquals (EToopDocumentType.DOC_TYPE_SCHEME, e.getScheme ());
      assertSame (e, EToopDocumentType.getFromIDOrNull (e.getScheme (), e.getValue ()));
      // Each doc type must be request OR response
      assertTrue (e.getMatchingRequestDocumentType () != null || e.getMatchingResponseDocumentType () != null);
    }
  }
}
