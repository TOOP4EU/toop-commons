package eu.toop.commons.exchange.mock;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.DevelopersNote;
import com.helger.commons.serialize.SerializationHelper;

import eu.toop.commons.exchange.IMSDataRequest;

/**
 * Mock member state request (DC to DP)
 *
 * @author Anton
 */
@Deprecated
@DevelopersNote("Mock class")
public class MSDataRequest implements IMSDataRequest, IMockDataElement {
  private final String _identifier;

  public MSDataRequest(final String identifier) {
    _identifier = identifier;
  }

  public String getIdentifier() {
    return _identifier;
  }

  @Nonnull
  public static Function<byte[], MSDataRequest> getDeserializerFunction() {
    return SerializationHelper::getDeserializedObject;
  }
}
