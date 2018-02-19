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
@DevelopersNote ("Mock class")
public class MSDataRequest implements IMSDataRequest {
  private final String _countryCode;
  private final String _docTypeID;
  private final String _processID;
  private final boolean _isProduction;
  private final String _identifier;

  public MSDataRequest (@Nonnull @Nonempty final String sCountryCode, @Nonnull @Nonempty final String sDocumentTypeID,
                        @Nonnull @Nonempty final String sProcessID, final boolean bIsProduction,
                        final String identifier) {
    ValueEnforcer.notEmpty(sCountryCode, "CountryCode");
    ValueEnforcer.notEmpty(sDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notEmpty(sProcessID, "ProcessID");
    _countryCode = sCountryCode;
    _docTypeID = sDocumentTypeID;
    _processID = sProcessID;
    _isProduction = bIsProduction;
    _identifier = identifier;
  }

  @Nonnull
  @Nonempty
  public String getDestinationCountryCode () {
    return _countryCode;
  }

  @Nonnull
  @Nonempty
  public String getDocumentTypeID () {
    return _docTypeID;
  }

  @Nonnull
  @Nonempty
  public String getProcessID () {
    return _processID;
  }

  public boolean isProduction () {
    return _isProduction;
  }

  public String getIdentifier () {
    return _identifier;
  }

  public IMimeType getMimeType () {
    return CMimeType.APPLICATION_XML;
  }

  @Nonnull
  public InputStream getAsSerializedVersion () {
    final IMicroDocument aDoc = new MicroDocument();
    final IMicroElement aElement = aDoc.appendElement("ms-request");
    aElement.setAttribute("production", _isProduction);
    aElement.appendElement("country-code").appendText(_countryCode);
    aElement.appendElement("document-type").appendText(_docTypeID);
    aElement.appendElement("process").appendText(_processID);
    aElement.appendElement("identifier").appendText(_identifier);

    return new NonBlockingByteArrayInputStream(MicroWriter.getNodeAsBytes(aDoc));
  }

  @Override
  public String toString () {
    return new ToStringGenerator(this).append("CountryCode", _countryCode).append("DocTypeID", _docTypeID)
                                      .append("ProcessID", _processID).append("Production", _isProduction)
                                      .append("Identifier", _identifier).getToString();
  }

  @Nonnull
  public static Function<byte[], MSDataRequest> getDeserializerFunction () {
    return x -> {
      final IMicroDocument aDoc = MicroReader.readMicroXML(x);
      if (aDoc != null) {
        final IMicroElement eRoot = aDoc.getDocumentElement();
        if (eRoot != null) {
          final String sCountryCode = MicroHelper.getChildTextContent(eRoot, "country-code");
          final String sDocumentTypeID = MicroHelper.getChildTextContent(eRoot, "document-type");
          final String sProcessID = MicroHelper.getChildTextContent(eRoot, "process");
          final boolean bIsProduction = eRoot.getAttributeValueAsBool("production", false);
          final String sIdentifier = MicroHelper.getChildTextContent(eRoot, "identifier");
          return new MSDataRequest(sCountryCode, sDocumentTypeID, sProcessID, bIsProduction, sIdentifier);
        }
      }
      return null;
    };
  }
}
