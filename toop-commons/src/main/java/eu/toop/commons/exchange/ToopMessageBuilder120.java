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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
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
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.string.StringHelper;
import com.helger.commons.text.IMultilingualText;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.concept.EConceptType;
import eu.toop.commons.dataexchange.v120.TDEAddressType;
import eu.toop.commons.dataexchange.v120.TDEConceptRequestType;
import eu.toop.commons.dataexchange.v120.TDEDataConsumerType;
import eu.toop.commons.dataexchange.v120.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.v120.TDEDataProviderType;
import eu.toop.commons.dataexchange.v120.TDEDataRequestAuthorizationType;
import eu.toop.commons.dataexchange.v120.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v120.TDEDocumentRequestType;
import eu.toop.commons.dataexchange.v120.TDEErrorType;
import eu.toop.commons.dataexchange.v120.TDELegalEntityType;
import eu.toop.commons.dataexchange.v120.TDENaturalPersonType;
import eu.toop.commons.dataexchange.v120.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v120.TDETOOPResponseType;
import eu.toop.commons.error.EToopErrorCategory;
import eu.toop.commons.error.EToopErrorCode;
import eu.toop.commons.error.EToopErrorOrigin;
import eu.toop.commons.error.EToopErrorSeverity;
import eu.toop.commons.error.IToopErrorCode;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.jaxb.ToopReader;
import eu.toop.commons.jaxb.ToopWriter;
import eu.toop.commons.jaxb.ToopXSDHelper120;
import eu.toop.commons.usecase.EToopEntityType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.BinaryObjectType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

/**
 * A helper class to build TOOP data model 1.2.0 stuff.
 *
 * @author Philip Helger
 * @since 0.10.0
 */
@Immutable
@Deprecated
public final class ToopMessageBuilder120
{
  public static final ClassPathResource TOOP_XSD = new ClassPathResource ("/xsd/toop/TOOP_DataExchange-1.2.0.xsd",
                                                                          ToopMessageBuilder120.class.getClassLoader ());
  private static final String ENTRY_NAME_TOOP_DATA_REQUEST = "TOOPRequest";
  private static final String ENTRY_NAME_TOOP_DATA_RESPONSE = "TOOPResponse";

  private static final Logger LOGGER = LoggerFactory.getLogger (ToopMessageBuilder120.class);

  private ToopMessageBuilder120 ()
  {}

  public static void createRequestMessageAsic (@Nonnull final TDETOOPRequestType aRequest,
                                               @Nonnull final OutputStream aOS,
                                               @Nonnull final SignatureHelper aSigHelper) throws ToopErrorException
  {
    ValueEnforcer.notNull (aRequest, "Request");
    ValueEnforcer.notNull (aOS, "ArchiveOutput");
    ValueEnforcer.notNull (aSigHelper, "SignatureHelper");

    final AsicWriterFactory aAsicWriterFactory = AsicWriterFactory.newFactory ();
    try
    {
      final IAsicWriter aAsicWriter = aAsicWriterFactory.newContainer (aOS);
      final byte [] aXML = ToopWriter.request120 ().getAsBytes (aRequest);
      if (aXML == null)
        throw new ToopErrorException ("Error marshalling the TOOP Request", EToopErrorCode.TC_001);

      aAsicWriter.add (new NonBlockingByteArrayInputStream (aXML),
                       ENTRY_NAME_TOOP_DATA_REQUEST,
                       CMimeType.APPLICATION_XML);
      aAsicWriter.sign (aSigHelper);
      LOGGER.info ("Successfully created request ASiC");
    }
    catch (final IOException ex)
    {
      throw new ToopErrorException ("Error creating signed ASIC container", ex, EToopErrorCode.TC_001);
    }
  }

