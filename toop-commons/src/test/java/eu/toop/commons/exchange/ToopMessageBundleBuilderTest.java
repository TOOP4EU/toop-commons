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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.helger.asic.SignatureHelper;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEDataProviderType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import eu.toop.commons.dataexchange.TDETOOPResponseType;
import eu.toop.commons.doctype.EToopDocumentType;
import eu.toop.commons.doctype.EToopProcess;
import eu.toop.commons.jaxb.ToopXSDHelper;

public final class ToopMessageBundleBuilderTest {
  private static final SignatureHelper SH = new SignatureHelper (FileHelper.getInputStream (new File ("src/test/resources/playground-keystore.jks")),
                                                                 "toop4eu", "sms-key", "toop4eu");

  @Test
  public void testRequestMessage () throws IOException {
    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ()) {
      final TDETOOPRequestType aSrcRequest = ToopMessageBuilder.createMockRequest (ToopXSDHelper.createIdentifier ("toop",
                                                                                                                   "senderid"),
                                                                                   "SE",
                                                                                   EToopDocumentType.DOCTYPE_REGISTERED_ORGANIZATION_REQUEST,
                                                                                   EToopProcess.REQUEST_RESPONSE_DATA,
                                                                                   new CommonsArrayList<> (new ConceptValue ("companyName",
                                                                                                                             "Acme Inc.")));
      ToopMessageBuilder.createRequestMessage (aSrcRequest, archiveOutput, SH);

      try (final NonBlockingByteArrayInputStream archiveInput = archiveOutput.getAsInputStream ()) {
        // Read ASIC again
        final TDETOOPRequestType aRead = ToopMessageBuilder.parseRequestMessage (archiveInput);
        assertNotNull (aRead);

        assertEquals (aRead, aSrcRequest);
      }
    }
  }

  @Test
  public void testResponseMessage () throws IOException {
    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ()) {
      final TDETOOPResponseType aSrcResponse = ToopMessageBuilder.createMockResponse (ToopXSDHelper.createIdentifier ("toop",
                                                                                                                      "senderid"),
                                                                                      "SE",
                                                                                      EToopDocumentType.DOCTYPE_REGISTERED_ORGANIZATION_REQUEST,
                                                                                      EToopProcess.REQUEST_RESPONSE_DATA,
                                                                                      new CommonsArrayList<> (new ConceptValue ("companyName",
                                                                                                                                "Acme Inc.")));
      ToopMessageBuilder.createResponseMessage (aSrcResponse, archiveOutput, SH);

      try (final NonBlockingByteArrayInputStream archiveInput = archiveOutput.getAsInputStream ()) {
        // Read ASIC again
        final TDETOOPResponseType aRead = ToopMessageBuilder.parseResponseMessage (archiveInput);
        assertNotNull (aRead);

        assertEquals (aRead, aSrcResponse);
      }
    }
  }

  @Test
  public void testResponseMessageV2 () throws IOException {
    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ()) {
      final TDETOOPRequestType aSrcRequest = ToopMessageBuilder.createMockResponse (ToopXSDHelper.createIdentifier ("toop",
                                                                                                                    "senderid"),
                                                                                    "SE",
                                                                                    EToopDocumentType.DOCTYPE_REGISTERED_ORGANIZATION_REQUEST,
                                                                                    EToopProcess.REQUEST_RESPONSE_DATA,
                                                                                    new CommonsArrayList<> (new ConceptValue ("companyName",
                                                                                                                              "Acme Inc.")));
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
      ToopMessageBuilder.createResponseMessage (aSrcResponse, archiveOutput, SH);

      try (final NonBlockingByteArrayInputStream archiveInput = archiveOutput.getAsInputStream ()) {
        // Read ASIC again
        final TDETOOPResponseType aRead = ToopMessageBuilder.parseResponseMessage (archiveInput);
        assertNotNull (aRead);

        assertEquals (aRead, aSrcResponse);
      }
    }
  }

  @Test
  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public void testEmpty () throws IOException {
    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ()) {
      // No data
      ToopMessageBuilder.createRequestMessage (null, archiveOutput, SH);
      fail ("Exception expected");
    } catch (final NullPointerException ex) {
      // Expected
    }

    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ()) {
      // No data
      ToopMessageBuilder.createResponseMessage (null, archiveOutput, SH);
      fail ("Exception expected");
    } catch (final NullPointerException ex) {
      // Expected
    }
  }
}
