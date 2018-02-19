package eu.toop.commons;

import java.io.Serializable;

import javax.annotation.Nullable;

/**
 * A Mockup TOOPMessageBundle
 */
public class ToopMessageBundle implements Serializable {
  private MSDataRequest _msDataRequest;
  private MSDataResponse _msDataResponse;
  private ToopDataRequest _toopDataRequest;
  private ToopDataResponse _toopDataResponse;

  @Nullable
  public MSDataRequest getMsDataRequest() {
    return _msDataRequest;
  }

  public void setMsDataRequest(@Nullable final MSDataRequest msDataRequest) {
    _msDataRequest = msDataRequest;
  }

  @Nullable
  public MSDataResponse getMsDataResponse() {
    return _msDataResponse;
  }

  public void setMsDataResponse(@Nullable final MSDataResponse msDataResponse) {
    _msDataResponse = msDataResponse;
  }

  @Nullable
  public ToopDataRequest getToopDataRequest() {
    return _toopDataRequest;
  }

  public void setToopDataRequest(@Nullable final ToopDataRequest toopDataRequest) {
    _toopDataRequest = toopDataRequest;
  }

  @Nullable
  public ToopDataResponse getToopDataResponse() {
    return _toopDataResponse;
  }

  public void setToopDataResponse(@Nullable final ToopDataResponse toopDataResponse) {
    _toopDataResponse = toopDataResponse;
  }
}
