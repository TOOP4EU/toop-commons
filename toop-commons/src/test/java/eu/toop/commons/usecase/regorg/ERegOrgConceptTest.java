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
package eu.toop.commons.usecase.regorg;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.commons.string.StringHelper;

/**
 * Test class for class {@link ERegOrgConcept}.
 *
 * @author Philip Helger
 */
public final class ERegOrgConceptTest
{
  @Test
  public void testBasic ()
  {
    for (final ERegOrgConcept e : ERegOrgConcept.values ())
    {
      assertTrue (StringHelper.hasText (e.getID ()));
      assertSame (e, ERegOrgConcept.getFromIDOrNull (e.getID ()));
    }
  }
}
