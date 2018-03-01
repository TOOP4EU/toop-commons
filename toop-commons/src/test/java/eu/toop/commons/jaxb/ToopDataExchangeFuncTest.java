package eu.toop.commons.jaxb;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import com.helger.datetime.util.PDTXMLConverter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriter;
import com.helger.xml.serialize.write.XMLWriterSettings;

import eu.toop.commons.dataexchange.ObjectFactory;
import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDECodeTypeTwoUppercaseLetters;
import eu.toop.commons.dataexchange.TDEConceptRequestType;
import eu.toop.commons.dataexchange.TDEDataConsumerType;
import eu.toop.commons.dataexchange.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.TDEDataRequestAuthorizationType;
import eu.toop.commons.dataexchange.TDEDataSubjectType;
import eu.toop.commons.dataexchange.TDENaturalPersonType;
import eu.toop.commons.dataexchange.TDETOOPDataRequestType;
import eu.toop.commons.dataexchange.TDETOOPDataResponseType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.BinaryObjectType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.CodeType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IndicatorType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

/**
 * Test class for {@link ToopReader} and {@link ToopWriter}.
 *
 * @author Philip Helger
 */
public final class ToopDataExchangeFuncTest {
  @Test
  public void testReadWriteExample () {
    final TDETOOPDataResponseType aResponse = ToopReader.dataResponse ()
                                                        .read (new File ("src/test/resources/xml/instance1.xml"));
    assertNotNull (aResponse);
    final String sXML = ToopWriter.dataResponse ().getAsString (aResponse);
    assertNotNull (sXML);
  }

  @Nonnull
  private static IdentifierType _createIdentifier (final String sValue) {
    final IdentifierType ret = new IdentifierType ();
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  private static IdentifierType _createIdentifier (final String sSchemeID, final String sValue) {
    final IdentifierType ret = new IdentifierType ();
    ret.setSchemeID (sSchemeID);
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  private static IndicatorType _createIndicator (final boolean bValue) {
    final IndicatorType ret = new IndicatorType ();
    ret.setValue (bValue);
    return ret;
  }

  @Nonnull
  private static TextType _createText (final String sValue) {
    final TextType ret = new TextType ();
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  private static CodeType _createCode (final String sValue) {
    final CodeType ret = new CodeType ();
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  private static CodeType _createCode (final String sSchemeID, final String sValue) {
    final CodeType ret = new CodeType ();
    ret.setName (sSchemeID);
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  private static TDECodeTypeTwoUppercaseLetters _createCountry (final String sCountryCode) {
    final TDECodeTypeTwoUppercaseLetters ret = new TDECodeTypeTwoUppercaseLetters ();
    ret.setValue (sCountryCode);
    return ret;
  }

  @Test
  public void testCreateRequestFromScratch () {
    final TDETOOPDataRequestType r = new TDETOOPDataRequestType ();
    r.setDocumentUniversalUniqueIdentifier (_createIdentifier (UUID.randomUUID ().toString ()));
    r.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    r.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    r.setCopyIndicator (_createIndicator (false));
    // Document type ID
    r.setDocumentTypeCode (_createCode ("toop-doctypeid", "data.request.registeredorganization"));
    r.setSpecificationIdentifier (_createIdentifier ("bla"));
    // Process ID
    r.setProcessIdentifier (_createIdentifier ("toop-procid", "urn:toop:www.toop.eu/data-request"));
    r.setDataConsumerDocumentIdentifier (_createIdentifier ("DC-ID-17"));
    r.setDataRequestIdentifier (_createIdentifier ("bla"));
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (_createIdentifier ("ATU12345678"));
      aDC.setDCName (_createText ("Helger Enterprises"));
      // Sender participant ID
      aDC.setDCElectronicAddressIdentifier (_createIdentifier ("iso6523-actorid-upis", "9915:test"));
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (_createCountry ("AT"));
      aDC.setDCLegalAddress (aAddress);
      r.setDataConsumer (aDC);
    }
    {
      final TDEDataSubjectType aDS = new TDEDataSubjectType ();
      aDS.setDataSubjectTypeCode (_createCode ("12345"));
      {
        final TDENaturalPersonType aNP = new TDENaturalPersonType ();
        aNP.setPersonIdentifier (_createIdentifier ("bla"));
        aNP.setFamilyName (_createText ("Helger"));
        aNP.setFirstName (_createText ("Philip"));
        aNP.setBirthDate (PDTXMLConverter.getXMLCalendarDateNow ());
        final TDEAddressType aAddress = new TDEAddressType ();
        // Destination country to use
        aAddress.setCountryCode (_createCountry ("DE"));
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
      aReq.setDataElementRequestIdentifier (_createIdentifier ("bla"));
      {
        final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
        aSrcConcept.setConceptType (_createCode ("DC"));
        aSrcConcept.setSemanticMappingExecutionIndicator (_createIndicator (false));
        aSrcConcept.setConceptNamespace (_createIdentifier ("elUri"));
        aSrcConcept.addConcept (_createText ("elType"));

        {
          final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
          aToopConcept.setConceptType (_createCode ("TOOP"));
          aToopConcept.setSemanticMappingExecutionIndicator (_createIndicator (false));
          aToopConcept.setConceptNamespace (_createIdentifier ("toopUri"));
          aToopConcept.addConcept (_createText ("toopType"));
          aSrcConcept.addConceptRequest (aToopConcept);
        }
        aReq.setConceptRequest (aSrcConcept);
      }

      r.addDataElementRequest (aReq);
    }

    final Document aDoc = ToopWriter.dataRequest ().getAsDocument (r);
    assertNotNull (aDoc);
    if (false) {
      final MapBasedNamespaceContext aCtx = new MapBasedNamespaceContext ();
      aCtx.addMapping ("toop", ObjectFactory._TOOPDataRequest_QNAME.getNamespaceURI ());
      System.out.println (XMLWriter.getNodeAsString (aDoc, new XMLWriterSettings ().setNamespaceContext (aCtx)));
    }
  }
}
