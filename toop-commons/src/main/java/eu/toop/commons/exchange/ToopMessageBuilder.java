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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.asic.AsicReaderFactory;
import com.helger.asic.AsicWriterFactory;
import com.helger.asic.IAsicReader;
import com.helger.asic.IAsicWriter;
import com.helger.asic.SignatureHelper;
import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.mime.CMimeType;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.concept.EConceptType;
import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEConceptRequestType;
import eu.toop.commons.dataexchange.TDEDataConsumerType;
import eu.toop.commons.dataexchange.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.TDEDataProviderType;
import eu.toop.commons.dataexchange.TDEDataRequestAuthorizationType;
import eu.toop.commons.dataexchange.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.TDENaturalPersonType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import eu.toop.commons.dataexchange.TDETOOPResponseType;
import eu.toop.commons.jaxb.ToopReader;
import eu.toop.commons.jaxb.ToopWriter;
import eu.toop.commons.jaxb.ToopXSDHelper;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.BinaryObjectType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

@Immutable
public final class ToopMessageBuilder {
  private static final String ENTRY_NAME_TOOP_DATA_REQUEST = "TOOPRequest";
  private static final String ENTRY_NAME_TOOP_DATA_RESPONSE = "TOOPResponse";

  private static final Logger s_aLogger = LoggerFactory.getLogger (ToopMessageBuilder.class);

  private ToopMessageBuilder () {
  }

  public static void createRequestMessage (@Nonnull final TDETOOPRequestType aRequest, @Nonnull final OutputStream aOS,
                                           @Nonnull final SignatureHelper aSigHelper) throws IOException {
    ValueEnforcer.notNull (aRequest, "Request");
    ValueEnforcer.notNull (aOS, "ArchiveOutput");
    ValueEnforcer.notNull (aSigHelper, "SignatureHelper");

    final AsicWriterFactory aAsicWriterFactory = AsicWriterFactory.newFactory ();
    final IAsicWriter aAsicWriter = aAsicWriterFactory.newContainer (aOS);
    {
      final byte[] aXML = ToopWriter.request ().getAsBytes (aRequest);
      aAsicWriter.add (new NonBlockingByteArrayInputStream (aXML), ENTRY_NAME_TOOP_DATA_REQUEST,
                       CMimeType.APPLICATION_XML);
    }
    aAsicWriter.sign (aSigHelper);
    s_aLogger.info ("Successfully created request ASiC");
  }

