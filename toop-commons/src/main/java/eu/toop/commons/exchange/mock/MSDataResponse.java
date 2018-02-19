package eu.toop.commons.exchange.mock;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.DevelopersNote;
import com.helger.commons.serialize.SerializationHelper;

import eu.toop.commons.exchange.IMSDataResponse;

/**
 * Mock member state response (DP to DC)
 *
 * @author Anton
 */
@Deprecated
@DevelopersNote ("Mock class")
public class MSDataResponse implements IMSDataResponse, IMockDataElement {
  private final String _identifier;

  public MSDataResponse (final String identifier) {
    _identifier = identifier;
  }

  public String getIdentifier () {
    return _identifier;
  }

  @Nonnull
  public static Function<byte[], MSDataResponse> getDeserializerFunction () {
    return SerializationHelper::getDeserializedObject;
  }
}
