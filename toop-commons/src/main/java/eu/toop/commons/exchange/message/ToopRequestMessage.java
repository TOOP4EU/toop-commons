
package eu.toop.commons.exchange.message;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import eu.toop.commons.exchange.IMSDataRequest;

/**
 * Generic TOOP request (DC to DP).
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class ToopRequestMessage implements Serializable {
  private IMSDataRequest _msDataRequest;

  @Nullable
  public IMSDataRequest getMSDataRequest () {
    return _msDataRequest;
  }

  public void setMSDataRequest (@Nullable final IMSDataRequest msDataRequest) {
    _msDataRequest = msDataRequest;
  }
}
