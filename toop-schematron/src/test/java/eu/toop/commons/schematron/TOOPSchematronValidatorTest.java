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
package eu.toop.commons.schematron;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.schematron.svrl.SVRLFailedAssert;

/**
 * Test class for class {@link TOOPSchematronValidator}.
 *
 * @author Philip Helger
 */
public final class TOOPSchematronValidatorTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (TOOPSchematronValidatorTest.class);

  @Test
  public void testBasic ()
  {
    final TOOPSchematronValidator v = new TOOPSchematronValidator ();

    for (final String sFilename : new String [] { "data-request-example.xml", "data-request1.xml" })
    {
      LOGGER.info ("Checking " + sFilename);

      final ICommonsList <SVRLFailedAssert> aFAs = v.validateTOOPMessage (new FileSystemResource ("src/test/resources/xml/" +
                                                                                                  sFilename));
      assertNotNull (aFAs);
      for (final SVRLFailedAssert aFA : aFAs)
        LOGGER.info (aFA.toString ());
      assertTrue (aFAs.isEmpty ());
    }
  }
}
