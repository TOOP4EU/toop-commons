package eu.toop.commons;

/*
 * A Mockup TOOPDataResponse
 */

import java.io.Serializable;

public class ToopDataResponse implements Serializable {
  public String identifier;

  public ToopDataResponse(String identifier) {
    this.identifier = identifier;
  }
}
