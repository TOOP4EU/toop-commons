package eu.toop.commons.exchange.mock;

import java.io.InputStream;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.DevelopersNote;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.mime.IMimeType;
import com.helger.commons.serialize.SerializationHelper;

import eu.toop.commons.exchange.IToopDataElement;

/**
 * Base interface for request and response data, for Member State and Toop
 * internal stuff.
 *
 * @author Philip Helger
 */
@Deprecated
@DevelopersNote ("Mock class")
public interface IMockDataElement extends IToopDataElement {
  @Nonnull
  default IMimeType getMimeType () {
    return CMimeType.APPLICATION_OCTET_STREAM;
  }

  @Nonnull
  default InputStream getAsSerializedVersion () {
    final byte[] aData = SerializationHelper.getSerializedByteArray(this);
    return new NonBlockingByteArrayInputStream(aData);
  }
}
