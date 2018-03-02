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

import eu.toop.commons.concept.ConceptValue;
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
import eu.toop.commons.jaxb.ToopReader;
import eu.toop.commons.jaxb.ToopWriter;
import eu.toop.commons.jaxb.ToopXSDHelper;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.BinaryObjectType;

@Immutable
public final class ToopMessageBuilder {
  public static final String ENTRY_NAME_TOOP_DATA_REQUEST = "TOOPDataRequest";
  public static final String ENTRY_NAME_TOOP_DATA_RESPONSE = "TOOPDataResponse";

  private static final Logger s_aLogger = LoggerFactory.getLogger (ToopMessageBuilder.class);

  private ToopMessageBuilder () {
  }

  public static void createRequestMessage (@Nonnull final TDETOOPDataRequestType aRequest,
                                           @Nonnull final OutputStream aOS,
                                           @Nonnull final SignatureHelper aSigHelper) throws IOException, IllegalStateException {
    ValueEnforcer.notNull (aRequest, "Request");
    ValueEnforcer.notNull (aOS, "ArchiveOutput");
    ValueEnforcer.notNull (aSigHelper, "SignatureHelper");

    final AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory ();
    final IAsicWriter asicWriter = asicWriterFactory.newContainer (aOS);
    {
      final byte[] aXML = ToopWriter.dataRequest ().getAsBytes (aRequest);
      asicWriter.add (new NonBlockingByteArrayInputStream (aXML), ENTRY_NAME_TOOP_DATA_REQUEST,
                      CMimeType.APPLICATION_XML);
    }
    asicWriter.sign (aSigHelper);
    s_aLogger.info ("Successfully created request ASiC");
  }

