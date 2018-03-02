package eu.toop.commons.jaxb;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import com.helger.datetime.util.PDTXMLConverter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriter;
import com.helger.xml.serialize.write.XMLWriterSettings;

import eu.toop.commons.dataexchange.ObjectFactory;
import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEConceptRequestType;
import eu.toop.commons.dataexchange.TDEDataConsumerType;
import eu.toop.commons.dataexchange.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.TDEDataProviderType;
import eu.toop.commons.dataexchange.TDEDataRequestAuthorizationType;
import eu.toop.commons.dataexchange.TDEDataSubjectType;
import eu.toop.commons.dataexchange.TDENaturalPersonType;
import eu.toop.commons.dataexchange.TDETOOPDataRequestType;
import eu.toop.commons.dataexchange.TDETOOPDataResponseType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.BinaryObjectType;

/**
 * Test class for {@link ToopReader} and {@link ToopWriter}.
 *
 * @author Philip Helger
 */
public final class ToopDataExchangeFuncTest {
  @Test
  public void testReadWriteDataRequest () {
    final TDETOOPDataRequestType aRequest = ToopReader.dataRequest ()
                                                      .read (new File ("src/test/resources/xml/data-request1.xml"));
    assertNotNull (aRequest);
    final String sXML = ToopWriter.dataRequest ().getAsString (aRequest);
    assertNotNull (sXML);
  }

  @Test
  public void testReadWriteDataResponse () {
    final TDETOOPDataResponseType aResponse = ToopReader.dataResponse ()
                                                        .read (new File ("src/test/resources/xml/data-response1.xml"));
    assertNotNull (aResponse);
    final String sXML = ToopWriter.dataResponse ().getAsString (aResponse);
    assertNotNull (sXML);
  }

