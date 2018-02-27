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
package eu.toop.commons.exchange.mock;

import java.io.InputStream;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.mime.IMimeType;
import com.helger.commons.string.ToStringGenerator;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.MicroDocument;
import com.helger.xml.microdom.serialize.MicroReader;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.microdom.util.MicroHelper;

import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.exchange.IMSDataRequest;

/**
 * Member state request (DC to DP)
 *
 * @author Philip Helger
 */
public class MSDataRequest implements IMSDataRequest {
  private final String m_sSenderParticipantID;
  private final String m_sCountryCode;
  private final String m_sDocTypeID;
  private final String m_sProcessID;
  private final ICommonsList<ConceptValue> m_aValues;

  public MSDataRequest (@Nonnull @Nonempty final String sSenderParticipantID,
                        @Nonnull @Nonempty final String sCountryCode, @Nonnull @Nonempty final String sDocumentTypeID,
                        @Nonnull @Nonempty final String sProcessID,
                        @Nullable final Iterable<? extends ConceptValue> aValues) {
    ValueEnforcer.notEmpty (sSenderParticipantID, "SenderParticipantID");
    ValueEnforcer.notEmpty (sCountryCode, "CountryCode");
    ValueEnforcer.notEmpty (sDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notEmpty (sProcessID, "ProcessID");
    m_sSenderParticipantID = sSenderParticipantID;
    m_sCountryCode = sCountryCode;
    m_sDocTypeID = sDocumentTypeID;
    m_sProcessID = sProcessID;
    m_aValues = new CommonsArrayList<> (aValues);
  }

  @Nonnull
  @Nonempty
  public String getSenderParticipantID () {
    return m_sSenderParticipantID;
  }

  @Nonnull
  @Nonempty
  public String getDestinationCountryCode () {
    return m_sCountryCode;
  }

  @Nonnull
  @Nonempty
  public String getDocumentTypeID () {
    return m_sDocTypeID;
  }

  @Nonnull
  @Nonempty
  public String getProcessID () {
    return m_sProcessID;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList<ConceptValue> getAllRequestValues () {
    return m_aValues.getClone ();
  }

  public IMimeType getMimeType () {
    return CMimeType.APPLICATION_XML;
  }

  @Nonnull
  public InputStream getAsSerializedVersion () {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement aElement = aDoc.appendElement ("ms-request");
    aElement.setAttribute ("version", "1.0");
    aElement.appendElement ("sender-pid").appendText (m_sSenderParticipantID);
    aElement.appendElement ("country-code").appendText (m_sCountryCode);
    aElement.appendElement ("document-type").appendText (m_sDocTypeID);
    aElement.appendElement ("process").appendText (m_sProcessID);
    // Add all request values
    for (final ConceptValue aValue : m_aValues) {
      final IMicroElement eValue = aElement.appendElement ("request-value");
      eValue.setAttribute ("namespace", aValue.getNamespace ());
      eValue.appendText (aValue.getValue ());
    }

    return new NonBlockingByteArrayInputStream (MicroWriter.getNodeAsBytes (aDoc));
  }

  @Override
  public String toString () {
    return new ToStringGenerator (this).append ("SenderParticipantID", m_sSenderParticipantID)
                                       .append ("CountryCode", m_sCountryCode).append ("DocTypeID", m_sDocTypeID)
                                       .append ("ProcessID", m_sProcessID).append ("ConceptValues", m_aValues)
                                       .getToString ();
  }

  @Nonnull
  public static Function<byte[], MSDataRequest> getDeserializerFunction () {
    return x -> {
      final IMicroDocument aDoc = MicroReader.readMicroXML (x);
      if (aDoc != null) {
        final IMicroElement eRoot = aDoc.getDocumentElement ();
        if (eRoot != null) {
          final String sSenderParticipantID = MicroHelper.getChildTextContent (eRoot, "sender-pid");
          final String sCountryCode = MicroHelper.getChildTextContent (eRoot, "country-code");
          final String sDocumentTypeID = MicroHelper.getChildTextContent (eRoot, "document-type");
          final String sProcessID = MicroHelper.getChildTextContent (eRoot, "process");
          final ICommonsList<ConceptValue> aValues = new CommonsArrayList<> ();
          for (final IMicroElement eRequest : eRoot.getAllChildElements ("request-value"))
            aValues.add (new ConceptValue (eRequest.getAttributeValue ("namespace"),
                                           eRequest.getTextContentTrimmed ()));
          return new MSDataRequest (sSenderParticipantID, sCountryCode, sDocumentTypeID, sProcessID, aValues);
        }
      }
      return null;
    };
  }
}
