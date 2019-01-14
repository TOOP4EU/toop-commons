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
package eu.toop.commons.jaxb;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.datetime.util.PDTXMLConverter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriter;
import com.helger.xml.serialize.write.XMLWriterSettings;

import eu.toop.commons.concept.EConceptType;
import eu.toop.commons.dataexchange.v140.ObjectFactory;
import eu.toop.commons.dataexchange.v140.TDEAddressType;
import eu.toop.commons.dataexchange.v140.TDEConceptRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataConsumerType;
import eu.toop.commons.dataexchange.v140.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataProviderType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestAuthorizationType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDENaturalPersonType;
import eu.toop.commons.dataexchange.v140.TDERoutingInformationType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.BinaryObjectType;

/**
 * Test class for {@link ToopReader} and {@link ToopWriter}.
 *
 * @author Philip Helger
 */
public final class ToopDataExchangeFuncTest
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ToopDataExchangeFuncTest.class);

  @Test
  public void testReadWriteDataRequest ()
  {
    for (final String sFilename : new String [] { "commander-request1.xml",
                                                  "data-request-document-example.xml",
                                                  "data-request-example.xml",
                                                  "data-request1.xml" })
    {
      final TDETOOPRequestType aRequest = ToopReader.request ()
                                                    .read (new File ("src/test/resources/xml/1.4.0/" + sFilename));
      assertNotNull (aRequest);
      final String sXML = ToopWriter.request ().getAsString (aRequest);
      assertNotNull (sXML);
    }
  }

  @Test
  public void testReadWriteDataResponse ()
  {
    for (final String sFilename : new String [] { "data-response-document-example.xml",
                                                  "data-response-error1.xml",
                                                  "data-response-example.xml",
                                                  "data-response-with-ERROR-example.xml",
                                                  "data-response1.xml" })
    {
      final TDETOOPResponseType aResponse = ToopReader.response ()
                                                      .read (new File ("src/test/resources/xml/1.4.0/" + sFilename));
      assertNotNull (aResponse);
      final String sXML = ToopWriter.response ().getAsString (aResponse);
      assertNotNull (sXML);
    }
  }

  @Test
  public void testCreateRequestFromScratch ()
  {
    final TDETOOPRequestType r = new TDETOOPRequestType ();
    r.setDocumentUniversalUniqueIdentifier (ToopXSDHelper.createIdentifier (UUID.randomUUID ().toString ()));
    r.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    r.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    r.setCopyIndicator (ToopXSDHelper.createIndicator (false));
    r.setSpecificationIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    r.setDataConsumerDocumentIdentifier (ToopXSDHelper.createIdentifier ("DC-ID-17"));
    r.setDataRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    {
      final TDERoutingInformationType ri = new TDERoutingInformationType ();
      // Document type ID
      ri.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier ("toop-doctypeid",
                                                                    "data.request.registeredorganization"));
      // Process ID
      ri.setProcessIdentifier (ToopXSDHelper.createIdentifier ("toop-procid", "urn:toop:www.toop.eu/data-request"));
      // Destination country code
      ri.setDataProviderCountryCode (ToopXSDHelper.createCode ("AT"));
      r.setRoutingInformation (ri);
    }
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper.createIdentifier ("ATU12345678"));
      aDC.setDCName (ToopXSDHelper.createText ("Helger Enterprises"));
      // Sender participant ID
      aDC.setDCElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("iso6523-actorid-upis", "9915:test"));
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper.createCodeWithLOA ("AT"));
      aDC.setDCLegalAddress (aAddress);
      r.setDataConsumer (aDC);
    }
    {
      final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
      aDS.setDataRequestSubjectTypeCode (ToopXSDHelper.createCode ("12345"));
      {
        final TDENaturalPersonType aNP = new TDENaturalPersonType ();
        aNP.setPersonIdentifier (ToopXSDHelper.createIdentifierWithLOA ("bla"));
        aNP.setFamilyName (ToopXSDHelper.createTextWithLOA ("Helger"));
        aNP.setFirstName (ToopXSDHelper.createTextWithLOA ("Philip"));
        aNP.setBirthDate (ToopXSDHelper.createDateWithLOANow ());
        final TDEAddressType aAddress = new TDEAddressType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper.createCodeWithLOA ("DE"));
        aNP.setNaturalPersonLegalAddress (aAddress);
        aDS.setNaturalPerson (aNP);
      }
      r.setDataRequestSubject (aDS);
    }
    {
      final TDEDataRequestAuthorizationType aAuth = new TDEDataRequestAuthorizationType ();
      final BinaryObjectType aBO = new BinaryObjectType ();
      aBO.setValue ("11101010101010001110101".getBytes (StandardCharsets.ISO_8859_1));
      aBO.setMimeCode ("application/octet-stream");
      aAuth.setDataRequestConsentToken (aBO);
      r.setDataRequestAuthorization (aAuth);
    }
    {
      final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
      aReq.setDataElementRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
      {
        final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
        aSrcConcept.setConceptTypeCode (ToopXSDHelper.createCode (EConceptType.DC.getID ()));
        aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
        aSrcConcept.setConceptNamespace (ToopXSDHelper.createIdentifier ("elUri"));
        aSrcConcept.setConceptName (ToopXSDHelper.createText ("elType"));

        {
          final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
          aToopConcept.setConceptTypeCode (ToopXSDHelper.createCode (EConceptType.TC.getID ()));
          aToopConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
          aToopConcept.setConceptNamespace (ToopXSDHelper.createIdentifier ("toopUri"));
          aToopConcept.setConceptName (ToopXSDHelper.createText ("toopType"));
          aSrcConcept.addConceptRequest (aToopConcept);
        }
        aReq.setConceptRequest (aSrcConcept);
      }

      r.addDataElementRequest (aReq);
    }

    final Document aDoc = ToopWriter.request ().getAsDocument (r);
    assertNotNull (aDoc);
    if (true)
    {
      final MapBasedNamespaceContext aCtx = new MapBasedNamespaceContext ();
      aCtx.addMapping ("toop", ObjectFactory._Request_QNAME.getNamespaceURI ());
      s_aLogger.info (XMLWriter.getNodeAsString (aDoc, new XMLWriterSettings ().setNamespaceContext (aCtx)));
    }
  }

  @Test
  public void testCreateResponseFromScratch ()
  {
    final TDETOOPResponseType r = new TDETOOPResponseType ();
    r.setDocumentUniversalUniqueIdentifier (ToopXSDHelper.createIdentifier (UUID.randomUUID ().toString ()));
    r.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    r.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    r.setCopyIndicator (ToopXSDHelper.createIndicator (false));
    r.setSpecificationIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    r.setDataConsumerDocumentIdentifier (ToopXSDHelper.createIdentifier ("DC-ID-17"));
    r.setDataRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    {
      final TDERoutingInformationType ri = new TDERoutingInformationType ();
      // Document type ID
      ri.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier ("toop-doctypeid",
                                                                    "data.request.registeredorganization"));
      // Process ID
      ri.setProcessIdentifier (ToopXSDHelper.createIdentifier ("toop-procid", "urn:toop:www.toop.eu/data-request"));
      // Destination country code
      ri.setDataProviderCountryCode (ToopXSDHelper.createCode ("AT"));
      r.setRoutingInformation (ri);
    }
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper.createIdentifier ("ATU12345678"));
      aDC.setDCName (ToopXSDHelper.createText ("Helger Enterprises"));
      // Sender participant ID
      aDC.setDCElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("iso6523-actorid-upis", "9915:test"));
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper.createCodeWithLOA ("AT"));
      aDC.setDCLegalAddress (aAddress);
      r.setDataConsumer (aDC);
    }
    {
      final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
      aDS.setDataRequestSubjectTypeCode (ToopXSDHelper.createCode ("12345"));
      {
        final TDENaturalPersonType aNP = new TDENaturalPersonType ();
        aNP.setPersonIdentifier (ToopXSDHelper.createIdentifierWithLOA ("bla"));
        aNP.setFamilyName (ToopXSDHelper.createTextWithLOA ("Helger"));
        aNP.setFirstName (ToopXSDHelper.createTextWithLOA ("Philip"));
        aNP.setBirthDate (ToopXSDHelper.createDateWithLOANow ());
        final TDEAddressType aAddress = new TDEAddressType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper.createCodeWithLOA ("DE"));
        aNP.setNaturalPersonLegalAddress (aAddress);
        aDS.setNaturalPerson (aNP);
      }
      r.setDataRequestSubject (aDS);
    }
    {
      final TDEDataRequestAuthorizationType aAuth = new TDEDataRequestAuthorizationType ();
      final BinaryObjectType aBO = new BinaryObjectType ();
      aBO.setValue ("11101010101010001110101".getBytes (StandardCharsets.ISO_8859_1));
      aBO.setMimeCode ("application/octet-stream");
      aAuth.setDataRequestConsentToken (aBO);
      r.setDataRequestAuthorization (aAuth);
    }
    {
      final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
      aReq.setDataElementRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
      {
        final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
        aSrcConcept.setConceptTypeCode (ToopXSDHelper.createCode (EConceptType.DC.getID ()));
        aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
        aSrcConcept.setConceptNamespace (ToopXSDHelper.createIdentifier ("elUri"));
        aSrcConcept.setConceptName (ToopXSDHelper.createText ("elType"));

        {
          final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
          aToopConcept.setConceptTypeCode (ToopXSDHelper.createCode (EConceptType.TC.getID ()));
          aToopConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
          aToopConcept.setConceptNamespace (ToopXSDHelper.createIdentifier ("toopUri"));
          aToopConcept.setConceptName (ToopXSDHelper.createText ("toopType"));
          aSrcConcept.addConceptRequest (aToopConcept);
        }
        aReq.setConceptRequest (aSrcConcept);
      }

      r.addDataElementRequest (aReq);
    }

    {
      final TDEDataProviderType p = new TDEDataProviderType ();
      p.setDPIdentifier (ToopXSDHelper.createIdentifier ("atbla"));
      p.setDPName (ToopXSDHelper.createText ("Register1"));
      p.setDPElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("me@register.example.org"));
      final TDEAddressType pa = new TDEAddressType ();
      pa.setCountryCode (ToopXSDHelper.createCodeWithLOA ("XK"));
      p.setDPLegalAddress (pa);
      r.addDataProvider (p);
    }

    final Document aDoc = ToopWriter.response ().getAsDocument (r);
    assertNotNull (aDoc);
    if (true)
    {
      final MapBasedNamespaceContext aCtx = new MapBasedNamespaceContext ();
      aCtx.addMapping ("toop", ObjectFactory._Request_QNAME.getNamespaceURI ());
      s_aLogger.info (XMLWriter.getNodeAsString (aDoc, new XMLWriterSettings ().setNamespaceContext (aCtx)));
    }
  }
}
