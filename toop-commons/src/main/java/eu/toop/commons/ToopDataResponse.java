package eu.toop.commons;

import java.io.Serializable;

public class ToopDataResponse implements Serializable {
  private final String _identifier;

  public ToopDataResponse(final String identifier) {
    _identifier = identifier;
  }

  public String getIdentifier() {
    return _identifier;
  }
}
