package eu.toop.commons;

import java.io.Serializable;

public class MSDataRequest implements Serializable {
  private final String _identifier;

  public MSDataRequest(final String identifier) {
    _identifier = identifier;
  }

  public String getIdentifier() {
    return _identifier;
  }
}
