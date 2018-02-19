package eu.toop.commons;

import java.io.Serializable;

public class MSDataResponse implements Serializable {
  private final String _identifier;

  public MSDataResponse(final String identifier) {
    _identifier = identifier;
  }

  public String getIdentifier() {
    return _identifier;
  }
}