  public static void createResponseMessageAsic (@Nonnull final TDETOOPResponseType aResponse,
                                                @Nonnull final OutputStream aOS,
                                                @Nonnull final SignatureHelper aSigHelper) throws ToopErrorException
  {
    ValueEnforcer.notNull (aResponse, "Response");
    ValueEnforcer.notNull (aOS, "ArchiveOutput");
    ValueEnforcer.notNull (aSigHelper, "SignatureHelper");

    final AsicWriterFactory aAsicWriterFactory = AsicWriterFactory.newFactory ();
    try
    {
      final IAsicWriter aAsicWriter = aAsicWriterFactory.newContainer (aOS);
      final byte [] aXML = ToopWriter.response120 ().getAsBytes (aResponse);
      if (aXML == null)
        throw new ToopErrorException ("Error marshalling the TOOP Response", EToopErrorCode.TC_001);

      aAsicWriter.add (new NonBlockingByteArrayInputStream (aXML),
                       ENTRY_NAME_TOOP_DATA_RESPONSE,
                       CMimeType.APPLICATION_XML);
      aAsicWriter.sign (aSigHelper);
      LOGGER.info ("Successfully created response ASiC");
    }
    catch (final IOException ex)
    {
      throw new ToopErrorException ("Error creating signed ASIC container", ex, EToopErrorCode.TC_001);
    }
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPRequestType}.
   *
   * @param aIS
   *        Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPRequestType} every time the method is called or
   *         <code>null</code> if none is contained in the ASIC.
   * @throws IOException
   *         In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static TDETOOPRequestType parseRequestMessage (@Nonnull @WillClose final InputStream aIS) throws IOException
  {
    ValueEnforcer.notNull (aIS, "archiveInput");

    final Serializable aObj = parseRequestOrResponse (aIS);
    if (aObj instanceof TDETOOPRequestType)
      return (TDETOOPRequestType) aObj;

    return null;
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPResponseType}.
   *
   * @param aIS
   *        Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPResponseType} every time the method is called or
   *         <code>null</code> if none is contained in the ASIC.
   * @throws IOException
   *         In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static TDETOOPResponseType parseResponseMessage (@Nonnull @WillClose final InputStream aIS) throws IOException
  {
    ValueEnforcer.notNull (aIS, "archiveInput");

    final Serializable aObj = parseRequestOrResponse (aIS);
    if (aObj instanceof TDETOOPResponseType)
      return (TDETOOPResponseType) aObj;

    return null;
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link TDETOOPRequestType} or {@link TDETOOPResponseType}.
   *
   * @param aIS
   *        Input stream to read from. May not be <code>null</code>.
   * @return New {@link TDETOOPRequestType} or {@link TDETOOPResponseType}.
   *         every time the method is called or <code>null</code> if none is
   *         contained in the ASIC.
   * @throws IOException
   *         In case of IO error
   */
  @Nullable
  @ReturnsMutableObject
  public static Serializable parseRequestOrResponse (@Nonnull @WillClose final InputStream aIS) throws IOException
  {
    ValueEnforcer.notNull (aIS, "archiveInput");

    try (final IAsicReader aAsicReader = AsicReaderFactory.newFactory ().open (aIS))
    {
      String sEntryName;
      while ((sEntryName = aAsicReader.getNextFile ()) != null)
      {
        if (sEntryName.equals (ENTRY_NAME_TOOP_DATA_REQUEST))
        {
          try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
          {
            aAsicReader.writeFile (aBAOS);
            return ToopReader.request120 ().read (aBAOS.getAsInputStream ());
          }
        }
        if (sEntryName.equals (ENTRY_NAME_TOOP_DATA_RESPONSE))
        {
          try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
          {
            aAsicReader.writeFile (aBAOS);
            return ToopReader.response120 ().read (aBAOS.getAsInputStream ());
          }
        }
      }
    }

    return null;
  }

  @Deprecated
  @Nonnull
  public static TDEAddressType createMockAddressType (@Nonnull @Nonempty final String sCountryCode)
  {
    final TDEAddressType aAddress = new TDEAddressType ();
    aAddress.addAddressLine (ToopXSDHelper120.createText ("Hintere Zollamtstraße 4"));
    aAddress.addAddressLine (ToopXSDHelper120.createText ("1030 Wien"));
    aAddress.setStreetName (ToopXSDHelper120.createText ("Hintere Zollamtstraße"));
    aAddress.setStreetNumber (ToopXSDHelper120.createText ("4"));
    aAddress.setCity (ToopXSDHelper120.createText ("Wien"));
    aAddress.setPostCode (ToopXSDHelper120.createText ("1030"));
    // Destination country to use
    aAddress.setCountryCode (ToopXSDHelper120.createCode (sCountryCode));
    return aAddress;
  }

  @Deprecated
  @Nonnull
  public static TDEDataRequestSubjectType createMockDataRequestSubject (@Nonnull @Nonempty final String sSrcCountryCode,
                                                                        @Nonnull @Nonempty final String sDstCountryCode,
                                                                        final boolean bLegalPerson,
                                                                        @Nonnull final String sUniqueIdentifier)
  {
    final TDEDataRequestSubjectType aRet = new TDEDataRequestSubjectType ();
    if (bLegalPerson)
    {
      // Codelist value (legacy)
      aRet.setDataRequestSubjectTypeCode (ToopXSDHelper120.createCode (EToopEntityType.LEGAL_ENTITY.getID ()));

      final TDELegalEntityType aLE = new TDELegalEntityType ();
      aLE.setLegalPersonUniqueIdentifier (ToopXSDHelper120.createIdentifier (sSrcCountryCode +
                                                                             "/" +
                                                                             sDstCountryCode +
                                                                             "/" +
                                                                             sUniqueIdentifier));
      aLE.setLegalName (ToopXSDHelper120.createText ("ACME Inc."));
      aLE.setLegalEntityLegalAddress (createMockAddressType (sDstCountryCode));
      aRet.setLegalEntity (aLE);
    }
    else
    {
      // Codelist value
      aRet.setDataRequestSubjectTypeCode (ToopXSDHelper120.createCode (EToopEntityType.NATURAL_PERSON.getID ()));

      final TDENaturalPersonType aNP = new TDENaturalPersonType ();
      aNP.setPersonIdentifier (ToopXSDHelper120.createIdentifier (sSrcCountryCode +
                                                                  "/" +
                                                                  sDstCountryCode +
                                                                  "/" +
                                                                  sUniqueIdentifier));
      aNP.setFamilyName (ToopXSDHelper120.createText ("Helger"));
      aNP.setFirstName (ToopXSDHelper120.createText ("Philip"));
      aNP.setBirthDate (PDTXMLConverter.getXMLCalendarDateNow ());
      aNP.setNaturalPersonLegalAddress (createMockAddressType (sDstCountryCode));
      aRet.setNaturalPerson (aNP);
    }
    return aRet;
  }

  private static void _fillRequest (@Nonnull final TDETOOPRequestType aRet,
                                    @Nonnull final TDEDataRequestSubjectType aRequestSubject,
                                    @Nonnull final String sDCCountryCode,
                                    @Nonnull final IdentifierType aSenderParticipantID,
                                    @Nonnull final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
                                    @Nonnull final EPredefinedProcessIdentifier eProcessID,
                                    @Nullable final Iterable <? extends ConceptValue> aValues)
  {
    aRet.setDocumentUniversalUniqueIdentifier (ToopXSDHelper120.createIdentifier ("UUID",
                                                                                  null,
                                                                                  UUID.randomUUID ().toString ()));
    aRet.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    aRet.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    aRet.setCopyIndicator (ToopXSDHelper120.createIndicator (false));
    aRet.setSpecificationIdentifier (ToopXSDHelper120.createIdentifier ("toop-doctypeid-qns",
                                                                        eDocumentTypeID.getID ()
                                                                                       .substring (0,
                                                                                                   eDocumentTypeID.getID ()
                                                                                                                  .indexOf ("##"))));
    aRet.setDataConsumerDocumentIdentifier (ToopXSDHelper120.createIdentifier ("whatsoever", null, "DC-ID-17"));

    if (false)
      aRet.setDataRequestIdentifier (ToopXSDHelper120.createIdentifier (UUID.randomUUID ().toString ()));

    {
      // Document type ID
      aRet.setDocumentTypeIdentifier (ToopXSDHelper120.createIdentifier (eDocumentTypeID.getScheme (),
                                                                         eDocumentTypeID.getID ()));
      // Process ID
      aRet.setProcessIdentifier (ToopXSDHelper120.createIdentifier (eProcessID.getScheme (), eProcessID.getID ()));
    }

    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper120.createIdentifier ("whatsoever", "9914", "ATU12345678"));
      aDC.setDCName (ToopXSDHelper120.createText ("Helger Enterprises"));
      {
        // Sender participant ID
        final IdentifierType aID = aSenderParticipantID.clone ();
        aID.setSchemeAgencyID ("0002");
        aDC.setDCElectronicAddressIdentifier (aID);
      }
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper120.createCode (sDCCountryCode));
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

    if (aValues != null)
      for (final ConceptValue aCV : aValues)
      {
        final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
        aReq.setDataElementRequestIdentifier (ToopXSDHelper120.createIdentifier ("bla"));
        {
          final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
          aSrcConcept.setConceptTypeCode (ToopXSDHelper120.createCode (EConceptType.DC.getID ()));
          aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper120.createIndicator (false));
          aSrcConcept.setConceptNamespace (ToopXSDHelper120.createIdentifier (aCV.getNamespace ()));
          aSrcConcept.setConceptName (ToopXSDHelper120.createText (aCV.getValue ()));
          aSrcConcept.addConceptDefinition (ToopXSDHelper120.createText ("Definition of " + aCV.getValue ()));
          aReq.setConceptRequest (aSrcConcept);
        }

        aRet.addDataElementRequest (aReq);
      }
  }

  @Deprecated
  @Nonnull
  public static TDETOOPRequestType createMockRequest (@Nonnull final TDEDataRequestSubjectType aRequestSubject,
                                                      @Nonnull final String sDCCountryCode,
                                                      @Nonnull final IdentifierType aSenderParticipantID,
                                                      @Nonnull final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
                                                      @Nonnull final EPredefinedProcessIdentifier eProcessID,
                                                      @Nullable final Iterable <? extends ConceptValue> aValues)
  {
    ValueEnforcer.notNull (aRequestSubject, "RequestSubject");
    ValueEnforcer.notNull (aSenderParticipantID, "SenderParticipantID");
    ValueEnforcer.notNull (eDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notNull (eProcessID, "ProcessID");

    final TDETOOPRequestType aRet = new TDETOOPRequestType ();
    _fillRequest (aRet, aRequestSubject, sDCCountryCode, aSenderParticipantID, eDocumentTypeID, eProcessID, aValues);
    return aRet;
  }

  @Deprecated
  @Nonnull
  public static TDETOOPResponseType createMockResponse (@Nonnull final IdentifierType aSenderParticipantID,
                                                        @Nonnull final TDEDataRequestSubjectType aRequestSubject,
                                                        @Nonnull final String sDCCountryCode,
                                                        @Nonnull final String sDPCountryCode,
                                                        @Nonnull final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
                                                        @Nonnull final EPredefinedProcessIdentifier eProcessID,
                                                        @Nullable final Iterable <? extends ConceptValue> aValues)
  {
    ValueEnforcer.notNull (aSenderParticipantID, "SenderParticipantID");
    ValueEnforcer.notNull (aRequestSubject, "RequestSubject");
    ValueEnforcer.notNull (eDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notNull (eProcessID, "ProcessID");

    final TDETOOPResponseType aRet = new TDETOOPResponseType ();
    // Values are added below
    _fillRequest (aRet, aRequestSubject, sDCCountryCode, aSenderParticipantID, eDocumentTypeID, eProcessID, null);

    aRet.setDataRequestIdentifier (ToopXSDHelper120.createIdentifier ("schas", "uuid", UUID.randomUUID ().toString ()));

    {
      final TDEDataRequestAuthorizationType aAuth = new TDEDataRequestAuthorizationType ();
      final BinaryObjectType aBO = new BinaryObjectType ();
      aBO.setValue ("11101010101010001110101".getBytes (StandardCharsets.ISO_8859_1));
      aBO.setMimeCode (CMimeType.APPLICATION_OCTET_STREAM.getAsString ());
      aAuth.setDataRequestConsentToken (aBO);
      aRet.setDataRequestAuthorization (aAuth);
    }

    if (aValues != null)
    {
      for (final ConceptValue aCV : aValues)
      {
        final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
        aReq.setDataElementRequestIdentifier (ToopXSDHelper120.createIdentifier ("bla"));
        {
          final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
          aSrcConcept.setConceptTypeCode (ToopXSDHelper120.createCode (EConceptType.DC.getID ()));
          aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper120.createIndicator (false));
          aSrcConcept.setConceptNamespace (ToopXSDHelper120.createIdentifier (aCV.getNamespace ()));
          aSrcConcept.setConceptName (ToopXSDHelper120.createText (aCV.getValue ()));
          aSrcConcept.addConceptDefinition (ToopXSDHelper120.createText ("Definition of " + aCV.getValue ()));
          {
            final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
            aToopConcept.setConceptTypeCode (ToopXSDHelper120.createCode (EConceptType.TC.getID ()));
            aToopConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper120.createIndicator (false));
            aToopConcept.setConceptNamespace (ToopXSDHelper120.createIdentifier (aCV.getNamespace () + "-toop"));
            aToopConcept.setConceptName (ToopXSDHelper120.createText ("toop." + aCV.getValue ()));
            {
              final TDEConceptRequestType aDPConcept = new TDEConceptRequestType ();
              aDPConcept.setConceptTypeCode (ToopXSDHelper120.createCode (EConceptType.DP.getID ()));
              aDPConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper120.createIndicator (false));
              aDPConcept.setConceptNamespace (ToopXSDHelper120.createIdentifier (aCV.getNamespace () + "-dp"));
              aDPConcept.setConceptName (ToopXSDHelper120.createText ("dp." + aCV.getValue ()));
              aToopConcept.addConceptRequest (aDPConcept);
            }
            aSrcConcept.addConceptRequest (aToopConcept);
          }
          aReq.setConceptRequest (aSrcConcept);
        }
        aRet.addDataElementRequest (aReq);
      }
    }
    else
    {
      final TDEDocumentRequestType aDR = new TDEDocumentRequestType ();
      aDR.setDocumentRequestIdentifier (ToopXSDHelper120.createIdentifier ("CertificatePDF-1234"));
      aDR.setDocumentRequestTypeCode (ToopXSDHelper120.createCode ("pdf"));
      aDR.setPreferredDocumentMimeTypeCode (ToopXSDHelper120.createCode (CMimeType.APPLICATION_PDF.getAsString ()));
      aRet.addDocumentRequest (aDR);
    }

    {
      final TDEDataProviderType aDP = new TDEDataProviderType ();
      aDP.setDPIdentifier (ToopXSDHelper120.createIdentifier ("schas", null, "atbla"));
      aDP.setDPName (ToopXSDHelper120.createText ("Register1"));
      aDP.setDPElectronicAddressIdentifier (ToopXSDHelper120.createIdentifier ("0002",
                                                                               "iso6523-actorid-upis",
                                                                               "9915:test"));
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper120.createCode (sDPCountryCode));
      aDP.setDPLegalAddress (aAddress);
      aRet.addDataProvider (aDP);
    }

    return aRet;
  }

  /**
   * Create a new response with all cloned values from the request. Additionally
   * the "DataRequestIdentifier" and the "DocumentUniversalUniqueIdentifier" is
   * set accordingly.
   *
   * @param aRequest
   *        Source request. May not be <code>null</code>.
   * @return Destination response. Never <code>null</code>.
   */
  @Nonnull
  public static TDETOOPResponseType createResponse (@Nonnull final TDETOOPRequestType aRequest)
  {
    final TDETOOPResponseType aResponse = new TDETOOPResponseType ();
    aRequest.cloneTo (aResponse);
    // Set response specific stuff
    aResponse.setDataRequestIdentifier (aRequest.getDocumentUniversalUniqueIdentifier ().clone ());
    // Create a new UUID
    aResponse.setDocumentUniversalUniqueIdentifier (ToopXSDHelper120.createIdentifier ("UUID",
                                                                                       null,
                                                                                       UUID.randomUUID ().toString ()));
    return aResponse;
  }

  @Nullable
  private static IdentifierType _getClone (@Nullable final IdentifierType x)
  {
    return x == null ? null : x.clone ();
  }

  /**
   * Create a single error object.
   *
   * @param sDPIdentifier
   *        Optional DP identifier. May be <code>null</code>.
   * @param eOrigin
   *        Error origin. May not be <code>null</code>.
   * @param eCategory
   *        Error category. May not be <code>null</code>.
   * @param aErrorCode
   *        Error code. May not be <code>null</code>.
   * @param eSeverity
   *        Error severity. May not be <code>null</code>.
   * @param aMLT
   *        Multilingual text to use. May not be <code>null</code>.
   * @param sTechDetails
   *        Optional technical details. May be <code>null</code>.
   * @return Never <code>null</code>.
   * @since 0.9.2
   */
  @Nonnull
  public static TDEErrorType createError (@Nullable final String sDPIdentifier,
                                          @Nonnull final EToopErrorOrigin eOrigin,
                                          @Nonnull final EToopErrorCategory eCategory,
                                          @Nonnull final IToopErrorCode aErrorCode,
                                          @Nonnull final EToopErrorSeverity eSeverity,
                                          @Nonnull final IMultilingualText aMLT,
                                          @Nullable final String sTechDetails)
  {
    final TDEErrorType ret = new TDEErrorType ();
    if (StringHelper.hasText (sDPIdentifier))
      ret.setDataProviderIdentifier (ToopXSDHelper120.createIdentifier (sDPIdentifier));
    ret.setOrigin (ToopXSDHelper120.createCode (eOrigin.getID ()));
    ret.setCategory (ToopXSDHelper120.createCode (eCategory.getID ()));
    ret.setErrorCode (ToopXSDHelper120.createCode (aErrorCode.getID ()));
    ret.setSeverity (ToopXSDHelper120.createCode (eSeverity.getID ()));
    for (final Map.Entry <Locale, String> aEntry : aMLT.texts ().entrySet ())
      ret.addErrorText (ToopXSDHelper120.createText (aEntry.getKey (), aEntry.getValue ()));
    if (StringHelper.hasText (sTechDetails))
      ret.setTechnicalDetails (ToopXSDHelper120.createText (sTechDetails));
    ret.setTimestamp (PDTXMLConverter.getXMLCalendarNow ());
    return ret;
  }
}
