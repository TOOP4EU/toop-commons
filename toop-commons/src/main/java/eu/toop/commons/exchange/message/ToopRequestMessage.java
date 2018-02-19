
package eu.toop.commons.exchange.message;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import eu.toop.commons.exchange.IMSDataRequest;
import eu.toop.commons.exchange.IToopDataRequest;

/**
 * Generic TOOP request (DC to DP).
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class ToopRequestMessage implements Serializable {
  private IMSDataRequest _msDataRequest;
  private IToopDataRequest _toopDataRequest;

  @Nullable
  public IMSDataRequest getMSDataRequest() {
    return _msDataRequest;
  }

  public void setMSDataRequest(@Nullable final IMSDataRequest msDataRequest) {
    _msDataRequest = msDataRequest;
  }

  @Nullable
  public IToopDataRequest getToopDataRequest() {
    return _toopDataRequest;
  }

  public void setToopDataRequest(@Nullable final IToopDataRequest toopDataRequest) {
    _toopDataRequest = toopDataRequest;
  }
}
