
package eu.toop.commons.exchange.message;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import eu.toop.commons.exchange.IMSDataResponse;
import eu.toop.commons.exchange.IToopDataRequest;
import eu.toop.commons.exchange.IToopDataResponse;

/**
 * Generic TOPP response message (DP to DC) that also includes the source
 * request data (DC to DP)
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class ToopResponseMessage extends ToopRequestMessage {
  private IToopDataRequest _toopDataRequest;
  private IMSDataResponse _msDataResponse;
  private IToopDataResponse _toopDataResponse;

  @Nullable
  public IToopDataRequest getToopDataRequest() {
    return _toopDataRequest;
  }

  public void setToopDataRequest(@Nullable final IToopDataRequest toopDataRequest) {
    _toopDataRequest = toopDataRequest;
  }

  @Nullable
  public IMSDataResponse getMSDataResponse() {
    return _msDataResponse;
  }

  public void setMSDataResponse(@Nullable final IMSDataResponse msDataResponse) {
    _msDataResponse = msDataResponse;
  }

  @Nullable
  public IToopDataResponse getToopDataResponse() {
    return _toopDataResponse;
  }

  public void setToopDataResponse(@Nullable final IToopDataResponse toopDataResponse) {
    _toopDataResponse = toopDataResponse;
  }
}
