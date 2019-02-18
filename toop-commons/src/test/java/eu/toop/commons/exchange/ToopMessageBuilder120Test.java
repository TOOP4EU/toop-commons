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
import eu.toop.commons.dataexchange.v120.TDEAddressType;
import eu.toop.commons.dataexchange.v120.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.v120.TDEDataProviderType;
import eu.toop.commons.dataexchange.v120.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v120.TDETOOPResponseType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.jaxb.ToopXSDHelper120;

/**
 * Test class for class {@link ToopMessageBuilder120}.
 *
 * @author Philip Helger
 */
@SuppressWarnings ("deprecation")
public final class ToopMessageBuilder120Test
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
      final String sDCCountryCode = "SE";
      final String sDPCountryCode = "SE";
      final TDETOOPRequestType aSrcRequest = ToopMessageBuilder120.createMockRequest (ToopMessageBuilder120.createMockDataRequestSubject (sDCCountryCode,
                                                                                                                                          sDPCountryCode,
                                                                                                                                          true,
                                                                                                                                          "id"),
                                                                                      sDCCountryCode,
                                                                                      sDPCountryCode,
                                                                                      ToopXSDHelper120.createIdentifier ("toop",
                                                                                                                         "senderid"),
                                                                                      EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
                                                                                      EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                      new CommonsArrayList <> (new ConceptValue ("companyName",
                                                                                                                                 "Acme Inc.")));
      ToopMessageBuilder120.createRequestMessageAsic (aSrcRequest, aBAOS, SH);
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSrcRequest, aSrcRequest.clone ());

      try (final NonBlockingByteArrayInputStream archiveInput = aBAOS.getAsInputStream ())
      {
        // Read ASIC again
        final TDETOOPRequestType aRead = ToopMessageBuilder120.parseRequestMessage (archiveInput);
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
      final String sDCCountryCode = "SE";
      final String sDPCountryCode = "SE";
      final TDETOOPResponseType aSrcResponse = ToopMessageBuilder120.createMockResponse (ToopXSDHelper120.createIdentifier ("toop",
                                                                                                                            "senderid"),
                                                                                         ToopMessageBuilder120.createMockDataRequestSubject (sDCCountryCode,
                                                                                                                                             sDPCountryCode,
                                                                                                                                             true,
                                                                                                                                             "id"),
                                                                                         sDCCountryCode,
                                                                                         sDPCountryCode,
                                                                                         EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
                                                                                         EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                         new CommonsArrayList <> (new ConceptValue ("companyName",
                                                                                                                                    "Acme Inc.")));
      ToopMessageBuilder120.createResponseMessageAsic (aSrcResponse, aBAOS, SH);
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSrcResponse, aSrcResponse.clone ());

      try (final NonBlockingByteArrayInputStream archiveInput = aBAOS.getAsInputStream ())
      {
        // Read ASIC again
        final TDETOOPResponseType aRead = ToopMessageBuilder120.parseResponseMessage (archiveInput);
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
      final String sDCCountryCode = "SE";
      final String sDPCountryCode = "SE";
      final TDETOOPRequestType aSrcRequest = ToopMessageBuilder120.createMockResponse (ToopXSDHelper120.createIdentifier ("toop",
                                                                                                                          "senderid"),
                                                                                       ToopMessageBuilder120.createMockDataRequestSubject (sDCCountryCode,
                                                                                                                                           sDPCountryCode,
                                                                                                                                           true,
                                                                                                                                           "id"),
                                                                                       sDCCountryCode,
                                                                                       sDPCountryCode,
                                                                                       EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
                                                                                       EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                       new CommonsArrayList <> (new ConceptValue ("companyName",
                                                                                                                                  "Acme Inc."),
                                                                                                                new ConceptValue ("companyVATIN",
                                                                                                                                  "blafoo.vatin")));
      final TDETOOPResponseType aSrcResponse = ToopMessageBuilder120.createResponse (aSrcRequest);
      {
        // Required for response
        final TDEDataProviderType p = new TDEDataProviderType ();
        p.setDPIdentifier (ToopXSDHelper120.createIdentifier ("toop", "blafoo-elonia"));
        p.setDPName (ToopXSDHelper120.createText ("EloniaDP"));
        p.setDPElectronicAddressIdentifier (ToopXSDHelper120.createIdentifier ("elonia@register.example.org"));
        final TDEAddressType pa = new TDEAddressType ();
        pa.setCountryCode (ToopXSDHelper120.createCode ("SV"));
        p.setDPLegalAddress (pa);
        aSrcResponse.addDataProvider (p);
      }
      // Add response to concept 1/2
      {
        final TDEDataElementResponseValueType aResponseValue = new TDEDataElementResponseValueType ();
        aResponseValue.setErrorIndicator (ToopXSDHelper120.createIndicator (false));
        aResponseValue.setAlternativeResponseIndicator (ToopXSDHelper120.createIndicator (false));
        aResponseValue.setResponseIdentifier (ToopXSDHelper120.createIdentifier ("id4711"));
        aSrcResponse.getDataElementRequestAtIndex (0).getConceptRequest ().addDataElementResponseValue (aResponseValue);
      }
      // Add response to concept 2/2
      {
        final TDEDataElementResponseValueType aResponseValue = new TDEDataElementResponseValueType ();
        aResponseValue.setErrorIndicator (ToopXSDHelper120.createIndicator (false));
        aResponseValue.setAlternativeResponseIndicator (ToopXSDHelper120.createIndicator (false));
        aResponseValue.setResponseNumeric (ToopXSDHelper120.createNumeric (MathHelper.toBigDecimal (47.11)));
        aSrcResponse.getDataElementRequestAtIndex (1).getConceptRequest ().addDataElementResponseValue (aResponseValue);
      }
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSrcResponse, aSrcResponse.clone ());
      ToopMessageBuilder120.createResponseMessageAsic (aSrcResponse, aBAOS, SH);

      try (final NonBlockingByteArrayInputStream archiveInput = aBAOS.getAsInputStream ())
      {
        // Read ASIC again
        final TDETOOPResponseType aRead = ToopMessageBuilder120.parseResponseMessage (archiveInput);
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
      ToopMessageBuilder120.createRequestMessageAsic (null, archiveOutput, SH);
      fail ("Exception expected");
    }
    catch (final NullPointerException ex)
    {
      // Expected
    }

    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ())
    {
      // No data
      ToopMessageBuilder120.createResponseMessageAsic (null, archiveOutput, SH);
      fail ("Exception expected");
    }
    catch (final NullPointerException ex)
    {
      // Expected
    }
  }
}