  public static void createResponseMessage (@Nonnull final TDETOOPDataResponseType aResponse,
                                            @Nonnull final OutputStream aOS,
                                            @Nonnull final SignatureHelper aSigHelper) throws IOException, IllegalStateException {
    ValueEnforcer.notNull (aResponse, "Response");
    ValueEnforcer.notNull (aOS, "ArchiveOutput");
    ValueEnforcer.notNull (aSigHelper, "SignatureHelper");

    final AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory ();
    final IAsicWriter asicWriter = asicWriterFactory.newContainer (aOS);
    {
      final byte[] aXML = ToopWriter.dataResponse ().getAsBytes (aResponse);
      asicWriter.add (new NonBlockingByteArrayInputStream (aXML), ENTRY_NAME_TOOP_DATA_RESPONSE,
                      CMimeType.APPLICATION_XML);
    }
    asicWriter.sign (aSigHelper);
    s_aLogger.info ("Successfully created response ASiC");
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPDataRequestType}.
   *
   * @param aIS
   *          Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPDataRequestType} every time the method is called or
   *         <code>null</code> if none is contained in the ASIC.
   * @throws IOException
   *           In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static TDETOOPDataRequestType parseRequestMessage (@Nonnull @WillClose final InputStream aIS) throws IOException {
    ValueEnforcer.notNull (aIS, "archiveInput");

    final Object o = parseRequestOrResponse (aIS);
    if (o instanceof TDETOOPDataRequestType)
      return (TDETOOPDataRequestType) o;

    return null;
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPDataResponseType}.
   *
   * @param aIS
   *          Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPDataResponseType} every time the method is called
   *         or <code>null</code> if none is contained in the ASIC.
   * @throws IOException
   *           In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static TDETOOPDataResponseType parseResponseMessage (@Nonnull @WillClose final InputStream aIS) throws IOException {
    ValueEnforcer.notNull (aIS, "archiveInput");

    final Object o = parseRequestOrResponse (aIS);
    if (o instanceof TDETOOPDataResponseType)
      return (TDETOOPDataResponseType) o;

    return null;
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPDataRequestType} or {@link TDETOOPDataResponseType}.
   *
   * @param aIS
   *          Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPDataRequestType} or {@link TDETOOPDataResponseType}
   *         every time the method is called or <code>null</code> if none is
   *         contained in the ASIC.
   * @throws IOException
   *           In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static Object parseRequestOrResponse (@Nonnull @WillClose final InputStream aIS) throws IOException {
    ValueEnforcer.notNull (aIS, "archiveInput");

    try (final IAsicReader asicReader = AsicReaderFactory.newFactory ().open (aIS)) {
      String entryName;
      while ((entryName = asicReader.getNextFile ()) != null) {
        if (entryName.equals (ENTRY_NAME_TOOP_DATA_REQUEST)) {
          try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ()) {
            asicReader.writeFile (aBAOS);
            return ToopReader.dataRequest ().read (aBAOS.getAsInputStream ());
          }
        }
        if (entryName.equals (ENTRY_NAME_TOOP_DATA_RESPONSE)) {
          try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ()) {
            asicReader.writeFile (aBAOS);
            return ToopReader.dataResponse ().read (aBAOS.getAsInputStream ());
          }
        }
      }
    }

    return null;
  }

  @Nonnull
  public static TDETOOPDataRequestType createMockRequest (@Nonnull @Nonempty final String sSenderParticipantID,
                                                          @Nonnull @Nonempty final String sCountryCode,
                                                          @Nonnull @Nonempty final String sDocumentTypeID,
                                                          @Nonnull @Nonempty final String sProcessID,
                                                          @Nullable final Iterable<? extends ConceptValue> aValues) {
    ValueEnforcer.notEmpty (sSenderParticipantID, "SenderParticipantID");
    ValueEnforcer.notEmpty (sCountryCode, "CountryCode");
    ValueEnforcer.notEmpty (sDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notEmpty (sProcessID, "ProcessID");

    final TDETOOPDataRequestType r = new TDETOOPDataRequestType ();
    r.setDocumentUniversalUniqueIdentifier (ToopXSDHelper.createIdentifier (UUID.randomUUID ().toString ()));
    r.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    r.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    r.setCopyIndicator (ToopXSDHelper.createIndicator (false));
    // Document type ID
    r.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier ("toop-doctypeid", sDocumentTypeID));
    r.setSpecificationIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    // Process ID
    r.setProcessIdentifier (ToopXSDHelper.createIdentifier ("toop-procid", sProcessID));
    r.setDataConsumerDocumentIdentifier (ToopXSDHelper.createIdentifier ("DC-ID-17"));
    r.setDataRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper.createIdentifier ("ATU12345678"));
      aDC.setDCName (ToopXSDHelper.createText ("Helger Enterprises"));
      // Sender participant ID
      aDC.setDCElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("iso6523-actorid-upis",
                                                                            sSenderParticipantID));
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
        aAddress.setCountryCode (ToopXSDHelper.createCode (sCountryCode));
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

    for (final ConceptValue aCV : aValues) {
      final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
      aReq.setDataElementRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
      {
        final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
        aSrcConcept.setConceptTypeCode (ToopXSDHelper.createCode ("DC"));
        aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
        aSrcConcept.setConceptNamespace (ToopXSDHelper.createIdentifier (aCV.getNamespace ()));
        aSrcConcept.setConceptName (ToopXSDHelper.createText (aCV.getValue ()));
        aReq.setConceptRequest (aSrcConcept);
      }

      r.addDataElementRequest (aReq);
    }
    return r;
  }

  @Nonnull
  public static TDETOOPDataResponseType createMockResponse (@Nonnull @Nonempty final String sSenderParticipantID,
                                                            @Nonnull @Nonempty final String sCountryCode,
                                                            @Nonnull @Nonempty final String sDocumentTypeID,
                                                            @Nonnull @Nonempty final String sProcessID,
                                                            @Nullable final Iterable<? extends ConceptValue> aValues) {
    ValueEnforcer.notEmpty (sSenderParticipantID, "SenderParticipantID");
    ValueEnforcer.notEmpty (sCountryCode, "CountryCode");
    ValueEnforcer.notEmpty (sDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notEmpty (sProcessID, "ProcessID");

    final TDETOOPDataResponseType r = new TDETOOPDataResponseType ();
    r.setDocumentUniversalUniqueIdentifier (ToopXSDHelper.createIdentifier (UUID.randomUUID ().toString ()));
    r.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    r.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    r.setCopyIndicator (ToopXSDHelper.createIndicator (false));
    // Document type ID
    r.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier ("toop-doctypeid", sDocumentTypeID));
    r.setSpecificationIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    // Process ID
    r.setProcessIdentifier (ToopXSDHelper.createIdentifier ("toop-procid", sProcessID));
    r.setDataConsumerDocumentIdentifier (ToopXSDHelper.createIdentifier ("DC-ID-17"));
    r.setDataRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper.createIdentifier ("ATU12345678"));
      aDC.setDCName (ToopXSDHelper.createText ("Helger Enterprises"));
      // Sender participant ID
      aDC.setDCElectronicAddressIdentifier (ToopXSDHelper.createIdentifier ("iso6523-actorid-upis",
                                                                            sSenderParticipantID));
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
        aAddress.setCountryCode (ToopXSDHelper.createCode (sCountryCode));
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

    for (final ConceptValue aCV : aValues) {
      final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
      aReq.setDataElementRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
      {
        final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
        aSrcConcept.setConceptTypeCode (ToopXSDHelper.createCode ("DC"));
        aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
        aSrcConcept.setConceptNamespace (ToopXSDHelper.createIdentifier (aCV.getNamespace ()));
        aSrcConcept.setConceptName (ToopXSDHelper.createText (aCV.getValue ()));
        {
          final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
          aToopConcept.setConceptTypeCode (ToopXSDHelper.createCode ("TOOP"));
          aToopConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
          aToopConcept.setConceptNamespace (ToopXSDHelper.createIdentifier (aCV.getNamespace () + "-toop"));
          aToopConcept.setConceptName (ToopXSDHelper.createText ("toop." + aCV.getValue ()));
          {
            final TDEConceptRequestType aDPConcept = new TDEConceptRequestType ();
            aDPConcept.setConceptTypeCode (ToopXSDHelper.createCode ("TOOP"));
            aDPConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
            aDPConcept.setConceptNamespace (ToopXSDHelper.createIdentifier (aCV.getNamespace () + "-dp"));
            aDPConcept.setConceptName (ToopXSDHelper.createText ("dp." + aCV.getValue ()));
            aToopConcept.addConceptRequest (aDPConcept);
          }
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

    return r;
  }
}
