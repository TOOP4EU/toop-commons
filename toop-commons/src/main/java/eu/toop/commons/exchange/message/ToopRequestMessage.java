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

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.string.ToStringGenerator;

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
  public IMSDataRequest getMSDataRequest () {
    return _msDataRequest;
  }

  public void setMSDataRequest (@Nullable final IMSDataRequest msDataRequest) {
    _msDataRequest = msDataRequest;
  }

  @Nullable
  public IToopDataRequest getToopDataRequest () {
    return _toopDataRequest;
  }

  public void setToopDataRequest (@Nullable final IToopDataRequest toopDataRequest) {
    _toopDataRequest = toopDataRequest;
  }

  @Override
  public String toString () {
    return new ToStringGenerator (this).append ("MSDataRequest", _msDataRequest)
                                       .appendIfNotNull ("ToopDataRequest", _toopDataRequest).getToString ();
  }
}