  public static void createResponseMessage (@Nonnull final TDETOOPResponseType aResponse,
                                            @Nonnull final OutputStream aOS,
                                            @Nonnull final SignatureHelper aSigHelper) throws IOException {
    ValueEnforcer.notNull (aResponse, "Response");
    ValueEnforcer.notNull (aOS, "ArchiveOutput");
    ValueEnforcer.notNull (aSigHelper, "SignatureHelper");

    final AsicWriterFactory aAsicWriterFactory = AsicWriterFactory.newFactory ();
    final IAsicWriter aAsicWriter = aAsicWriterFactory.newContainer (aOS);
    {
      final byte[] aXML = ToopWriter.response ().getAsBytes (aResponse);
      aAsicWriter.add (new NonBlockingByteArrayInputStream (aXML), ENTRY_NAME_TOOP_DATA_RESPONSE,
                       CMimeType.APPLICATION_XML);
    }
    aAsicWriter.sign (aSigHelper);
    s_aLogger.info ("Successfully created response ASiC");
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPRequestType}.
   *
   * @param aIS
   *          Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPRequestType} every time the method is called or
   *         <code>null</code> if none is contained in the ASIC.
   * @throws IOException
   *           In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static TDETOOPRequestType parseRequestMessage (@Nonnull @WillClose final InputStream aIS) throws IOException {
    ValueEnforcer.notNull (aIS, "archiveInput");

    final Object aObj = parseRequestOrResponse (aIS);
    if (aObj instanceof TDETOOPRequestType)
      return (TDETOOPRequestType) aObj;

    return null;
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPResponseType}.
   *
   * @param aIS
   *          Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPResponseType} every time the method is called or
   *         <code>null</code> if none is contained in the ASIC.
   * @throws IOException
   *           In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static TDETOOPResponseType parseResponseMessage (@Nonnull @WillClose final InputStream aIS) throws IOException {
    ValueEnforcer.notNull (aIS, "archiveInput");

    final Object aObj = parseRequestOrResponse (aIS);
    if (aObj instanceof TDETOOPResponseType)
      return (TDETOOPResponseType) aObj;

    return null;
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPRequestType} or {@link TDETOOPResponseType}.
   *
   * @param aIS
   *          Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPRequestType} or {@link TDETOOPResponseType} every
   *         time the method is called or <code>null</code> if none is contained
   *         in the ASIC.
   * @throws IOException
   *           In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static Object parseRequestOrResponse (@Nonnull @WillClose final InputStream aIS) throws IOException {
    ValueEnforcer.notNull (aIS, "archiveInput");

    try (final IAsicReader aAsicReader = AsicReaderFactory.newFactory ().open (aIS)) {
      String sEntryName;
      while ((sEntryName = aAsicReader.getNextFile ()) != null) {
        if (sEntryName.equals (ENTRY_NAME_TOOP_DATA_REQUEST)) {
          try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ()) {
            aAsicReader.writeFile (aBAOS);
            return ToopReader.request ().read (aBAOS.getAsInputStream ());
          }
        }
        if (sEntryName.equals (ENTRY_NAME_TOOP_DATA_RESPONSE)) {
          try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ()) {
            aAsicReader.writeFile (aBAOS);
            return ToopReader.response ().read (aBAOS.getAsInputStream ());
          }
        }
      }
    }

    return null;
  }

  @Nonnull
  public static TDEDataRequestSubjectType createMockDataRequestSubject (@Nonnull @Nonempty final String sCountryCode) {
    final TDEDataRequestSubjectType aRet = new TDEDataRequestSubjectType ();
    aRet.setDataRequestSubjectTypeCode (ToopXSDHelper.createCode ("12345"));
    {
      final TDENaturalPersonType aNP = new TDENaturalPersonType ();
      aNP.setPersonIdentifier (ToopXSDHelper.createIdentifier ("bla"));
      aNP.setFamilyName (ToopXSDHelper.createText ("Helger"));
      aNP.setFirstName (ToopXSDHelper.createText ("Philip"));
      aNP.setBirthDate (PDTXMLConverter.getXMLCalendarDateNow ());
      final TDEAddressType aAddress = new TDEAddressType ();
      // Destination country to use
      aAddress.setCountryCode (ToopXSDHelper.createCode (sCountryCode));
      aNP.setNaturalPersonLegalAddress (aAddress);
      aRet.setNaturalPerson (aNP);
    }
    return aRet;
  }

  @Nonnull
  public static TDETOOPRequestType createMockRequest (@Nonnull final TDEDataRequestSubjectType aRequestSubject,
                                                      @Nonnull final IdentifierType aSenderParticipantID,
                                                      @Nonnull @Nonempty final String sCountryCode,
                                                      @Nonnull final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
                                                      @Nonnull final EPredefinedProcessIdentifier eProcessID,
                                                      @Nullable final Iterable<? extends ConceptValue> aValues) {
    ValueEnforcer.notNull (aRequestSubject, "RequestSubject");
    ValueEnforcer.notNull (aSenderParticipantID, "SenderParticipantID");
    ValueEnforcer.notEmpty (sCountryCode, "CountryCode");
    ValueEnforcer.notNull (eDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notNull (eProcessID, "ProcessID");

    final TDETOOPRequestType aRet = new TDETOOPRequestType ();
    aRet.setDocumentUniversalUniqueIdentifier (ToopXSDHelper.createIdentifier (UUID.randomUUID ().toString ()));
    aRet.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    aRet.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    aRet.setCopyIndicator (ToopXSDHelper.createIndicator (false));
    // Document type ID
    aRet.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier (eDocumentTypeID.getScheme (),
                                                                    eDocumentTypeID.getID ()));
    aRet.setSpecificationIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    // Process ID
    aRet.setProcessIdentifier (ToopXSDHelper.createIdentifier (eProcessID.getScheme (), eProcessID.getID ()));
    aRet.setDataConsumerDocumentIdentifier (ToopXSDHelper.createIdentifier ("DC-ID-17"));
    aRet.setDataRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper.createIdentifier ("ATU12345678"));
      aDC.setDCName (ToopXSDHelper.createText ("Helger Enterprises"));
      // Sender participant ID
      aDC.setDCElectronicAddressIdentifier (aSenderParticipantID.clone ());
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper.createCode ("AT"));
      aDC.setDCLegalAddress (aAddress);
      aRet.setDataConsumer (aDC);
    }
    {
      aRet.setDataRequestSubject (aRequestSubject);
    }
    {
      final TDEDataRequestAuthorizationType aAuth = new TDEDataRequestAuthorizationType ();
      final BinaryObjectType aBO = new BinaryObjectType ();
      aBO.setValue ("11101010101010001110101".getBytes (StandardCharsets.ISO_8859_1));
      aBO.setMimeCode ("application/octet-stream");
      aAuth.setDataRequestConsentToken (aBO);
      aRet.setDataRequestAuthorization (aAuth);
    }

    for (final ConceptValue aCV : aValues) {
      final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
      aReq.setDataElementRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
      {
        final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
        aSrcConcept.setConceptTypeCode (ToopXSDHelper.createCode (EConceptType.DC.getID ()));
        aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
        aSrcConcept.setConceptNamespace (ToopXSDHelper.createIdentifier (aCV.getNamespace ()));
        aSrcConcept.setConceptName (ToopXSDHelper.createText (aCV.getValue ()));
        aReq.setConceptRequest (aSrcConcept);
      }

      aRet.addDataElementRequest (aReq);
    }
    return aRet;
  }

  @Nonnull
  public static TDETOOPResponseType createMockResponse (@Nonnull final IdentifierType aSenderParticipantID,
                                                        @Nonnull @Nonempty final String sCountryCode,
                                                        @Nonnull final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
                                                        @Nonnull final EPredefinedProcessIdentifier eProcessID,
                                                        @Nullable final Iterable<? extends ConceptValue> aValues) {
    ValueEnforcer.notNull (aSenderParticipantID, "SenderParticipantID");
    ValueEnforcer.notEmpty (sCountryCode, "CountryCode");
    ValueEnforcer.notNull (eDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notNull (eProcessID, "ProcessID");

    final TDETOOPResponseType aRet = new TDETOOPResponseType ();
    aRet.setDocumentUniversalUniqueIdentifier (ToopXSDHelper.createIdentifier (UUID.randomUUID ().toString ()));
    aRet.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    aRet.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    aRet.setCopyIndicator (ToopXSDHelper.createIndicator (false));
    // Document type ID
    aRet.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier (eDocumentTypeID.getScheme (),
                                                                    eDocumentTypeID.getID ()));
    aRet.setSpecificationIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    // Process ID
    aRet.setProcessIdentifier (ToopXSDHelper.createIdentifier (eProcessID.getScheme (), eProcessID.getID ()));
    aRet.setDataConsumerDocumentIdentifier (ToopXSDHelper.createIdentifier ("DC-ID-17"));
    aRet.setDataRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper.createIdentifier ("ATU12345678"));
      aDC.setDCName (ToopXSDHelper.createText ("Helger Enterprises"));
      // Sender participant ID
      aDC.setDCElectronicAddressIdentifier (aSenderParticipantID.clone ());
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper.createCode ("AT"));
      aDC.setDCLegalAddress (aAddress);
      aRet.setDataConsumer (aDC);
    }
    {
      final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
      aDS.setDataRequestSubjectTypeCode (ToopXSDHelper.createCode ("12345"));
      {
        final TDENaturalPersonType aNP = new TDENaturalPersonType ();
        aNP.setPersonIdentifier (ToopXSDHelper.createIdentifier ("bla"));
        aNP.setFamilyName (ToopXSDHelper.createText ("Helger"));
        aNP.setFirstName (ToopXSDHelper.createText ("Philip"));
        aNP.setBirthDate (PDTXMLConverter.getXMLCalendarDateNow ());
        final TDEAddressType aAddress = new TDEAddressType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper.createCode (sCountryCode));
        aNP.setNaturalPersonLegalAddress (aAddress);
        aDS.setNaturalPerson (aNP);
      }
      aRet.setDataRequestSubject (aDS);
    }
    {
      final TDEDataRequestAuthorizationType aAuth = new TDEDataRequestAuthorizationType ();
      final BinaryObjectType aBO = new BinaryObjectType ();
      aBO.setValue ("11101010101010001110101".getBytes (StandardCharsets.ISO_8859_1));
      aBO.setMimeCode ("application/octet-stream");
      aAuth.setDataRequestConsentToken (aBO);
      aRet.setDataRequestAuthorization (aAuth);
    }

    for (final ConceptValue aCV : aValues) {
      final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
      aReq.setDataElementRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
      {
        final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
        aSrcConcept.setConceptTypeCode (ToopXSDHelper.createCode (EConceptType.DC.getID ()));
        aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
        aSrcConcept.setConceptNamespace (ToopXSDHelper.createIdentifier (aCV.getNamespace ()));
        aSrcConcept.setConceptName (ToopXSDHelper.createText (aCV.getValue ()));
        {
          final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
          aToopConcept.setConceptTypeCode (ToopXSDHelper.createCode (EConceptType.TC.getID ()));
          aToopConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
          aToopConcept.setConceptNamespace (ToopXSDHelper.createIdentifier (aCV.getNamespace () + "-toop"));
          aToopConcept.setConceptName (ToopXSDHelper.createText ("toop." + aCV.getValue ()));
          {
            final TDEConceptRequestType aDPConcept = new TDEConceptRequestType ();
            aDPConcept.setConceptTypeCode (ToopXSDHelper.createCode (EConceptType.DP.getID ()));
            aDPConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
            aDPConcept.setConceptNamespace (ToopXSDHelper.createIdentifier (aCV.getNamespace () + "-dp"));
            aDPConcept.setConceptName (ToopXSDHelper.createText ("dp." + aCV.getValue ()));
            aToopConcept.addConceptRequest (aDPConcept);
          }
          aSrcConcept.addConceptRequest (aToopConcept);
        }
        aReq.setConceptRequest (aSrcConcept);
      }

      aRet.addDataElementRequest (aReq);
    }

    {
      final TDEDataProviderType aDP = new TDEDataProviderType ();
      aDP.setDPIdentifier (ToopXSDHelper.createIdentifier ("atbla"));
      aDP.setDPName (ToopXSDHelper.createText ("Register1"));
      aDP.setDPElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("me@register.example.org"));
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper.createCode ("XK"));
      aDP.setDPLegalAddress (aAddress);
      aRet.setDataProvider (aDP);
    }

    return aRet;
  }

  /**
   * Create a new response with all cloned values from the request.
   *
   * @param aRequest
   *          Source request. May not be <code>null</code>.
   * @return Destination response. Never <code>null</code>.
   */
  @Nonnull
  public static TDETOOPResponseType createResponse (@Nonnull final TDETOOPRequestType aRequest) {
    final TDETOOPResponseType aResponse = new TDETOOPResponseType ();
    aRequest.cloneTo (aResponse);
    // Response specific stuff stays null
    return aResponse;
  }
}
