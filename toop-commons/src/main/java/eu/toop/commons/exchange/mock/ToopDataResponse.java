package eu.toop.commons.exchange.mock;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.DevelopersNote;
import com.helger.commons.serialize.SerializationHelper;

import eu.toop.commons.exchange.IToopDataResponse;

/**
 * Mock TOOP Demo UI response (DP to DC)
 *
 * @author Anton
 */
@Deprecated
@DevelopersNote("Mock class")
public class ToopDataResponse implements IToopDataResponse, IMockDataElement {
  private final String _identifier;

  public ToopDataResponse(final String identifier) {
    _identifier = identifier;
  }

  public String getIdentifier() {
    return _identifier;
  }

  @Nonnull
  public static Function<byte[], ToopDataResponse> getDeserializerFunction() {
    return SerializationHelper::getDeserializedObject;
  }
}
