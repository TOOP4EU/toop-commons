package eu.toop.commons;

import java.io.Serializable;

public class ToopDataRequest implements Serializable {
  private final String _identifier;

  public ToopDataRequest(final String identifier) {
    _identifier = identifier;
  }

  public String getIdentifier() {
    return _identifier;
  }
}
