package eu.toop.commons.exchange.mock;

import java.io.InputStream;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.DevelopersNote;
import com.helger.commons.annotation.Nonempty;
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

import eu.toop.commons.exchange.IMSDataRequest;

/**
 * Mock member state request (DC to DP)
 *
 * @author Anton
 */
@Deprecated
@DevelopersNote("Mock class")
public class MSDataRequest implements IMSDataRequest {
  private final String _countryCode;
  private final String _docTypeID;
  private final String _identifier;

  public MSDataRequest(@Nonnull @Nonempty final String countryCode, @Nonnull @Nonempty final String documentTypeID,
      final String identifier) {
    ValueEnforcer.notEmpty(countryCode, "CountryCode");
    ValueEnforcer.notEmpty(documentTypeID, "DocumentTypeID");
    _countryCode = countryCode;
    _docTypeID = documentTypeID;
    _identifier = identifier;
  }

  @Nonnull
  @Nonempty
  public String getDestinationCountryCode() {
    return _countryCode;
  }

  @Nonnull
  @Nonempty
  public String getDocumentTypeID() {
    return _docTypeID;
  }

  public String getIdentifier() {
    return _identifier;
  }

  public IMimeType getMimeType() {
    return CMimeType.APPLICATION_XML;
  }

  @Nonnull
  public InputStream getAsSerializedVersion() {
    final IMicroDocument aDoc = new MicroDocument();
    final IMicroElement aElement = aDoc.appendElement("ms-request");
    aElement.appendElement("country-code").appendText(_countryCode);
    aElement.appendElement("document-type").appendText(_docTypeID);
    aElement.appendElement("identifier").appendText(_identifier);

    return new NonBlockingByteArrayInputStream(MicroWriter.getNodeAsBytes(aDoc));
  }

  @Override
  public String toString() {
    return new ToStringGenerator(this).append("CountryCode", _countryCode).append("DocTypeID", _docTypeID)
        .append("Identifier", _identifier).getToString();
  }

  @Nonnull
  public static Function<byte[], MSDataRequest> getDeserializerFunction() {
    return x -> {
      final IMicroDocument aDoc = MicroReader.readMicroXML(x);
      if (aDoc != null) {
        final IMicroElement eRoot = aDoc.getDocumentElement();
        if (eRoot != null) {
          final String sCountryCode = MicroHelper.getChildTextContent(eRoot, "country-code");
          final String sDocumentTypeID = MicroHelper.getChildTextContent(eRoot, "document-type");
          final String sIdentifier = MicroHelper.getChildTextContent(eRoot, "identifier");
          return new MSDataRequest(sCountryCode, sDocumentTypeID, sIdentifier);
        }
      }
      return null;
    };
  }
}
