/**
 * Copyright (C) 2018 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.toop.commons.exchange.message;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.string.ToStringGenerator;

import eu.toop.commons.exchange.IMSDataResponse;
import eu.toop.commons.exchange.IToopDataResponse;

/**
 * Generic TOPP response message (DP to DC) that also includes the source
 * request data (DC to DP)
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class ToopResponseMessage extends ToopRequestMessage {
  private IMSDataResponse _msDataResponse;
  private IToopDataResponse _toopDataResponse;

  @Nullable
  public IMSDataResponse getMSDataResponse () {
    return _msDataResponse;
  }

  public void setMSDataResponse (@Nullable final IMSDataResponse msDataResponse) {
    _msDataResponse = msDataResponse;
  }

  @Nullable
  public IToopDataResponse getToopDataResponse () {
    return _toopDataResponse;
  }

  public void setToopDataResponse (@Nullable final IToopDataResponse toopDataResponse) {
    _toopDataResponse = toopDataResponse;
  }

  @Override
  public String toString () {
    return ToStringGenerator.getDerived (super.toString ()).append ("MSDataResponse", _msDataResponse)
                            .appendIfNotNull ("ToopDataResponse", _toopDataResponse).getToString ();
  }
}
