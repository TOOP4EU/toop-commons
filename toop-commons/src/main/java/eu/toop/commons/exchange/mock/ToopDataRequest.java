package eu.toop.commons.exchange.mock;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.DevelopersNote;
import com.helger.commons.serialize.SerializationHelper;

import eu.toop.commons.exchange.IToopDataRequest;

/**
 * Mock TOOP Demo UI request (DC to DP)
 *
 * @author Anton
 */
@Deprecated
@DevelopersNote("Mock class")
public class ToopDataRequest implements IToopDataRequest, IMockDataElement {
  private final String _identifier;

  public ToopDataRequest(final String identifier) {
    _identifier = identifier;
  }

  public String getIdentifier() {
    return _identifier;
  }

  @Nonnull
  public static Function<byte[], ToopDataRequest> getDeserializerFunction() {
    return SerializationHelper::getDeserializedObject;
  }
}