  @Test
  public void testCreateRequestFromScratch () {
    final TDETOOPDataRequestType r = new TDETOOPDataRequestType ();
    r.setDocumentUniversalUniqueIdentifier (ToopXSDHelper.createIdentifier (UUID.randomUUID ().toString ()));
    r.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    r.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    r.setCopyIndicator (ToopXSDHelper.createIndicator (false));
    // Document type ID
    r.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier ("toop-doctypeid",
                                                                 "data.request.registeredorganization"));
    r.setSpecificationIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    // Process ID
    r.setProcessIdentifier (ToopXSDHelper.createIdentifier ("toop-procid", "urn:toop:www.toop.eu/data-request"));
    r.setDataConsumerDocumentIdentifier (ToopXSDHelper.createIdentifier ("DC-ID-17"));
    r.setDataRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper.createIdentifier ("ATU12345678"));
      aDC.setDCName (ToopXSDHelper.createText ("Helger Enterprises"));
      // Sender participant ID
      aDC.setDCElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("iso6523-actorid-upis", "9915:test"));
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper.createCode ("AT"));
      aDC.setDCLegalAddress (aAddress);
      r.setDataConsumer (aDC);
    }
    {
      final TDEDataSubjectType aDS = new TDEDataSubjectType ();
      aDS.setDataSubjectTypeCode (ToopXSDHelper.createCode ("12345"));
      {
        final TDENaturalPersonType aNP = new TDENaturalPersonType ();
        aNP.setPersonIdentifier (ToopXSDHelper.createIdentifier ("bla"));
        aNP.setFamilyName (ToopXSDHelper.createText ("Helger"));
        aNP.setFirstName (ToopXSDHelper.createText ("Philip"));
        aNP.setBirthDate (PDTXMLConverter.getXMLCalendarDateNow ());
        final TDEAddressType aAddress = new TDEAddressType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper.createCode ("DE"));
        aNP.setNaturalPersonLegalAddress (aAddress);
        aDS.setNaturalPerson (aNP);
      }
      r.setDataSubject (aDS);
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
        aSrcConcept.setConceptTypeCode (ToopXSDHelper.createCode ("DC"));
        aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
        aSrcConcept.setConceptNamespace (ToopXSDHelper.createIdentifier ("elUri"));
        aSrcConcept.setConceptName (ToopXSDHelper.createText ("elType"));

        {
          final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
          aToopConcept.setConceptTypeCode (ToopXSDHelper.createCode ("TOOP"));
          aToopConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
          aToopConcept.setConceptNamespace (ToopXSDHelper.createIdentifier ("toopUri"));
          aToopConcept.setConceptName (ToopXSDHelper.createText ("toopType"));
          aSrcConcept.addConceptRequest (aToopConcept);
        }
        aReq.setConceptRequest (aSrcConcept);
      }

      r.addDataElementRequest (aReq);
    }

    final Document aDoc = ToopWriter.dataRequest ().getAsDocument (r);
    assertNotNull (aDoc);
    if (true) {
      final MapBasedNamespaceContext aCtx = new MapBasedNamespaceContext ();
      aCtx.addMapping ("toop", ObjectFactory._TOOPDataRequest_QNAME.getNamespaceURI ());
      System.out.println (XMLWriter.getNodeAsString (aDoc, new XMLWriterSettings ().setNamespaceContext (aCtx)));
    }
  }

  @Test
  public void testCreateResponseFromScratch () {
    final TDETOOPDataResponseType r = new TDETOOPDataResponseType ();
    r.setDocumentUniversalUniqueIdentifier (ToopXSDHelper.createIdentifier (UUID.randomUUID ().toString ()));
    r.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    r.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    r.setCopyIndicator (ToopXSDHelper.createIndicator (false));
    // Document type ID
    r.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier ("toop-doctypeid",
                                                                 "data.request.registeredorganization"));
    r.setSpecificationIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    // Process ID
    r.setProcessIdentifier (ToopXSDHelper.createIdentifier ("toop-procid", "urn:toop:www.toop.eu/data-request"));
    r.setDataConsumerDocumentIdentifier (ToopXSDHelper.createIdentifier ("DC-ID-17"));
    r.setDataRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper.createIdentifier ("ATU12345678"));
      aDC.setDCName (ToopXSDHelper.createText ("Helger Enterprises"));
      // Sender participant ID
      aDC.setDCElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("iso6523-actorid-upis", "9915:test"));
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper.createCode ("AT"));
      aDC.setDCLegalAddress (aAddress);
      r.setDataConsumer (aDC);
    }
    {
      final TDEDataSubjectType aDS = new TDEDataSubjectType ();
      aDS.setDataSubjectTypeCode (ToopXSDHelper.createCode ("12345"));
      {
        final TDENaturalPersonType aNP = new TDENaturalPersonType ();
        aNP.setPersonIdentifier (ToopXSDHelper.createIdentifier ("bla"));
        aNP.setFamilyName (ToopXSDHelper.createText ("Helger"));
        aNP.setFirstName (ToopXSDHelper.createText ("Philip"));
        aNP.setBirthDate (PDTXMLConverter.getXMLCalendarDateNow ());
        final TDEAddressType aAddress = new TDEAddressType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper.createCode ("DE"));
        aNP.setNaturalPersonLegalAddress (aAddress);
        aDS.setNaturalPerson (aNP);
      }
      r.setDataSubject (aDS);
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
        aSrcConcept.setConceptTypeCode (ToopXSDHelper.createCode ("DC"));
        aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
        aSrcConcept.setConceptNamespace (ToopXSDHelper.createIdentifier ("elUri"));
        aSrcConcept.setConceptName (ToopXSDHelper.createText ("elType"));

        {
          final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
          aToopConcept.setConceptTypeCode (ToopXSDHelper.createCode ("TOOP"));
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
      pa.setCountryCode (ToopXSDHelper.createCode ("XK"));
      p.setDPLegalAddress (pa);
      r.setDataProvider (p);
    }

    final Document aDoc = ToopWriter.dataResponse ().getAsDocument (r);
    assertNotNull (aDoc);
    if (true) {
      final MapBasedNamespaceContext aCtx = new MapBasedNamespaceContext ();
      aCtx.addMapping ("toop", ObjectFactory._TOOPDataRequest_QNAME.getNamespaceURI ());
      System.out.println (XMLWriter.getNodeAsString (aDoc, new XMLWriterSettings ().setNamespaceContext (aCtx)));
    }
  }
}
