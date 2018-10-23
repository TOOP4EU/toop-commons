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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.helger.asic.SignatureHelper;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.math.MathHelper;
import com.helger.commons.mock.CommonsTestHelper;
import com.helger.security.keystore.EKeyStoreType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.TDEDataProviderType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import eu.toop.commons.dataexchange.TDETOOPResponseType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.jaxb.ToopXSDHelper;

/**
 * Test class for class {@link ToopMessageBuilder}.
 *
 * @author Philip Helger
 */
@SuppressWarnings ("deprecation")
public final class ToopMessageBuilderTest
{
  private static final SignatureHelper SH = new SignatureHelper (EKeyStoreType.JKS,
                                                                 "playground-keystore-v1.jks",
                                                                 "toop4eu",
                                                                 "sms-key",
                                                                 "toop4eu");

  @Test
  public void testRequestMessage () throws ToopErrorException, IOException
  {
    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
    {
      final String sCountryCode = "SE";
      final TDETOOPRequestType aSrcRequest = ToopMessageBuilder.createMockRequest (ToopMessageBuilder.createMockDataRequestSubject (sCountryCode),
                                                                                   ToopXSDHelper.createIdentifier ("toop",
                                                                                                                   "senderid"),
                                                                                   sCountryCode,
                                                                                   EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
                                                                                   EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                   new CommonsArrayList <> (new ConceptValue ("companyName",
                                                                                                                              "Acme Inc.")));
      ToopMessageBuilder.createRequestMessageAsic (aSrcRequest, aBAOS, SH);
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSrcRequest, aSrcRequest.clone ());

      try (final NonBlockingByteArrayInputStream archiveInput = aBAOS.getAsInputStream ())
      {
        // Read ASIC again
        final TDETOOPRequestType aRead = ToopMessageBuilder.parseRequestMessage (archiveInput);
        assertNotNull (aRead);

        assertEquals (aRead, aSrcRequest);
        CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aRead, aRead.clone ());
      }
    }
  }

  @Test
  public void testResponseMessage () throws ToopErrorException, IOException
  {
    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
    {
      final String sCountryCode = "SE";
      final TDETOOPResponseType aSrcResponse = ToopMessageBuilder.createMockResponse (ToopXSDHelper.createIdentifier ("toop",
                                                                                                                      "senderid"),
                                                                                      sCountryCode,
                                                                                      EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
                                                                                      EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                      new CommonsArrayList <> (new ConceptValue ("companyName",
                                                                                                                                 "Acme Inc.")));
      ToopMessageBuilder.createResponseMessageAsic (aSrcResponse, aBAOS, SH);
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSrcResponse, aSrcResponse.clone ());

      try (final NonBlockingByteArrayInputStream archiveInput = aBAOS.getAsInputStream ())
      {
        // Read ASIC again
        final TDETOOPResponseType aRead = ToopMessageBuilder.parseResponseMessage (archiveInput);
        assertNotNull (aRead);

        assertEquals (aRead, aSrcResponse);
        CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aRead, aRead.clone ());
      }
    }
  }

  @Test
  public void testResponseMessageV2 () throws ToopErrorException, IOException
  {
    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
    {
      final TDETOOPRequestType aSrcRequest = ToopMessageBuilder.createMockResponse (ToopXSDHelper.createIdentifier ("toop",
                                                                                                                    "senderid"),
                                                                                    "SE",
                                                                                    EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
                                                                                    EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                    new CommonsArrayList <> (new ConceptValue ("companyName",
                                                                                                                               "Acme Inc."),
                                                                                                             new ConceptValue ("companyVATIN",
                                                                                                                               "blafoo.vatin")));
      final TDETOOPResponseType aSrcResponse = ToopMessageBuilder.createResponse (aSrcRequest);
      {
        // Required for response
        final TDEDataProviderType p = new TDEDataProviderType ();
        p.setDPIdentifier (ToopXSDHelper.createIdentifier ("toop", "blafoo-elonia"));
        p.setDPName (ToopXSDHelper.createText ("EloniaDP"));
        p.setDPElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("elonia@register.example.org"));
        final TDEAddressType pa = new TDEAddressType ();
        pa.setCountryCode (ToopXSDHelper.createCode ("SV"));
        p.setDPLegalAddress (pa);
        aSrcResponse.setDataProvider (p);
      }
      // Add response to concept 1/2
      {
        final TDEDataElementResponseValueType aResponseValue = new TDEDataElementResponseValueType ();
        aResponseValue.setErrorIndicator (ToopXSDHelper.createIndicator (false));
        aResponseValue.setAlternativeResponseIndicator (ToopXSDHelper.createIndicator (false));
        aResponseValue.setResponseIdentifier (ToopXSDHelper.createIdentifier ("id4711"));
        aSrcResponse.getDataElementRequestAtIndex (0).getConceptRequest ().addDataElementResponseValue (aResponseValue);
      }
      // Add response to concept 2/2
      {
        final TDEDataElementResponseValueType aResponseValue = new TDEDataElementResponseValueType ();
        aResponseValue.setErrorIndicator (ToopXSDHelper.createIndicator (false));
        aResponseValue.setAlternativeResponseIndicator (ToopXSDHelper.createIndicator (false));
        aResponseValue.setResponseNumeric (ToopXSDHelper.createNumeric (MathHelper.toBigDecimal (47.11)));
        aSrcResponse.getDataElementRequestAtIndex (1).getConceptRequest ().addDataElementResponseValue (aResponseValue);
      }
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSrcResponse, aSrcResponse.clone ());
      ToopMessageBuilder.createResponseMessageAsic (aSrcResponse, aBAOS, SH);

      try (final NonBlockingByteArrayInputStream archiveInput = aBAOS.getAsInputStream ())
      {
        // Read ASIC again
        final TDETOOPResponseType aRead = ToopMessageBuilder.parseResponseMessage (archiveInput);
        assertNotNull (aRead);

        assertEquals (aRead, aSrcResponse);
        CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aRead, aRead.clone ());
      }
    }
  }

  @Test
  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public void testEmpty () throws ToopErrorException
  {
    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ())
    {
      // No data
      ToopMessageBuilder.createRequestMessageAsic (null, archiveOutput, SH);
      fail ("Exception expected");
    }
    catch (final NullPointerException ex)
    {
      // Expected
    }

    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ())
    {
      // No data
      ToopMessageBuilder.createResponseMessageAsic (null, archiveOutput, SH);
      fail ("Exception expected");
    }
    catch (final NullPointerException ex)
    {
      // Expected
    }
  }
}
